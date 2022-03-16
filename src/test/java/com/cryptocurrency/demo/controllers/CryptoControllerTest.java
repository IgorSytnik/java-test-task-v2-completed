package com.cryptocurrency.demo.controllers;

import com.cryptocurrency.demo.context.Config;
import com.cryptocurrency.demo.entities.CryptoCurrency;
import com.cryptocurrency.demo.entities.Price;
import com.cryptocurrency.demo.services.CurrencyService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataMongoTest
@ContextConfiguration(classes = {Config.class})
class CryptoControllerTest {

    String currencyName1 = "TEST1";
    String currencyName2 = "TEST2";
    String usd = "USD";
    String minPriceStr = "1.0";
    String medPriceStr = "2.0";
    String maxPriceStr = "3.0";
    long minTimestamp = 1L;
    long medTimestamp = 2L;
    long maxTimestamp = 3L;
    Price minPrice = new Price(minTimestamp, minPriceStr);
    Price medPrice = new Price(medTimestamp, medPriceStr);
    Price maxPrice = new Price(maxTimestamp, maxPriceStr);
    List<Price> priceList1 = List.of(minPrice, medPrice, maxPrice);
    List<Price> priceList2 = List.of(medPrice);
    List<CryptoCurrency> currencyList = List.of(
            new CryptoCurrency(currencyName1, usd, priceList1),
            new CryptoCurrency(currencyName2, usd, priceList2));
    CurrencyService currencyService = Mockito.mock(CurrencyService.class);
    CryptoController cryptoController = new CryptoController(currencyService);

    @BeforeAll
    static void beforeAll() {
    }

    @AfterAll
    static void afterAll() {
    }

