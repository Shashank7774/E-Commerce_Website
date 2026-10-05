package com.ecommerce.backend.common;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ecommerce.backend.order.InsufficientStockException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==========================================
    // Validation Error
    // ==========================================

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<ErrorResponse>
    handleValidationException(
            MethodArgumentNotValidException exception
    ) {

        String message =
                exception
                        .getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error ->
                                error.getField()
                                        + ": "
                                        + error.getDefaultMessage()
                        )
                        .collect(
                                Collectors.joining(", ")
                        );

        ErrorResponse response =
                new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        message,
                        OffsetDateTime.now()
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    // ==========================================
    // Insufficient Stock
    // ==========================================

    @ExceptionHandler(
            InsufficientStockException.class
    )
    public ResponseEntity<StockErrorResponse>
    handleInsufficientStock(
            InsufficientStockException exception
    ) {

        StockErrorResponse response =
                new StockErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        "INSUFFICIENT_STOCK",
                        exception.getMessage(),
                        exception.getProductId(),
                        exception.getProductName(),
                        exception.getRequestedQuantity(),
                        exception.getAvailableQuantity(),
                        OffsetDateTime.now()
                );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    // ==========================================
    // Illegal Argument
    // ==========================================

    @ExceptionHandler(
            IllegalArgumentException.class
    )
    public ResponseEntity<ErrorResponse>
    handleIllegalArgumentException(
            IllegalArgumentException exception
    ) {

        ErrorResponse response =
                new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getMessage(),
                        OffsetDateTime.now()
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    // ==========================================
    // Optimistic Locking
    // ==========================================

    @ExceptionHandler(
            ObjectOptimisticLockingFailureException.class
    )
    public ResponseEntity<ErrorResponse>
    handleOptimisticLockingException(
            ObjectOptimisticLockingFailureException exception
    ) {

        ErrorResponse response =
                new ErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        "The product stock changed while your order was being processed. Please refresh your cart and try again.",
                        OffsetDateTime.now()
                );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    // ==========================================
    // Generic Error
    // ==========================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse>
    handleGenericException(
            Exception exception
    ) {

        ErrorResponse response =
                new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "An unexpected error occurred. Please try again.",
                        OffsetDateTime.now()
                );

        return ResponseEntity
                .status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
                .body(response);
    }

    // ==========================================
    // Standard Error Response
    // ==========================================

    public record ErrorResponse(
            int status,
            String message,
            OffsetDateTime timestamp
    ) {}

    // ==========================================
    // Stock Error Response
    // ==========================================

    public record StockErrorResponse(
            int status,
            String code,
            String message,
            Long productId,
            String productName,
            int requestedQuantity,
            int availableQuantity,
            OffsetDateTime timestamp
    ) {}
}