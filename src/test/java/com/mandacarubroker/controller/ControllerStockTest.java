package com.mandacarubroker.controller;

import com.mandacarubroker.domain.dto.RequestStockDTO;
import com.mandacarubroker.domain.stock.Stock;
import com.mandacarubroker.service.implementation.StockServiceImplem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
@SpringBootTest
class ControllerStockTest {
    private static final String ID  = "12345";
    private static final Integer INDEX   = 0;
    private static final String SYMBOL    = "AABB2";
    private static final String COMPANY = "Petrobras";
    private static final Double PRICE = 12.45;
    private Stock stock;
    private RequestStockDTO requestStockDTO;

    @Autowired
    private StockController controller;

    @MockBean
    private StockServiceImplem service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startStock();}

    @Test
    void whenGetStockByIdThenReturnSuccess() {
        when(service.getStockById(anyString())).thenReturn(stock);
        ResponseEntity<Stock> response = controller.getStockById(ID);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(Stock.class, response.getBody().getClass());

        assertEquals(ID, response.getBody().getId());
        assertEquals(SYMBOL, response.getBody().getSymbol());
        assertEquals(COMPANY, response.getBody().getCompanyName());
        assertEquals(PRICE, response.getBody().getPrice());
    }
    @Test
    void whenGetAllStocksThenReturnAListOfStock() {
        when(service.getAllStocks()).thenReturn(List.of(stock));
        ResponseEntity
                <List<Stock>> response = controller.getAllStocks();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(Stock.class, response.getBody().get(INDEX).getClass());
        assertEquals(ID, response.getBody().get(INDEX).getId());
        assertEquals(SYMBOL, response.getBody().get(INDEX).getSymbol());
        assertEquals(COMPANY, response.getBody().get(INDEX).getCompanyName());
        assertEquals(PRICE, response.getBody().get(INDEX).getPrice());
    }
    @Test
    void whenCreateThenReturnOk() {
        when(service.validateAndCreateStock(any())).thenReturn(stock);
        ResponseEntity<Stock> response = controller.createStock(requestStockDTO);
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void whenUpdateThenReturnSuccess() {
        when(service.validateAndUpdateStock(ID, requestStockDTO)).thenReturn(stock);
        ResponseEntity<Stock> response = controller.updateStock(ID, requestStockDTO);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(Stock.class, response.getBody().getClass());
        assertEquals(ID, response.getBody().getId());
        assertEquals(SYMBOL, response.getBody().getSymbol());
        assertEquals(COMPANY, response.getBody().getCompanyName());
        assertEquals(PRICE, response.getBody().getPrice());
    }
    @Test
    void whenDeleteThenReturnSuccess() {
        doNothing().when(service).deleteStock(anyString());
        ResponseEntity<String> response = controller.deleteStock(ID);
        assertNotNull(response);
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).deleteStock(anyString());
    }

    private void startStock() {
        requestStockDTO = new RequestStockDTO( SYMBOL, COMPANY,  PRICE);
        stock = new Stock(requestStockDTO);
        stock.setId(ID);
    }


}