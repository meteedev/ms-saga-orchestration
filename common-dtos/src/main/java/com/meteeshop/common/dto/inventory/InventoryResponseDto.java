package com.meteeshop.common.dto.inventory;

import com.meteeshop.common.constant.InventoryStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class InventoryResponseDto {
    private UUID orderId;
    private Integer userId;
    private Integer productId;
    private InventoryStatus status;
}
