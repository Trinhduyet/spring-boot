package com.example.catalog.ordering.api.controller;

import com.example.catalog.ordering.api.mapper.OrderApiMapper;
import com.example.catalog.ordering.api.request.CreateOrderRequest;
import com.example.catalog.ordering.api.response.OrderResponse;
import com.example.catalog.ordering.application.service.OrderService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(
            @RequestHeader(value = "Idempotency-Key", required = false) String key,
            @Valid @RequestBody CreateOrderRequest request,
            Principal principal) {
        OrderResponse result =
                OrderApiMapper.toResponse(
                        service.create(actor(principal), key, OrderApiMapper.toInput(request)));
        URI location =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(result.id())
                        .toUri();
        return ResponseEntity.created(location).body(result);
    }

    @GetMapping("/{id}")
    public OrderResponse get(@PathVariable long id, Principal principal) {
        return OrderApiMapper.toResponse(service.get(id, actor(principal)));
    }

    @PostMapping("/{id}/cancel")
    public OrderResponse cancel(@PathVariable long id, Principal principal) {
        return OrderApiMapper.toResponse(service.cancel(id, actor(principal)));
    }

    private String actor(Principal principal) {
        return principal == null ? "local-developer" : principal.getName();
    }
}
