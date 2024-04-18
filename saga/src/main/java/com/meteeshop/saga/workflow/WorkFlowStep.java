package com.meteeshop.saga.workflow;

import reactor.core.publisher.Mono;

public interface WorkFlowStep {

    WorkflowStepStatus getStatus();
    Mono<Boolean> process();
    Mono<Boolean> compensate();


}
