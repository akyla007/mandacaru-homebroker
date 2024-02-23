package com.mandacarubroker.controller.exceptions;


import com.mandacarubroker.domain.dto.RequestStockDTO;
import com.mandacarubroker.service.exceptions.DataIntegratyViolationException;
import com.mandacarubroker.service.exceptions.StockNotFoundException;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ControllerExceptionHandlerTest {

    private static final String STOCK_NOT_FOUND = "Stock not found";
    private static final String STOCK_ALREADY_REGISTERED = "Stock already registered with this symbol";
    @InjectMocks
    private ControllerExceptionHandler exceptionHandler;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);}
    @Test
    void whenStockNotFoundExceptionThenReturnAResponseEntity() {
        ResponseEntity<StandardError> response = exceptionHandler
                .handleObjectNotFoundException(
                        new StockNotFoundException(STOCK_NOT_FOUND));
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(StandardError.class, response.getBody().getClass());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().getStatus());
        assertEquals(LocalDateTime.class,response.getBody().getTimestamp().getClass());
    }
    @Test
    void whenDataIntegrityViolationExceptionThenReturnAResponseEntity() {
        ResponseEntity<StandardError> response = exceptionHandler
                .dataIntegrityViolationException(
                        new DataIntegratyViolationException(STOCK_ALREADY_REGISTERED));
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(StandardError.class, response.getBody().getClass());
        assertEquals(STOCK_ALREADY_REGISTERED, response.getBody().getError());
        assertEquals(HttpStatus.CONFLICT, response.getBody().getStatus());
    }
}