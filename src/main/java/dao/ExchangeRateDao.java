package dao;

import exception.DBException;
import mapper.DataMapper;
import model.Currency;
import model.ExchangeRate;
import utils.ConnectionManager;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDao {

    private final static ExchangeRateDao INSTANCE = new ExchangeRateDao();


    private final static String SAVE_EXCHANGE_RATE_SQL = """
            Insert into currencydatabase.exchangerates(basecurrencyid, targetcurrencyid, rate)  
            values(?,?,?)
            """;

    private final static String GET_ALL_SQL = """
    select e.id, e.basecurrencyid, e.targetcurrencyid, e.rate,
           bc.code as base_currency_code, bc.fullname as base_fullname, bc.sign as base_sign,
           tc.code as target_currency_code, tc.fullname as target_fullname, tc.sign as target_sign
    from currencydatabase.exchangerates e
             left join currencydatabase.currencies bc on e.basecurrencyid = bc.id
             left join currencydatabase.currencies tc on e.targetcurrencyid = tc.id
""";

    private final static String UPDATE_EXCHANGE_RATE_SQL = """
            UPDATE currencydatabase.exchangerates
            SET rate = ?
            WHERE id = ?
            """;

    private final static String GET_BY_PAIR_SQL =GET_ALL_SQL + """
    where bc.code = ? and tc.code = ?
""";



    public Optional<ExchangeRate> save(Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SAVE_EXCHANGE_RATE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ExchangeRate exchangerate = new ExchangeRate();
            statement.setInt(1, baseCurrency.getId());
            statement.setInt(2, targetCurrency.getId());
            statement.setBigDecimal(3, rate);
            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();

            if (keys.next()) {
                exchangerate.setId(keys.getInt("id"));
            }
            return Optional.ofNullable(exchangerate);
        } catch (SQLException e) {
            throw new DBException("Ошибка при создании курса обмена валют. Проблемы с доступом к БД! ");
        }
    }

    public List<ExchangeRate> getAll() {
        try (var connection = ConnectionManager.get();
        var statement =connection.prepareStatement(GET_ALL_SQL)
        ) {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        var result = statement.executeQuery();
        while (result.next()) {
            exchangeRates.add(DataMapper.buildExchangeRate(result));
        }
        return exchangeRates;
        } catch (SQLException e) {
            throw new DBException("Ошибка при получение списка курса обмена валют.Проблемы с доступом к БД!");
        }

    }


    public boolean update(String basecurrencycode, String targetcurrencycode, BigDecimal rate) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(UPDATE_EXCHANGE_RATE_SQL)) {
          //  ExchangeRate exchangerate = getByPair(basecurrencycode,targetcurrencycode).get();
            Optional<ExchangeRate> optionalRate = getByPair(basecurrencycode,targetcurrencycode);
            if (optionalRate.isEmpty()) {
                throw new DBException("Курс обмена не найден для пары: " + basecurrencycode + " → " + targetcurrencycode);
            }
            ExchangeRate exchangerate = optionalRate.get();
            statement.setBigDecimal(1, rate);
            statement.setInt(2, exchangerate.getId());
            exchangerate.setRate(rate);
            return statement.executeUpdate() >0;
        } catch (SQLException e) {
           throw new DBException("Ошибка при обновление курса обмена валют.Проблемы с доступом к БД!");
        }

    }

    public Optional<ExchangeRate> getByPair(String basecurrencycode, String targetcurrencycode) {
        try (var connection = ConnectionManager.get();
        var statement = connection.prepareStatement(GET_BY_PAIR_SQL)
        ) {
            statement.setString(1, basecurrencycode);
            statement.setString(2, targetcurrencycode);
            var result = statement.executeQuery();
            ExchangeRate exchangeRate = null;
            if (result.next()) {
            exchangeRate = DataMapper.buildExchangeRate(result);
            }
        return Optional.ofNullable(exchangeRate);
        } catch (SQLException e) {
            throw new DBException("Ошибка при получение пары обмена курса валют. Проблемы с доступом к БД!");
        }
    }

    public static ExchangeRateDao getInstance() {
        return INSTANCE;
    }
    private ExchangeRateDao() {
    }
}
