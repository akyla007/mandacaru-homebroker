package com.mandacarubroker.service.implementation;

import com.mandacarubroker.domain.dto.RequestStockDTO;
import com.mandacarubroker.domain.stock.Stock;
import com.mandacarubroker.repositories.StockRepository;
import com.mandacarubroker.service.StockService;
import com.mandacarubroker.service.exceptions.DataIntegratyViolationException;
import com.mandacarubroker.service.exceptions.StockNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.ConstraintViolation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@Service
public
class StockServiceImplem implements StockService {

    public static final Supplier<StockNotFoundException> NOT_FOUND_EXCEPTION_SUPPLIER =
            () -> new StockNotFoundException(("Stock not found"));
    //    @Autowired
    private final StockRepository stockRepository;


    public StockServiceImplem(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Stock getStockById(String id) {
        return stockRepository.findById(id).orElseThrow(
                NOT_FOUND_EXCEPTION_SUPPLIER);
    }


    public void deleteStock(String id) {
        Stock stock = stockRepository.findById(id).orElseThrow(
                NOT_FOUND_EXCEPTION_SUPPLIER);

        stockRepository.deleteById(stock.getId());
    }

    public Stock validateAndUpdateStock(String id, RequestStockDTO data) {
        validateRequestStockDTO(data);
        Optional<Stock> stockId = stockRepository.findById(id);
            if(stockId.isPresent()) {
                Stock stockBD = stockId.get();
                findComp(stockBD, data);


            }
            return stockId
                    .map(stock -> {
                        stock.setSymbol(data.symbol());
                        stock.setCompanyName(data.companyName());
                        double newPrice = stock.changePrice(data.price());
                        stock.setPrice(newPrice);

                        return stockRepository.save(stock);
                    }).orElseThrow(
                            NOT_FOUND_EXCEPTION_SUPPLIER);
    }

    private void findBySymbol(RequestStockDTO data) {
        Optional<Stock> stock = stockRepository.findBySymbol(data.symbol());
        if(stock.isPresent()) {
            throw new DataIntegratyViolationException("Stock already registered with this symbol");
        }
    }


    private void findComp(Stock stockBD,RequestStockDTO data) {
        if((!Objects.equals(data.symbol(), stockBD.getSymbol()))){
                findBySymbol(data);
            }
    }


    public static void validateRequestStockDTO(RequestStockDTO data) {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();

            Set<ConstraintViolation<RequestStockDTO>> violations = validator.validate(data);

            if (!violations.isEmpty()) {
                StringBuilder errorMessage = new StringBuilder("Validation failed. Details: ");

                for (ConstraintViolation<RequestStockDTO> violation : violations) {
                    errorMessage.append(String.format("[%s: %s], ", violation.getPropertyPath(), violation.getMessage()));
                }

                errorMessage.delete(errorMessage.length() - 2, errorMessage.length());

                    throw new ConstraintViolationException(errorMessage.toString(), violations);
            }
        }catch (ValidationException ve) {
            throw new ValidationException(ve.getMessage());
        }

    }


    public Stock validateAndCreateStock(RequestStockDTO data) {
        validateRequestStockDTO(data);
        findBySymbol(data);

        Stock newStock = new Stock(data);
        return stockRepository.save(newStock);
    }


}
