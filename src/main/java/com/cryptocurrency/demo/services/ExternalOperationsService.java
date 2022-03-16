package com.cryptocurrency.demo.services;

import com.cryptocurrency.demo.entities.CryptoCurrency;

import java.io.IOException;

public interface ExternalOperationsService {
    CryptoCurrency fetchAndSave(String symbol1, String symbol2, String body) throws IOException;
}
