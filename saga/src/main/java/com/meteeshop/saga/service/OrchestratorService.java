package com.meteeshop.saga.service;

import com.meteeshop.common.constant.OrderStatus;
import com.meteeshop.common.dto.inventory.InventoryRequestDto;
import com.meteeshop.common.dto.orchestrator.OrchestratorRequestDto;
import com.meteeshop.common.dto.orchestrator.OrchestratorResponseDto;
import com.meteeshop.common.dto.payment.PaymentRequestDto;
import com.meteeshop.saga.step.InventoryStep;
import com.meteeshop.saga.step.PaymentStep;
import com.meteeshop.saga.workflow.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class OrchestratorService {


    @Qualifier("payment")
    private final WebClient paymentClient;

    @Qualifier("inventory")
    private final WebClient inventoryClient;


    public OrchestratorService(WebClient paymentClient, WebClient inventoryClient) {
        this.paymentClient = paymentClient;
        this.inventoryClient = inventoryClient;
    }

    public Mono<OrchestratorResponseDto> orderProduct(final OrchestratorRequestDto requestDTO){
        WorkFlow orderWorkflow = this.getOrderWorkflow(requestDTO);
        return Flux.fromStream(() -> orderWorkflow.getSteps().stream())
                .flatMap(WorkFlowStep::process)
                .handle(((aBoolean, synchronousSink) -> {
                    if(aBoolean)
                        synchronousSink.next(true);
                    else
                        synchronousSink.error(new WorkflowException("create order failed!"));
                }))
                .then(Mono.fromCallable(() -> getResponseDTO(requestDTO, OrderStatus.ORDER_COMPLETED)))
                .onErrorResume(ex -> this.compensateOrder(orderWorkflow, requestDTO));

    }


    private Mono<OrchestratorResponseDto> compensateOrder(final WorkFlow workflow, final OrchestratorRequestDto requestDTO){
        return Flux.fromStream(() -> workflow.getSteps().stream())
                .filter(wf -> wf.getStatus().equals(WorkflowStepStatus.COMPLETE))
                .flatMap(WorkFlowStep::compensate)
                .retry(3)
                .then(Mono.just(this.getResponseDTO(requestDTO, OrderStatus.ORDER_CANCELLED)));
    }

    private WorkFlow getOrderWorkflow(OrchestratorRequestDto requestDTO){
        WorkFlowStep paymentStep = new PaymentStep(this.paymentClient, this.getPaymentRequestDTO(requestDTO));
        WorkFlowStep inventoryStep = new InventoryStep(this.inventoryClient, this.getInventoryRequestDTO(requestDTO));
        return new OrderWorkFlow(List.of(paymentStep, inventoryStep));
    }

    private OrchestratorResponseDto getResponseDTO(OrchestratorRequestDto requestDTO, OrderStatus status){
        OrchestratorResponseDto responseDTO = new OrchestratorResponseDto();
        responseDTO.setOrderId(requestDTO.getOrderId());
        responseDTO.setAmount(requestDTO.getAmount());
        responseDTO.setProductId(requestDTO.getProductId());
        responseDTO.setUserId(requestDTO.getUserId());
        responseDTO.setStatus(status);
        return responseDTO;
    }

    private PaymentRequestDto getPaymentRequestDTO(OrchestratorRequestDto requestDTO){
        PaymentRequestDto paymentRequestDTO = new PaymentRequestDto();
        paymentRequestDTO.setUserId(requestDTO.getUserId());
        paymentRequestDTO.setAmount(requestDTO.getAmount());
        paymentRequestDTO.setOrderId(requestDTO.getOrderId());
        return paymentRequestDTO;
    }

    private InventoryRequestDto getInventoryRequestDTO(OrchestratorRequestDto requestDTO){
        InventoryRequestDto inventoryRequestDTO = new InventoryRequestDto();
        inventoryRequestDTO.setUserId(requestDTO.getUserId());
        inventoryRequestDTO.setProductId(requestDTO.getProductId());
        inventoryRequestDTO.setOrderId(requestDTO.getOrderId());
        return inventoryRequestDTO;
    }


}
