package com.mandacarubroker.services.impl;


import com.mandacarubroker.domain.stock.Stock;
import com.mandacarubroker.domain.dto.RequestStockDTO;
import com.mandacarubroker.repositories.StockRepository;
import com.mandacarubroker.service.implementation.StockServiceImplem;
import com.mandacarubroker.service.exceptions.DataIntegratyViolationException;
import com.mandacarubroker.service.exceptions.StockNotFoundException;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;


@RunWith(SpringRunner.class)
@SpringBootTest
class StockServiceImplemTest {

    private static final String ID  = "12345";
    private static final Integer INDEX   = 0;
    private static final String SYMBOL    = "AABB2";
    private static final String COMPANY = "Petrobras";
    private static final Double PRICE = 12.45;

    private static final String STOCK_NOT_FOUND = "Stock not found";

    private static final String STOCK_ALREADY_REGISTERED = "Stock already registered with this symbol";

    private static final List<String> VALIDATION = Arrays.asList(
        "Validation failed. Details: [companyName: Company name cannot be blank]",
        "Validation failed. Details: [symbol: Symbol must be 4 letters followed by 1 number]",
        "Validation failed. Details: [symbol: Symbol must be 4 letters followed by 1 number], [companyName: Company name cannot be blank]",
        "Validation failed. Details: [companyName: Company name cannot be blank], [symbol: Symbol must be 4 letters followed by 1 number]");


    @Autowired
    private StockServiceImplem service;
    @MockBean
    private StockRepository repository;
    private Stock stock;
    private RequestStockDTO requestStockDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startStock();
    }

    @Test
    void whenFindByIdThenReturnAnStockInstance() {
        when(repository.findById(anyString())).thenReturn(Optional.of(stock));
        Stock response = service.getStockById(ID);
        assertNotNull(response);
        assertEquals(Stock.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(SYMBOL, response.getSymbol());
        assertEquals(COMPANY, response.getCompanyName());
        assertEquals(PRICE, response.getPrice());
    }

    @Test
    void whenFindByIdThenReturnAnStockNotFoundException() {
        when(repository.findById(anyString()))
                .thenThrow(new StockNotFoundException(STOCK_NOT_FOUND));
        try{
            service.getStockById(ID);
        } catch (Exception ex) {
            assertEquals(StockNotFoundException.class, ex.getClass());
            assertEquals(STOCK_NOT_FOUND, ex.getMessage());
        }
    }

    @Test
    void whenFindAllThenReturnAnListOfUsers() {
        when(repository.findAll()).thenReturn(List.of(stock));
        List<Stock> response = service.getAllStocks();
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(Stock.class, response.get(INDEX).getClass());

        assertEquals(ID, response.get(INDEX).getId());
        assertEquals(SYMBOL, response.get(INDEX).getSymbol());
        assertEquals(COMPANY, response.get(INDEX).getCompanyName());
        assertEquals(PRICE, response.get(INDEX).getPrice());
    }

    @Test
    void whenCreateThenReturnSuccess() {
        when(repository.save(any())).thenReturn(stock);

        Stock response = service.validateAndCreateStock(requestStockDTO);

        assertNotNull(response);
        assertEquals(Stock.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(SYMBOL, response.getSymbol());
        assertEquals(COMPANY, response.getCompanyName());
        assertEquals(PRICE, response.getPrice());
    }

    @Test
    void whenCreateThenReturnAnValidationConstraintViolationException() {
        try{
            service.validateAndCreateStock(new RequestStockDTO("a2", "", 0D));
        } catch (Exception ex) {
            assertEquals(ValidationException.class, ex.getClass());
            assertTrue(VALIDATION.contains(ex.getMessage()));
        }}
    @Test
    void whenCreateThenReturnAnDataIntegrityViolationException() {
        when(repository.findBySymbol(anyString())).thenReturn(Optional.of(stock));
        try{
            service.validateAndCreateStock(requestStockDTO);
        } catch (Exception ex) {
            assertEquals(DataIntegratyViolationException.class, ex.getClass());
            assertEquals(STOCK_ALREADY_REGISTERED, ex.getMessage());
        }}
    @Test
    void whenUpdateThenReturnSuccess() {
        when(repository.findById(ID)).thenReturn(Optional.of(stock));
        when(repository.save(any())).thenReturn(stock);
        Stock response = service.validateAndUpdateStock(ID, requestStockDTO);
        assertNotNull(response);
        assertEquals(Stock.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(SYMBOL, response.getSymbol());
        assertEquals(COMPANY, response.getCompanyName());
        assertEquals(PRICE, response.getPrice());
    }

    @Test
    void whenUpdateThenReturnAnValidationConstraintViolationException() {
        when(repository.findById(anyString())).thenReturn(Optional.of(stock));
        try{
            service.validateAndUpdateStock(ID,new RequestStockDTO("a2", "", 0D));
        } catch (Exception ex) {
            assertEquals(ValidationException.class, ex.getClass());
            assertTrue(VALIDATION.contains(ex.getMessage()));
        }
    }
    @Test
    void whenUpdateThenReturnAnDataIntegrityViolationException() {
        when(repository.findById(anyString())).thenReturn(Optional.of(stock));
        when(repository.findBySymbol(anyString())).thenReturn(Optional.of(stock));
        stock.setSymbol("BBAA1");
        try{
            service.validateAndUpdateStock(ID,requestStockDTO);
        } catch (Exception ex) {
            assertEquals(DataIntegratyViolationException.class, ex.getClass());
            assertEquals(STOCK_ALREADY_REGISTERED, ex.getMessage());
        }}
    @Test
    void whenUpdateThenReturnAnStockNotFoundException() {
        when(repository.findById(anyString())).thenThrow(new StockNotFoundException(STOCK_NOT_FOUND));
        try{
            service.validateAndUpdateStock(ID,requestStockDTO);
        } catch (Exception ex) {
            assertEquals(StockNotFoundException.class, ex.getClass());
            assertEquals(STOCK_NOT_FOUND, ex.getMessage());
        }}


    @Test
    void deleteWithSuccess() {
        when(repository.findById(anyString())).thenReturn(Optional.of(stock));
        doNothing().when(repository).deleteById(anyString());
        service.deleteStock(ID);
        verify(repository, times(1)).deleteById(anyString());
    }

    @Test
    void whenDeleteThenReturnObjectNotFoundException() {
        when(repository.findById(anyString()))
                .thenThrow(new StockNotFoundException(STOCK_NOT_FOUND));
        try {
            service.deleteStock(ID);
        } catch (Exception ex) {
            assertEquals(StockNotFoundException.class, ex.getClass());
            assertEquals(STOCK_NOT_FOUND, ex.getMessage());
        }
    }


    private void startStock() {
        requestStockDTO = new RequestStockDTO(SYMBOL, COMPANY,  PRICE);
        stock = new Stock(requestStockDTO);
        stock.setId(ID);
    }
}