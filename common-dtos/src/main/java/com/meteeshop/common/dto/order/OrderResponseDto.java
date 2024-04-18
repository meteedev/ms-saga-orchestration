package com.meteeshop.common.dto.order;

import com.meteeshop.common.constant.OrderStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderResponseDto {
    private UUID orderId;
    private Integer userId;
    private Integer productId;
    private Double amount;
    private OrderStatus status;

}
