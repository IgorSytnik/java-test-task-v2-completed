package com.cryptocurrency.demo.repositories;

import com.cryptocurrency.demo.entities.CryptoCurrency;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends MongoRepository<CryptoCurrency, String> {
    CryptoCurrency findByCurrName1(String currName1);
}
