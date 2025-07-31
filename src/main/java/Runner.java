import dao.CurrencyDao;
import dao.ExchangeRateDao;
import model.Currency;

import java.math.BigDecimal;

public class Runner {

    public static void main(String[] args) {

        var currencyDao = CurrencyDao.getInstance();
        Currency currency = new Currency(3,"SHI","SHITCOINT","%");
     //   currencyDao.create(currency);
      // System.out.println(currencyDao.findAll());

        var exchangeratedao = ExchangeRateDao.getInstance();
        System.out.println();
            System.out.println(exchangeratedao.getByPair("RUB","USD"));
        System.out.println();
        System.out.println(exchangeratedao.getAll());
    }
}
