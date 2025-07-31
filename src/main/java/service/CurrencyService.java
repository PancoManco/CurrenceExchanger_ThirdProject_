package service;

import dao.CurrencyDao;
import dto.CurrencyDto;
import mapper.DataMapper;
import model.Currency;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class CurrencyService {
    CurrencyDao dao = CurrencyDao.getInstance();
    public List<CurrencyDto> getAllCurrencies() {
        return dao.findAll().stream()
                .map(DataMapper::convertToDto)
                .collect(Collectors.toList());
    }
    public CurrencyDto getCurrencyByCode(String code) {
        return DataMapper.convertToDto(
                dao.findByCode(code).orElseThrow(() -> new NoSuchElementException("Валюта с указанным кодом не найдена"))
        );
    }

    public CurrencyDto create(CurrencyDto currencyDto) {
        Currency createdCurrency = dao.save(DataMapper.convertToCurrency(currencyDto)).get();  // Создаем новый объект валюты
        return DataMapper.convertToDto(createdCurrency);
    }
}
