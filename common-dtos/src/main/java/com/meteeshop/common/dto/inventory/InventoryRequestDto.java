package com.meteeshop.common.dto.inventory;

import lombok.Data;

import java.util.UUID;

@Data
public class InventoryRequestDto {
    private Integer userId;
    private Integer productId;
    private UUID orderId;
}
