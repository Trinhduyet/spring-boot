package com.example.catalog.product.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.catalog.api.error.ApiExceptionHandler;
import com.example.catalog.product.application.input.CreateProductInput;
import com.example.catalog.product.application.result.ProductResult;
import com.example.catalog.product.application.service.ProductService;
import com.example.catalog.product.domain.exception.ProductNotFoundException;
import com.example.catalog.security.infrastructure.config.SecurityConfig;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;

@WebMvcTest(ProductController.class)
@Import({SecurityConfig.class, ApiExceptionHandler.class})
@ActiveProfiles("local")
class ProductControllerTest {
    @Autowired MockMvc mvc;
    @MockitoBean ProductService service;

    @Test
    void createReturns201AndLocation() throws Exception {
        var created =
                new ProductResult(
                        7L,
                        "BOOK-7",
                        "Java guide",
                        new BigDecimal("12.50"),
                        true,
                        0,
                        Instant.parse("2026-01-01T00:00:00Z"),
                        Instant.parse("2026-01-01T00:00:00Z"));
        when(service.create(any(CreateProductInput.class))).thenReturn(created);

        mvc.perform(
                        post("/api/v1/products")
                                .contentType("application/json")
                                .content(
                                        """
{"sku":"BOOK-7","name":"Java guide","price":12.50,"active":true}
"""))
                .andExpect(status().isCreated())
                .andExpect(
                        header().string(
                                        "Location",
                                        org.hamcrest.Matchers.endsWith("/api/v1/products/7")))
                .andExpect(jsonPath("$.sku").value("BOOK-7"));
    }

    @Test
    void invalidBodyReturns400ProblemDetail() throws Exception {
        mvc.perform(
                        post("/api/v1/products")
                                .contentType("application/json")
                                .content(
                                        """
                                        {"sku":" ","name":"","price":-1,"active":true}
                                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }

    @Test
    void invalidPageSizeReturns400() throws Exception {
        mvc.perform(get("/api/v1/products").param("page", "0").param("size", "1000"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void notFoundUsesConsistentProblemDetail() throws Exception {
        when(service.list(any(), any(), any())).thenReturn(Page.empty());
        when(service.get(999L)).thenThrow(new ProductNotFoundException(999L));

        mvc.perform(get("/api/v1/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Product 999 not found"));
    }
}
