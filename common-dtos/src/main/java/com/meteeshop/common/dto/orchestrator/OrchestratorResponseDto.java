package com.meteeshop.common.dto.orchestrator;


import com.meteeshop.common.constant.OrderStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class OrchestratorResponseDto {
    private Integer userId;
    private Integer productId;
    private UUID orderId;
    private Double amount;
    private OrderStatus status;
}
