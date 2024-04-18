package com.meteeshop.order.entitiy;

import com.meteeshop.common.constant.OrderStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderEntity {
    private UUID id;
    private Integer userId;
    private Integer productId;
    private Double price;
    private OrderStatus status;
}
