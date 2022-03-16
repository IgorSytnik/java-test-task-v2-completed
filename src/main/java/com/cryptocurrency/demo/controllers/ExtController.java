package com.cryptocurrency.demo.controllers;

import com.cryptocurrency.demo.services.ExternalOperationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
public class ExtController {

    private final ExternalOperationsService operationsService;

    /**
     * Calls {@link ExternalOperationsService#fetchAndSave(String, String, String)}
     * to fetch and save cryptocurrency prices.
     * <p>Cryptocurrencies fetched:
     * <ul>
     *     <li>BTC</li>
     *     <li>ETH</li>
     *     <li>XRP</li>
     * </ul>
     * <p>The price for all of them is set to <b>USD</b>.
     * Number of prices for each: 100.
     * Time span: 24 hours.
     *
     * @param lastHours time span for which prices should be fetched.
     * @param maxRespArrSize number of prices to be fetched.
     * @throws IOException if exception occurred in
     * {@link ExternalOperationsService#fetchAndSave(String, String, String)}.
     */
    public void fetchAndSave(long lastHours, long maxRespArrSize) throws IOException {
        String symbol1 = "BTC";
        String symbol2 = "USD";
        String body = "{\"lastHours\": " + lastHours + ", \"maxRespArrSize\": " + maxRespArrSize + "}";
        operationsService.fetchAndSave(symbol1, symbol2, body);
        symbol1 = "ETH";
        operationsService.fetchAndSave(symbol1, symbol2, body);
        symbol1 = "XRP";
        operationsService.fetchAndSave(symbol1, symbol2, body);
    }
}
