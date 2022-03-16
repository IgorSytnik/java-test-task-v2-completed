package com.cryptocurrency.demo.controllers;

import com.cryptocurrency.demo.entities.Price;
import com.cryptocurrency.demo.exceptions.CurrencyException;
import com.cryptocurrency.demo.services.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cryptocurrencies")
public class CryptoController {

    private final CurrencyService currencyService;

    /**
     * <p>/cryptocurrencies/minprice?name=<i>name</i> GET endpoint.
     * <p>Get min price of the cryptocurrency with name <i>name</i>
     * <p>Throws {@link ResponseStatusException} if currency with name <i>name</i> does not exist.
     *
     * @param name name cryptocurrency name.
     * @return min {@link Price} for this cryptocurrency.
     * @see CurrencyService#getMinPriceByCurrName1(String)
     */
    @GetMapping("/minprice")
    public ResponseEntity<Price> getMinPrice(@RequestParam String name) {
        currencyCheck(name);
        return ResponseEntity.ok(currencyService.getMinPriceByCurrName1(name));
    }

    /**
     * <p>/cryptocurrencies/maxprice?name=<i>name</i> GET endpoint.
     * <p>Get max price of the cryptocurrency with name <i>name</i>
     * <p>Throws {@link ResponseStatusException} if currency with name <i>name</i> does not exist.
     *
     * @param name cryptocurrency name.
     * @return max {@link Price} for this cryptocurrency.
     * @see CurrencyService#getMaxPriceByCurrName1(String)
     */
    @GetMapping("/maxprice")
    public ResponseEntity<Price> getMaxPrice(@RequestParam String name) {
        currencyCheck(name);
        return ResponseEntity.ok(currencyService.getMaxPriceByCurrName1(name));
    }

    /**
     * /cryptocurrencies?name=<i>name</i>&page=<i>page</i>&size=<i>size</i> GET endpoint.
     * <p>Get page number <i>page</i> as if the page size was <i>size</i>.
     * <p>Throws {@link ResponseStatusException} if currency with name <i>name</i> does not exist.
     *
     * @param name cryptocurrency name.
     * @param page page number for the {@link Price} list. Default is <b>0</b>.<br>
     *             Changed to <b>0</b> if <b>{@code page<0}</b>.<br>
     *             Changed to <b>max value</b> if <b>{@code page>{number of possible pages}}</b>.
     * @param size page size for the {@link Price} list. Default is 10.<br>
     *             Changed to <b>10</b> if <b>{@code size<=0}</b>.
     * @return page number <i>page</i> as if the page size was <i>size</i>.
     * @see CurrencyService#getPricesByCurrName1Sorted(String)
     */
    @GetMapping
    public ResponseEntity<List<Price>> getPages(@RequestParam String name,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        currencyCheck(name);
        List<Price> prices = currencyService.getPricesByCurrName1Sorted(name);
        size = size <= 0 ? 10 : size;
        page = page < 0 ? 0 : page * size > prices.size() - 1 ? (prices.size() - 1) / size : page;
        int start = page * size;
        int end = start + size > prices.size() - 1 ? prices.size() : start + size;
        return ResponseEntity.ok(prices.subList(start, end));
    }

    /**
     * /cryptocurrencies/csv GET endpoint.
     * <p>Generates and returns csv report file containing:
     * <b>Cryptocurrency Name, Min Price, Max Price</b> for each currency.
     *
     * @return csv report file to be downloaded.
     */
    @GetMapping("/csv")
    public ResponseEntity<InputStreamResource> getCSVReport() {
        byte[] bytes = currencyService.restoreAll().stream()
                .map(cryptoCurrency ->
                        cryptoCurrency.getCurrName1() + '/' +
                                cryptoCurrency.getCurrName2() + ',' +
                                currencyService.getMinPriceByCurrName1(cryptoCurrency.getCurrName1()).getPrice() + ',' +
                                currencyService.getMaxPriceByCurrName1(cryptoCurrency.getCurrName1()).getPrice()
                ).collect(Collectors.joining("\n")).getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=csv_report.csv")
                // Content-Type
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                // Contet-Length
                .contentLength(bytes.length)
                .body(new InputStreamResource(new ByteArrayInputStream(bytes)));
    }

    /**
     * <p>Checks if database has currency with the given name.
     * Throws {@link ResponseStatusException} if it does not.
     *
     * @param name cryptocurrency name.
     * @throws ResponseStatusException if currency was not found.
     * @see CurrencyService#checkCurrency(String)
     */
    private void currencyCheck(String name) throws ResponseStatusException {
        if (!currencyService.checkCurrency(name))
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, String.format("Currency %s not found", name),
                    new CurrencyException("Currency not found"));
    }
}
