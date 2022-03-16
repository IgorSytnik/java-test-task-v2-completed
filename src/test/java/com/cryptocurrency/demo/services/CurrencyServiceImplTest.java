package com.cryptocurrency.demo.services;

import com.cryptocurrency.demo.entities.CryptoCurrency;
import com.cryptocurrency.demo.entities.Price;
import com.cryptocurrency.demo.exceptions.PriceException;
import com.cryptocurrency.demo.repositories.CurrencyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.data.domain.Example;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyServiceImplTest {

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
    List<Price> priceList1 = List.of(medPrice, maxPrice, minPrice);
    List<Price> priceList2 = List.of(medPrice);
    CryptoCurrency currency1 = new CryptoCurrency(currencyName1, usd, priceList1);
    CryptoCurrency currency1Empty = new CryptoCurrency(currencyName1, usd, List.of());
    List<CryptoCurrency> currencyList = List.of(
            currency1,
            new CryptoCurrency(currencyName2, usd, priceList2));
    CurrencyRepository currencyRepository = Mockito.mock(CurrencyRepository.class);
    CurrencyService currencyService = new CurrencyServiceImpl(currencyRepository);

    @Test
    void saveList_saveNotEmptyList_Equals() {
//        GIVEN
        Mockito.doReturn(currencyList).when(currencyRepository).saveAll(currencyList);
//        WHEN
        List<CryptoCurrency> actual = currencyService.saveList(currencyList);
//        THEN
        assertEquals(currencyList, actual);
    }

    @Test
    void restoreAll_getNotEmptyList_Equals() {
//        GIVEN
        Mockito.doReturn(currencyList).when(currencyRepository).findAll();
//        WHEN
        List<CryptoCurrency> actual = currencyService.restoreAll();
//        THEN
        assertEquals(currencyList, actual);
    }

    @Test
    void getMaxPriceByCurrName1_notEmptyPrices_Equals() {
//        GIVEN
        Mockito.doReturn(currency1).when(currencyRepository).findByCurrName1(currencyName1);
//        WHEN
        Price actual = currencyService.getMaxPriceByCurrName1(currencyName1);
//        THEN
        assertEquals(maxPrice, actual);
    }

    @Test
    void getMaxPriceByCurrName1_emptyPrices_Throws() {
//        GIVEN
        Mockito.doReturn(currency1Empty).when(currencyRepository).findByCurrName1(currencyName1);
//        WHEN
         Executable executable = () -> currencyService.getMaxPriceByCurrName1(currencyName1);
//        THEN
        assertThrows(PriceException.class, executable);
    }

    @Test
    void getMinPriceByCurrName1_notEmptyPrices_Equals() {
//        GIVEN
        Mockito.doReturn(currency1).when(currencyRepository).findByCurrName1(currencyName1);
//        WHEN
        Price actual = currencyService.getMinPriceByCurrName1(currencyName1);
//        THEN
        assertEquals(minPrice, actual);
    }

    @Test
    void getMinPriceByCurrName1_emptyPrices_Throws() {
//        GIVEN
        Mockito.doReturn(currency1Empty).when(currencyRepository).findByCurrName1(currencyName1);
//        WHEN
        Executable executable = () -> currencyService.getMinPriceByCurrName1(currencyName1);
//        THEN
        assertThrows(PriceException.class, executable);
    }

    @Test
    void getPricesByCurrName1Sorted_notEmptyPrices_Equals() {
//        GIVEN
        Mockito.doReturn(currency1).when(currencyRepository).findByCurrName1(currencyName1);
//        WHEN
        List<Price> actual = currencyService.getPricesByCurrName1Sorted(currencyName1);
//        THEN
        assertEquals(
                priceList1.stream()
                        .sorted(Comparator.comparing(
                                price -> Double.valueOf(price.getPrice())))
                        .collect(Collectors.toList()),
                actual);
    }

    @Test
    void getPricesByCurrName1Sorted_emptyPrices_Equals() {
//        GIVEN
        Mockito.doReturn(currency1Empty).when(currencyRepository).findByCurrName1(currencyName1);
//        WHEN
        List<Price> actual = currencyService.getPricesByCurrName1Sorted(currencyName1);
//        THEN
        assertEquals(List.of(), actual);
    }

    @Test
    void checkCurrency_exists_True() {
//        GIVEN
        Mockito.doReturn(true).when(currencyRepository)
                .exists(Example.of(new CryptoCurrency(currencyName1, null)));
//        WHEN
        boolean actual = currencyService.checkCurrency(currencyName1);
//        THEN
        assertTrue(actual);
    }

    @Test
    void checkCurrency_doesNotExist_False() {
//        GIVEN
        Mockito.doReturn(false).when(currencyRepository)
                .exists(Example.of(new CryptoCurrency(currencyName1, null)));
//        WHEN
        boolean actual = currencyService.checkCurrency(currencyName1);
//        THEN
        assertFalse(actual);
    }

}