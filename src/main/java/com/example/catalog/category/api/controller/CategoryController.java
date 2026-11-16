package com.example.catalog.category.api.controller;

import com.example.catalog.category.api.mapper.CategoryApiMapper;
import com.example.catalog.category.api.request.CategoryRequest;
import com.example.catalog.category.api.response.CategoryResponse;
import com.example.catalog.category.application.service.CategoryService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategoryResponse> list() {
        return service.list().stream().map(CategoryApiMapper::toResponse).toList();
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse result =
                CategoryApiMapper.toResponse(service.create(CategoryApiMapper.toInput(request)));
        URI location =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(result.id())
                        .toUri();
        return ResponseEntity.created(location).body(result);
    }

    @PutMapping("/{id}")
    public CategoryResponse update(
            @PathVariable long id, @Valid @RequestBody CategoryRequest request) {
        return CategoryApiMapper.toResponse(service.update(id, CategoryApiMapper.toInput(request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/products")
    public List<Long> productIds(@PathVariable long id) {
        return service.productIds(id);
    }

    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<Void> assign(
            @PathVariable long categoryId, @PathVariable long productId) {
        service.assign(categoryId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<Void> unassign(
            @PathVariable long categoryId, @PathVariable long productId) {
        service.unassign(categoryId, productId);
        return ResponseEntity.noContent().build();
    }
}
