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

    public static ExchangeRate buildExchangeRate(ResultSet rs) throws SQLException {
        Currency baseCurrency = new Currency(
                rs.getInt("basecurrencyid"),
                rs.getString("base_currency_code"),
                rs.getString("base_fullname"),
                rs.getString("base_sign")
        );
        Currency targetCurrency = new Currency(
                rs.getInt("targetcurrencyid"),
                rs.getString("target_currency_code"),
                rs.getString("target_fullname"),
                rs.getString("target_sign")
        );

        return new ExchangeRate(
                rs.getInt("id"),
                baseCurrency,
                targetCurrency,
                rs.getBigDecimal("rate")
        );
    }

    public static CurrencyDto toDto(Currency currency) {
        if (currency == null) return null;
        return new CurrencyDto(
                currency.getId(),
                currency.getCode(),
                currency.getName(),
                currency.getSign()
        );
    }

    public static Currency toModel(CurrencyDto dto) {
        if (dto == null) return null;
        return new Currency(
                dto.getId(),
                dto.getCode(),
                dto.getName(),
                dto.getSign()
        );
    }

    public static ExchangeRateDto toDto(ExchangeRate exchangeRate) {
        if (exchangeRate == null) return null;

        return new ExchangeRateDto(
                exchangeRate.getId(),
                toDto(exchangeRate.getBaseCurrency()),
                toDto(exchangeRate.getTargetCurrency()),
                exchangeRate.getRate()
        );
    }

    public static ExchangeRate toModel(ExchangeRateDto dto) {
        if (dto == null) return null;

        return new ExchangeRate(
                dto.getId(),
                toModel(dto.getBaseCurrency()),
                toModel(dto.getTargetCurrency()),
                dto.getRate()
        );
    }
}
