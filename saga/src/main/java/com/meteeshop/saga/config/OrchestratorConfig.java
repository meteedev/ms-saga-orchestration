package com.meteeshop.saga.config;

import com.meteeshop.common.dto.orchestrator.OrchestratorRequestDto;
import com.meteeshop.common.dto.orchestrator.OrchestratorResponseDto;
import com.meteeshop.saga.service.OrchestratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
public class OrchestratorConfig {

    private OrchestratorService orchestratorService;

    public OrchestratorConfig(OrchestratorService orchestratorService) {
        this.orchestratorService = orchestratorService;
    }

    @Bean
    public Function<Flux<OrchestratorRequestDto>, Flux<OrchestratorResponseDto>> processor(){
        return flux -> flux
                .flatMap(dto -> this.orchestratorService.orderProduct(dto))
                .doOnNext(dto -> System.out.println("Status : " + dto.getStatus()));
    }

}
