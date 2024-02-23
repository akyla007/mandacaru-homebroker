package com.mandacarubroker.repositories;


import com.mandacarubroker.domain.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository  extends JpaRepository<Stock,String> {
    Optional<Stock> findBySymbol(String symbol);
}
