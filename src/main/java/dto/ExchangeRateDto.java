package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Currency;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateDto {
    int id;
    Currency baseCurrency;
    Currency targetCurrency;
    BigDecimal rate;
}
