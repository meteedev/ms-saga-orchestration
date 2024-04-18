package com.meteeshop.order.service;

import com.meteeshop.common.constant.OrderStatus;
import com.meteeshop.common.constant.ProductConstant;
import com.meteeshop.common.dto.orchestration.OrchestratorRequestDto;
import com.meteeshop.common.dto.order.OrderRequestDto;
import com.meteeshop.common.dto.order.OrderResponseDto;
import com.meteeshop.order.entitiy.OrderEntity;
import com.meteeshop.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final Sinks.Many<OrchestratorRequestDto> sink;

    public OrderService(OrderRepository orderRepository,Sinks.Many<OrchestratorRequestDto> sink) {
        this.orderRepository = orderRepository;
        this.sink = sink;
    }

    public Mono<OrderEntity> createOrder(OrderRequestDto orderRequestDto){
        return this.orderRepository.save(this.dtoToEntity(orderRequestDto))
                .doOnNext(e -> orderRequestDto.setOrderId(e.getId()))
                .doOnNext(e -> this.emitEvent(orderRequestDto));
    }

    public Flux<OrderResponseDto> getAll() {
        return this.orderRepository.findAll()
                .map(this::entityToDto);
    }

    private void emitEvent(OrderRequestDto orderRequestDTO){
        this.sink.tryEmitNext(this.getOrchestratorRequestDTO(orderRequestDTO));
    }

    private OrderEntity dtoToEntity(final OrderRequestDto dto){
        OrderEntity purchaseOrder = new OrderEntity();
        purchaseOrder.setId(dto.getOrderId());
        purchaseOrder.setProductId(dto.getProductId());
        purchaseOrder.setUserId(dto.getUserId());
        purchaseOrder.setStatus(OrderStatus.ORDER_CREATED);
        purchaseOrder.setPrice(ProductConstant.PRODUCT_PRICE.get(purchaseOrder.getProductId()));
        return purchaseOrder;
    }

    private OrderResponseDto entityToDto(final OrderEntity purchaseOrder){
        OrderResponseDto dto = new OrderResponseDto();
        dto.setOrderId(purchaseOrder.getId());
        dto.setProductId(purchaseOrder.getProductId());
        dto.setUserId(purchaseOrder.getUserId());
        dto.setStatus(purchaseOrder.getStatus());
        dto.setAmount(purchaseOrder.getPrice());
        return dto;
    }

    public OrchestratorRequestDto getOrchestratorRequestDTO(OrderRequestDto orderRequestDTO){
        OrchestratorRequestDto requestDTO = new OrchestratorRequestDto();
        requestDTO.setUserId(orderRequestDTO.getUserId());
        requestDTO.setAmount(ProductConstant.PRODUCT_PRICE.get(orderRequestDTO.getProductId()));
        requestDTO.setOrderId(orderRequestDTO.getOrderId());
        requestDTO.setProductId(orderRequestDTO.getProductId());
        return requestDTO;
    }

}
