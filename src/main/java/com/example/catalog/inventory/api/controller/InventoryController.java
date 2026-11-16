package com.example.catalog.inventory.api.controller;

import com.example.catalog.inventory.api.mapper.InventoryApiMapper;
import com.example.catalog.inventory.api.request.InventoryAdjustmentRequest;
import com.example.catalog.inventory.api.response.InventoryMovementResponse;
import com.example.catalog.inventory.api.response.InventoryResponse;
import com.example.catalog.inventory.application.service.InventoryService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {
    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping("/{productId}")
    public InventoryResponse get(@PathVariable long productId) {
        return InventoryApiMapper.toResponse(service.get(productId));
    }

    @PostMapping("/{productId}/adjust")
    public InventoryResponse adjust(
            @PathVariable long productId,
            @Valid @RequestBody InventoryAdjustmentRequest request,
            Principal principal) {
        String actor = principal == null ? "local-developer" : principal.getName();
        return InventoryApiMapper.toResponse(
                service.adjust(productId, request.quantity(), request.reference(), actor));
    }

    @GetMapping("/{productId}/history")
    public List<InventoryMovementResponse> history(@PathVariable long productId) {
        return service.history(productId).stream().map(InventoryApiMapper::toResponse).toList();
    }
}
