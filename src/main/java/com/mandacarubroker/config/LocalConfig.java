package com.mandacarubroker.config;
import com.mandacarubroker.domain.dto.RequestStockDTO;
import com.mandacarubroker.domain.stock.Stock;
import com.mandacarubroker.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile("local")
public class LocalConfig {

    @Autowired
    private StockRepository repository;

    @Bean
    public void startDB() {
        RequestStockDTO rdto = new RequestStockDTO("AABB2", "Petrobras", 111D);
        RequestStockDTO rdto2 = new RequestStockDTO("BBAA5", "Apple", 723.34);
        Stock u1 = new Stock(rdto);
        Stock u2 = new Stock(rdto2);
        u1.setId("12345");
        u2.setId("5678");







        repository.saveAll(List.of(u1, u2));
    }
}
