package com.meteeshop.saga.step;

import com.meteeshop.common.constant.InventoryStatus;
import com.meteeshop.common.dto.inventory.InventoryRequestDto;
import com.meteeshop.common.dto.inventory.InventoryResponseDto;
import com.meteeshop.saga.workflow.WorkFlowStep;
import com.meteeshop.saga.workflow.WorkflowStepStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class InventoryStep implements WorkFlowStep {
    private final WebClient webClient;
    private final InventoryRequestDto requestDTO;
    private WorkflowStepStatus stepStatus = WorkflowStepStatus.PENDING;

    public InventoryStep(WebClient webClient, InventoryRequestDto requestDTO) {
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
                .uri("/inventory/deduct")
                .body(BodyInserters.fromValue(this.requestDTO))
                .retrieve()
                .bodyToMono(InventoryResponseDto.class)
                .map(r -> r.getStatus().equals(InventoryStatus.AVAILABLE))
                .doOnNext(b -> this.stepStatus = b ? WorkflowStepStatus.COMPLETE : WorkflowStepStatus.FAILED);
    }

    @Override
    public Mono<Boolean> compensate() {
        return this.webClient
                .post()
                .uri("/inventory/add")
                .body(BodyInserters.fromValue(this.requestDTO))
                .retrieve()
                .bodyToMono(Void.class)
                .map(r ->true)
                .onErrorReturn(false);
    }
}
