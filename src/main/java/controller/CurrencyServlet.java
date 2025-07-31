package controller;

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
        String name = req.getParameter( "name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");
    CurrencyDto currency = currencyService.create(new CurrencyDto(name,code,sign));
        JsonUtil.sendJsonResponse(resp,HttpServletResponse.SC_CREATED,currency);
    }
}
