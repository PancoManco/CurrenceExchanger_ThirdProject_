package mapper;

import dto.CurrencyDto;
import dto.ExchangeRateDto;
import model.Currency;
import model.ExchangeRate;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class DataMapper {

    private DataMapper() {}

    public static Currency buildCurrency(ResultSet resultSet) throws  SQLException {
        return new Currency(resultSet.getInt("id"),
                resultSet.getString("code"),
                resultSet.getString("fullname"),
                resultSet.getString("sign"));
    }

    public static ExchangeRate buildExchangeRate(ResultSet resultSet) throws SQLException {
        Currency baseCurrency = new Currency(
                resultSet.getInt("basecurrencyid"),
                resultSet.getString("base_currency_code"),
                resultSet.getString("base_fullname"), // ??
                resultSet.getString("base_sign")); // ??
        Currency targetCurrency = new Currency(
                resultSet.getInt("targetcurrencyid"),
                resultSet.getString("target_currency_code"),
                resultSet.getString("target_fullname"), // ??
                resultSet.getString("target_sign")); // ??
        return new ExchangeRate(
                resultSet.getInt("id"),
                baseCurrency,
                targetCurrency,
                resultSet.getBigDecimal("rate"));
    }

    public static CurrencyDto convertToDto(Currency currency) {
        if(currency == null){
            return null;
        }
        CurrencyDto dto = new CurrencyDto();
      //  dto.setId(currency.getId());
        dto.setCode(currency.getCode());
        dto.setName(currency.getName());
        dto.setSign(currency.getSign());
        return dto;
    }

    public static Currency convertToCurrency(CurrencyDto dto) {
        Currency currency = new Currency();
     //   currency.setId(dto.getId());
        currency.setCode(dto.getCode());
        currency.setName(dto.getName());
        currency.setSign(dto.getSign());
        return currency;
    }

    public static ExchangeRateDto convertToDto(ExchangeRate exchangeRate) {
        ExchangeRateDto dto = new ExchangeRateDto();
        dto.setId(exchangeRate.getId());
        dto.setBaseCurrency(exchangeRate.getBaseCurrency());
        dto.setTargetCurrency(exchangeRate.getTargetCurrency());
        dto.setRate(exchangeRate.getRate());
        return dto;
    }

    public static ExchangeRate convertToExchangeRate(ExchangeRateDto dto) {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setId(dto.getId());
        exchangeRate.setBaseCurrency(dto.getBaseCurrency());
        exchangeRate.setTargetCurrency(dto.getTargetCurrency());
        exchangeRate.setRate(dto.getRate());
        return exchangeRate;
    }
}
