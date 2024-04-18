package com.meteeshop.order.controller;

import com.meteeshop.common.dto.order.OrderRequestDto;
import com.meteeshop.common.dto.order.OrderResponseDto;
import com.meteeshop.order.entitiy.OrderEntity;
import com.meteeshop.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("shop/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping()
    public Mono<OrderEntity> createOrder(@RequestBody Mono<OrderRequestDto> mono){
        return mono
                .flatMap(this.orderService::createOrder);
    }

    @GetMapping()
    public Flux<OrderResponseDto> getOrders(){
        return this.orderService.getAll();
    }


}
