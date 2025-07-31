package service;

import dao.CurrencyDao;
import dto.CurrencyDto;
import exception.NotFoundException;
import mapper.CurrencyMapper;
import mapper.DataMapper;
import model.Currency;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class CurrencyService {
    private final CurrencyDao dao = CurrencyDao.getInstance();
    private final CurrencyMapper mapper = CurrencyMapper.INSTANCE;

    public List<CurrencyDto> getAllCurrencies() {
        return dao.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public CurrencyDto getCurrencyByCode(String code) {
        return mapper.toDto(
                dao.findByCode(code).orElseThrow(() -> new NotFoundException("Валюта с кодом '" + code + "' не найдена"))
        );
    }

    public CurrencyDto create(CurrencyDto currencyDto) {
        Currency currencyToSave = mapper.toEntity(currencyDto);
        Currency savedCurrency = dao.save(currencyToSave)
                .orElseThrow(() -> new IllegalStateException("Ошибка при сохранении валюты")); // на случай, если Optional пустой
        return mapper.toDto(savedCurrency);
    }
}
