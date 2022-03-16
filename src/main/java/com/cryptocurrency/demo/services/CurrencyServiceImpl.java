package com.cryptocurrency.demo.services;

import com.cryptocurrency.demo.entities.CryptoCurrency;
import com.cryptocurrency.demo.entities.Price;
import com.cryptocurrency.demo.exceptions.PriceException;
import com.cryptocurrency.demo.repositories.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private final CurrencyRepository currencyRepository;

    /**
     * Saves {@link List}<{@link CryptoCurrency}> to the database
     * and then returns {@link List} of saved {@link CryptoCurrency}.
     *
     * @param list {@link List} to save to the database.
     * @return {@link List} of saved {@link CryptoCurrency}.
     * @see CurrencyRepository#saveAll(Iterable)
     */
    @Override
    public List<CryptoCurrency> saveList(List<CryptoCurrency> list) {
        return currencyRepository.saveAll(list);
    }

    /**
     * Returns {@link List} of all {@link CryptoCurrency} in th database.
     *
     * @return {@link List} of all {@link CryptoCurrency} in th database.
     * @see CurrencyRepository#findAll()
     */
    @Override
    public List<CryptoCurrency> restoreAll() {
        return currencyRepository.findAll();
    }

    /**
     * Returns max {@link Price} of currency with the given name.
     *
     * @param currName1 name of the cryptocurrency.
     * @return max {@link Price} of the cryptocurrency with name <i>currName1</i>.
     * @throws PriceException if the currency has no prices.
     * @see CurrencyRepository#findByCurrName1(String)
     */
    @Override
    public Price getMaxPriceByCurrName1(String currName1) throws PriceException {
        return currencyRepository.findByCurrName1(currName1).getPrices().stream()
                .max(Comparator.comparing(price -> Double.valueOf(price.getPrice())))
                .orElseThrow(() ->
                        new PriceException(String.format("Couldn't find min price for currency \"%s\".", currName1)));
    }

    /**
     * Returns min {@link Price} of currency with the given name.
     *
     * @param currName1 name of the cryptocurrency.
     * @return min {@link Price} of the cryptocurrency with name <i>currName1</i>.
     * @throws PriceException if the currency has no prices.
     * @see CurrencyRepository#findByCurrName1(String)
     */
    @Override
    public Price getMinPriceByCurrName1(String currName1) throws PriceException {
        return currencyRepository.findByCurrName1(currName1).getPrices().stream()
                .min(Comparator.comparing(price -> Double.valueOf(price.getPrice())))
                .orElseThrow(() ->
                        new PriceException(String.format("Couldn't find max price for currency \"%s\".", currName1)));
    }

    /**
     * Returns sorted {@link List}<{@link Price}> of currency with
     * the given name from low price to high price.
     *
     * @param currName1 name of the cryptocurrency.
     * @return sorted {@link List} of cryptocurrency with name <i>currName</i>.
     * @see CurrencyRepository#findByCurrName1(String)
     */
    @Override
    public List<Price> getPricesByCurrName1Sorted(String currName1) {
        return currencyRepository.findByCurrName1(currName1).getPrices().stream()
                .sorted(Comparator.comparing(price -> Double.valueOf(price.getPrice())))
                .collect(Collectors.toList());
    }

    /**
     * Checks if currency with the given name exists in the database.
     *
     * @param currName1 name of the cryptocurrency.
     * @return <b>true</b> if cryptocurrency with name <i>currName1</i> exists,
     * <b>false</b> otherwise.
     * @see CurrencyRepository#exists(Example)
     */
    @Override
    public boolean checkCurrency(String currName1) {
        return currencyRepository.exists(Example.of(new CryptoCurrency(currName1, null)));
    }
}
