package dao;

import exception.DBException;
import exception.DaoException;
import mapper.DataMapper;
import model.Currency;
import utils.ConnectionManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao {
    private final static CurrencyDao INSTANCE = new CurrencyDao();

    private final static String SAVE_SQL = """
            insert into currencydatabase.currencies(code,fullname,sign) values(?,?,?)""";

    private final static String GET_ALL_SQL = """
            select * from currencydatabase.currencies
            """;

    private final static String GET_BY_CODE_SQL = GET_ALL_SQL + """
            where code = ?
            """;

    public Optional<Currency> save(Currency currency) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
        ) {
        statement.setString(1,currency.getCode());
        statement.setString(2,currency.getName());
        statement.setString(3,currency.getSign());
        statement.executeUpdate();
        var keys=statement.getGeneratedKeys();
        if (keys.next()) {
            currency.setId(keys.getInt("id"));
        }
        return Optional.ofNullable(currency);
        } catch (SQLException e) {

            throw new DBException("Ошибка при сохранении валюты в базу данных: код валюты='" + currency.getCode() + "', имя валюты='" + currency.getName() + "', детальнее: " + e.getMessage());
        }
    }

    public List<Currency> findAll() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(GET_ALL_SQL)) {
            List<Currency> currencies = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next()) {
                currencies.add(
                        DataMapper.buildCurrency(result));

            }
            return currencies;
        } catch (SQLException e) {
            throw new DBException("Ошибка получения списка валют из базы данных: запрос SQL=" + GET_ALL_SQL + ", детализация: " + e.getMessage());
        }

    }


    public Optional<Currency>  findByCode(String code) {
        try ( var connection = ConnectionManager.get();
        var statement = connection.prepareStatement(GET_BY_CODE_SQL)
        ) {
            statement.setString(1, code);
            var result = statement.executeQuery();
            Currency currency = null;
            if (result.next()) {
                currency = DataMapper.buildCurrency(result);
            }
            return Optional.ofNullable(currency);
        } catch (SQLException e) {

            throw new DBException("Ошибка при поиске валюты по коду: " + code + ". Детали: " + e.getMessage());
        }
    }

    public static CurrencyDao getInstance() {
        return INSTANCE;
    }
    private CurrencyDao() {
    }
}
