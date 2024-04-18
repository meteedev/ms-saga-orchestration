package com.meteeshop.common.dto.payment;

import com.meteeshop.common.constant.PaymentStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class PaymentRequestDto {
    private Integer userId;
    private UUID orderId;
    private Double amount;

}
