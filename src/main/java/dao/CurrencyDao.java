package dao;

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

    public Optional<Currency> create(Currency currency) {
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
            //  TODO EXCETOPTION SC_INTERNAL_SERVER_EROOR currency not save. something happpened with database or server
            throw new RuntimeException(e);
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
            // TODO    SC_INTERNAL_ERROR
            throw new DaoException(e);
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
            // TODO DB EXCEPTION SC _ INTERNAL_ ERROR
            throw new RuntimeException(e);
        }
    }

    public static CurrencyDao getInstance() {
        return INSTANCE;
    }
    private CurrencyDao() {
    }
}
