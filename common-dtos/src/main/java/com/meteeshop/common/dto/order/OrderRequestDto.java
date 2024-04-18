package com.meteeshop.common.dto.order;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderRequestDto {
    private Integer userId;
    private Integer productId;
    private UUID orderId;

}
