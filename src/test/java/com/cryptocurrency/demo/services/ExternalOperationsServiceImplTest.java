package com.cryptocurrency.demo.services;

import com.cryptocurrency.demo.context.Config;
import com.cryptocurrency.demo.entities.CryptoCurrency;
import com.cryptocurrency.demo.repositories.CurrencyRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@DataMongoTest
@ContextConfiguration(classes = {Config.class})
class ExternalOperationsServiceImplTest {

    String symbol1 = "BTC";
    String symbol2 = "USD";
    String body = "{\"lastHours\": 24, \"maxRespArrSize\": 100}";
    CryptoCurrency currency = new CryptoCurrency(symbol1, symbol2);
    CurrencyRepository currencyRepository = Mockito.mock(CurrencyRepository.class);
    ExternalOperationsService operationsService = new ExternalOperationsServiceImpl(currencyRepository);

    @BeforeAll
    static void setUp() {
    }

    @Test
    void fetchAndSave_returnsSameCrypto_Equals() throws IOException {
//        GIVEN
        Mockito.when(currencyRepository.save(any(CryptoCurrency.class))).thenReturn(currency);
        Mockito.doReturn(Optional.of(currency)).when(currencyRepository).findOne(Example.of(currency));
//        WHEN
        CryptoCurrency actual = operationsService.fetchAndSave(symbol1, symbol2, body);
//        THEN
        assertEquals(currency, actual);
    }

    @Test
    void fetchAndSave_pricesListIsNotEmpty_False() throws IOException {
//        GIVEN
        Mockito.when(currencyRepository.save(any(CryptoCurrency.class))).thenReturn(currency);
        Mockito.doReturn(Optional.empty()).when(currencyRepository).findOne(Example.of(currency));
//        WHEN
        CryptoCurrency actual = operationsService.fetchAndSave(symbol1, symbol2, body);
//        THEN
        assertEquals(currency, actual);
    }
}