package com.meteeshop.order.service;

import com.meteeshop.common.dto.orchestration.OrchestratorResponseDto;
import com.meteeshop.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderEventService {

    private final OrderRepository orderRepository;

    public OrderEventService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Mono<Void> updateOrder(final OrchestratorResponseDto responseDto){
        return this.orderRepository.findById(responseDto.getOrderId())
                .doOnNext(p -> p.setStatus(responseDto.getStatus()))
                .flatMap(this.orderRepository::save)
                .then();
    }

}
