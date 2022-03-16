package com.cryptocurrency.demo.services;

import com.cryptocurrency.demo.entities.CryptoCurrency;
import com.cryptocurrency.demo.entities.Price;

import java.util.List;

public interface CurrencyService {
    List<CryptoCurrency> saveList(List<CryptoCurrency> list);
    List<CryptoCurrency> restoreAll();
    Price getMaxPriceByCurrName1(String currName1);
    Price getMinPriceByCurrName1(String currName1);
    List<Price> getPricesByCurrName1Sorted(String currName1);
    boolean checkCurrency(String currName1);
}