    @Test
    void getMinPrice_comparingWithRightPrice_Equals() {
//        GIVEN
        Mockito.doReturn(minPrice).when(currencyService).getMinPriceByCurrName1(currencyName1);
        Mockito.doReturn(true).when(currencyService).checkCurrency(currencyName1);
//        WHEN
        ResponseEntity<Price> actual = cryptoController.getMinPrice(currencyName1);
//        THEN
        assertEquals(minPrice, actual.getBody());
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void getMinPrice_comparingWithWrongPrice_NotEquals() {
//        GIVEN
        Mockito.doReturn(medPrice).when(currencyService).getMinPriceByCurrName1(currencyName2);
        Mockito.doReturn(true).when(currencyService).checkCurrency(currencyName2);
//        WHEN
        ResponseEntity<Price> actual = cryptoController.getMinPrice(currencyName2);
//        THEN
        assertNotEquals(minPrice, actual.getBody());
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void getMinPrice_invalidCurrency_Throws() {
//        GIVEN
        Mockito.doReturn(false).when(currencyService).checkCurrency(currencyName2);
//        WHEN
        Executable supplier = () -> cryptoController.getMinPrice(currencyName2);
//        THEN
        assertThrows(ResponseStatusException.class, supplier);
    }

    @Test
    void getMaxPrice_comparingWithRightPrice_Equals() {
//        GIVEN
        Mockito.doReturn(maxPrice).when(currencyService).getMaxPriceByCurrName1(currencyName1);
        Mockito.doReturn(true).when(currencyService).checkCurrency(currencyName1);
//        WHEN
        ResponseEntity<Price> actual = cryptoController.getMaxPrice(currencyName1);
//        THEN
        assertEquals(maxPrice, actual.getBody());
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void getMaxPrice_comparingWithWrongPrice_NotEquals() {
//        GIVEN
        Mockito.doReturn(medPrice).when(currencyService).getMaxPriceByCurrName1(currencyName2);
        Mockito.doReturn(true).when(currencyService).checkCurrency(currencyName2);
//        WHEN
        ResponseEntity<Price> actual = cryptoController.getMaxPrice(currencyName2);
//        THEN
        assertNotEquals(maxPrice, actual.getBody());
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void getMaxPrice_invalidCurrency_Throws() {
//        GIVEN
        Mockito.doReturn(false).when(currencyService).checkCurrency(currencyName2);
//        WHEN
        Executable supplier = () -> cryptoController.getMaxPrice(currencyName2);
//        THEN
        assertThrows(ResponseStatusException.class, supplier);
    }

    @Test
    void getPages_page0Size3FromListOfSize3_Equals() {
//        GIVEN
        Mockito.doReturn(priceList1).when(currencyService).getPricesByCurrName1Sorted(currencyName1);
        Mockito.doReturn(true).when(currencyService).checkCurrency(currencyName1);
//        WHEN
        ResponseEntity<List<Price>> actual = cryptoController.getPages(currencyName1, 0, 3);
//        THEN
        assertEquals(priceList1, actual.getBody());
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void getPages_page1Size3FromListOfSize3_Equals() {
//        GIVEN
        Mockito.doReturn(priceList1).when(currencyService).getPricesByCurrName1Sorted(currencyName1);
        Mockito.doReturn(true).when(currencyService).checkCurrency(currencyName1);
//        WHEN
        ResponseEntity<List<Price>> actual = cryptoController.getPages(currencyName1, 1, 3);
//        THEN
        assertEquals(priceList1, actual.getBody());
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void getPages_pageMinus1Size3FromListOfSize3_Equals() {
//        GIVEN
        Mockito.doReturn(priceList1).when(currencyService).getPricesByCurrName1Sorted(currencyName1);
        Mockito.doReturn(true).when(currencyService).checkCurrency(currencyName1);
//        WHEN
        ResponseEntity<List<Price>> actual = cryptoController.getPages(currencyName1, -1, 3);
//        THEN
        assertEquals(priceList1, actual.getBody());
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void getPages_page0Size2FromListOfSize3_Equals() {
//        GIVEN
        Mockito.doReturn(priceList1).when(currencyService).getPricesByCurrName1Sorted(currencyName1);
        Mockito.doReturn(true).when(currencyService).checkCurrency(currencyName1);
//        WHEN
        ResponseEntity<List<Price>> actual = cryptoController.getPages(currencyName1, 0, 2);
//        THEN
        assertEquals(List.of(minPrice, medPrice), actual.getBody());
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void getPages_page1Size2FromListOfSize3_Equals() {
//        GIVEN
        Mockito.doReturn(priceList1).when(currencyService).getPricesByCurrName1Sorted(currencyName1);
        Mockito.doReturn(true).when(currencyService).checkCurrency(currencyName1);
//        WHEN
        ResponseEntity<List<Price>> actual = cryptoController.getPages(currencyName1, 1, 2);
//        THEN
        assertEquals(List.of(maxPrice), actual.getBody());
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void getPages_page2Size2FromListOfSize3_Equals() {
//        GIVEN
        Mockito.doReturn(priceList1).when(currencyService).getPricesByCurrName1Sorted(currencyName1);
        Mockito.doReturn(true).when(currencyService).checkCurrency(currencyName1);
//        WHEN
        ResponseEntity<List<Price>> actual = cryptoController.getPages(currencyName1, 2, 2);
//        THEN
        assertEquals(List.of(maxPrice), actual.getBody());
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void getPages_page0SizeMinus1FromListOfSize3_Equals() {
//        GIVEN
        Mockito.doReturn(priceList1).when(currencyService).getPricesByCurrName1Sorted(currencyName1);
        Mockito.doReturn(true).when(currencyService).checkCurrency(currencyName1);
//        WHEN
        ResponseEntity<List<Price>> actual = cryptoController.getPages(currencyName1, 0, -1);
//        THEN
        assertEquals(priceList1, actual.getBody());
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void getPages_invalidCurrency_Throws() {
//        GIVEN
        Mockito.doReturn(priceList1).when(currencyService).getPricesByCurrName1Sorted(currencyName1);
        Mockito.doReturn(false).when(currencyService).checkCurrency(currencyName1);
//        WHEN
        Executable supplier = () -> cryptoController.getPages(currencyName1, 0, 1);
//        THEN
        assertThrows(ResponseStatusException.class, supplier);
    }

    @Test
    void getCSVReport_notEmptyDB_Equals() throws IOException {
//        GIVEN
        String expected = String.format("%s/%s,%s,%s\n%s/%s,%s,%s",
                currencyName1, usd, minPrice.getPrice(), maxPrice.getPrice(),
                currencyName2, usd, medPrice.getPrice(), medPrice.getPrice());
        Mockito.doReturn(currencyList).when(currencyService).restoreAll();
        Mockito.doReturn(minPrice).when(currencyService).getMinPriceByCurrName1(currencyName1);
        Mockito.doReturn(maxPrice).when(currencyService).getMaxPriceByCurrName1(currencyName1);
        Mockito.doReturn(medPrice).when(currencyService).getMinPriceByCurrName1(currencyName2);
        Mockito.doReturn(medPrice).when(currencyService).getMaxPriceByCurrName1(currencyName2);
//        WHEN
        ResponseEntity<InputStreamResource> actual = cryptoController.getCSVReport();
//        THEN
        assertEquals(expected,
                new String(
                        Objects.requireNonNull(actual.getBody())
                                .getInputStream().readAllBytes(), StandardCharsets.UTF_8));
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void getCSVReport_emptyDB_Equals() throws IOException {
//        GIVEN
        String expected = "";
        Mockito.doReturn(List.of()).when(currencyService).restoreAll();
//        WHEN
        ResponseEntity<InputStreamResource> actual = cryptoController.getCSVReport();
//        THEN
        assertEquals(expected,
                new String(
                        Objects.requireNonNull(actual.getBody())
                                .getInputStream().readAllBytes(), StandardCharsets.UTF_8));
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }
}