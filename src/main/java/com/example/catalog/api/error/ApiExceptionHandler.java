package com.example.catalog.api.error;

import com.example.catalog.category.domain.exception.CategoryConflictException;
import com.example.catalog.category.domain.exception.CategoryInUseException;
import com.example.catalog.category.domain.exception.CategoryNotFoundException;
import com.example.catalog.inventory.domain.exception.InsufficientStockException;
import com.example.catalog.inventory.domain.exception.InventoryNotFoundException;
import com.example.catalog.ordering.domain.exception.IdempotencyConflictException;
import com.example.catalog.ordering.domain.exception.InvalidOrderStateException;
import com.example.catalog.ordering.domain.exception.OrderNotFoundException;
import com.example.catalog.ordering.domain.exception.OrderProductNotFoundException;
import com.example.catalog.product.domain.exception.ProductNotFoundException;
import com.example.catalog.product.domain.exception.SkuConflictException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ProblemDetail> notFound(ProductNotFoundException ex) {
        return response(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({
        CategoryNotFoundException.class,
        InventoryNotFoundException.class,
        OrderNotFoundException.class,
        OrderProductNotFoundException.class
    })
    public ResponseEntity<ProblemDetail> resourceNotFound(RuntimeException ex) {
        return response(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({
        CategoryInUseException.class,
        InsufficientStockException.class,
        InvalidOrderStateException.class,
        IdempotencyConflictException.class
    })
    public ResponseEntity<ProblemDetail> businessConflict(RuntimeException ex) {
        return response(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler({
        SkuConflictException.class,
        CategoryConflictException.class,
        ObjectOptimisticLockingFailureException.class,
        DataIntegrityViolationException.class
    })
    public ResponseEntity<ProblemDetail> conflict(Exception ex) {
        return response(
                HttpStatus.CONFLICT,
                (ex instanceof SkuConflictException || ex instanceof CategoryConflictException)
                        ? ex.getMessage()
                        : "The operation conflicts with current data; reload the resource and"
                                + " retry");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> badRequest(IllegalArgumentException ex) {
        return response(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> invalid(MethodArgumentNotValidException ex) {
        String detail =
                ex.getBindingResult().getFieldErrors().stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .distinct()
                        .collect(Collectors.joining("; "));
        return response(HttpStatus.BAD_REQUEST, detail);
    }

    private ResponseEntity<ProblemDetail> response(HttpStatus status, String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(status.getReasonPhrase());
        return ResponseEntity.status(status).body(problem);
    }
}
