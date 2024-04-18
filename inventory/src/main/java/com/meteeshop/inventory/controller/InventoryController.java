package com.meteeshop.inventory.controller;

import com.meteeshop.common.dto.inventory.InventoryRequestDto;
import com.meteeshop.common.dto.inventory.InventoryResponseDto;
import com.meteeshop.inventory.service.InventoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/deduct")
    public InventoryResponseDto deduct(@RequestBody final InventoryRequestDto requestDTO){
        return this.inventoryService.deductInventory(requestDTO);
    }

    @PostMapping("/add")
    public void add(@RequestBody final InventoryRequestDto requestDTO){
        this.inventoryService.addInventory(requestDTO);
    }


}
