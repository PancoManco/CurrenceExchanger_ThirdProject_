package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CurrencyDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Currency;
import service.CurrencyService;
import utils.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/currencies")
public class CurrencyServlet extends HttpServlet {
    private CurrencyService currencyService = new CurrencyService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<CurrencyDto> currencies = currencyService.getAllCurrencies();
        JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, currencies);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrencyDto currencyDto = new ObjectMapper().readValue(req.getReader(), CurrencyDto.class);
        CurrencyDto createdCurrency = currencyService.create(currencyDto);
        JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_CREATED, createdCurrency);

        /*
        String name = req.getParameter( "name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");

        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setCode(code);
        currencyDto.setName(name);
        currencyDto.setSign(sign);
        CurrencyDto currency = currencyService.create(currencyDto);
        JsonUtil.sendJsonResponse(resp,HttpServletResponse.SC_CREATED,currency); */

    }
}
