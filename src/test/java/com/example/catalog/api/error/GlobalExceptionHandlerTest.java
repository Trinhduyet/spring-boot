package com.example.catalog.api.error;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.catalog.product.domain.exception.ProductNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class GlobalExceptionHandlerTest {
    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void mapsMissingProductToNotFoundProblemDetail() {
        var response = handler.notFound(new ProductNotFoundException(17L));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDetail()).isEqualTo("Product 17 not found");
    }
}
