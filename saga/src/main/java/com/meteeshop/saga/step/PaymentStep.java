package com.meteeshop.saga.step;

import com.meteeshop.common.constant.PaymentStatus;
import com.meteeshop.common.dto.payment.PaymentRequestDto;
import com.meteeshop.common.dto.payment.PaymentResponseDto;
import com.meteeshop.saga.workflow.WorkFlowStep;
import com.meteeshop.saga.workflow.WorkflowStepStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class PaymentStep implements WorkFlowStep {

    private final WebClient webClient;
    private final PaymentRequestDto requestDTO;
    private WorkflowStepStatus stepStatus = WorkflowStepStatus.PENDING;

    public PaymentStep(WebClient webClient, PaymentRequestDto requestDTO) {
        this.webClient = webClient;
        this.requestDTO = requestDTO;
    }

    @Override
    public WorkflowStepStatus getStatus() {
        return this.stepStatus;
    }

    @Override
    public Mono<Boolean> process() {
        return this.webClient
                .post()
                .uri("/payment/debit")
                .body(BodyInserters.fromValue(this.requestDTO))
                .retrieve()
                .bodyToMono(PaymentResponseDto.class)
                .map(r -> r.getStatus().equals(PaymentStatus.PAYMENT_APPROVED))
                .doOnNext(b -> this.stepStatus = b ? WorkflowStepStatus.COMPLETE : WorkflowStepStatus.FAILED);
    }

    @Override
    public Mono<Boolean> compensate() {
        return this.webClient
                .post()
                .uri("/payment/credit")
                .body(BodyInserters.fromValue(this.requestDTO))
                .retrieve()
                .bodyToMono(Void.class)
                .map(r -> true)
                .onErrorReturn(false);
    }

}
