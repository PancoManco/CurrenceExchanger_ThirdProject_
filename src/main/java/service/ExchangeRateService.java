package service;

import dao.ExchangeRateDao;
import dto.ExchangeRateDto;
import exception.NotFoundException;
import mapper.ExchangeRateMapper;
import model.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ExchangeRateService {

    private final ExchangeRateDao dao = ExchangeRateDao.getInstance();
    private final ExchangeRateMapper mapper = ExchangeRateMapper.INSTANCE;

    public List<ExchangeRateDto> getAllExchangeRates() {
        return dao.getAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public ExchangeRateDto getByCurrencyPair(String baseCode, String targetCode) {
        return dao.getByPair(baseCode, targetCode)
                .map(mapper::toDto)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Курс обмена для пары %s -> %s не найден", baseCode, targetCode)));

    }

    public ExchangeRateDto createExchangeRate(ExchangeRateDto dto) {
        ExchangeRate exchangeRateToSave = mapper.toEntity(dto);
        // Важно: Currency у ExchangeRate должен иметь id, иначе сохранение не пройдет!
        ExchangeRate savedRate = dao.save(
                exchangeRateToSave.getBaseCurrency(),
                exchangeRateToSave.getTargetCurrency(),
                exchangeRateToSave.getRate()
        ).orElseThrow(() -> new IllegalStateException("Не удалось сохранить курс обмена"));
        return mapper.toDto(savedRate);
    }

    public boolean updateExchangeRate(String baseCode, String targetCode, BigDecimal newRate) {
        return dao.update(baseCode, targetCode, newRate);
    }
}
