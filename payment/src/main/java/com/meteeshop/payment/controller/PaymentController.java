package com.meteeshop.payment.controller;

import com.meteeshop.common.dto.payment.PaymentRequestDto;
import com.meteeshop.common.dto.payment.PaymentResponseDto;
import com.meteeshop.payment.service.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("shop/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/debit")
    public PaymentResponseDto debit(@RequestBody PaymentRequestDto requestDTO){
        return this.paymentService.debit(requestDTO);
    }

    @PostMapping("/credit")
    public void credit(@RequestBody PaymentRequestDto requestDTO){
        this.paymentService.credit(requestDTO);
    }

}
