package com.meteeshop.order.config;

import com.meteeshop.common.dto.orchestration.OrchestratorRequestDto;
import com.meteeshop.common.dto.orchestration.OrchestratorResponseDto;
import com.meteeshop.order.service.OrderEventService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
public class OrderEventHandlerConfig {

    private final Flux<OrchestratorRequestDto> flux;
    private final OrderEventService orderEventService;

    public OrderEventHandlerConfig(Flux<OrchestratorRequestDto> flux, OrderEventService orderEventService) {
        this.flux = flux;
        this.orderEventService = orderEventService;
    }

    @Bean
    public Supplier<Flux<OrchestratorRequestDto>> supplier(){
        return () -> flux;
    };

    @Bean
    public Consumer<Flux<OrchestratorResponseDto>> consumer(){
        return f -> f
                .doOnNext(c -> System.out.println("Consuming :: " + c))
                //.flatMap(responseDto -> this.orderEventService.updateOrder(responseDto))
                .flatMap(this.orderEventService::updateOrder)
                .subscribe();
    }

}
