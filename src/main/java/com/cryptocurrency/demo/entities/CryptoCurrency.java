package com.cryptocurrency.demo.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "crypto_currency")
@CompoundIndexes({
        @CompoundIndex(name = "unique_pair_idx", def = "#{T(org.bson.Document).parse(\"{ 'currName1': 1, 'currName2': 1 }\")}", unique = true)
})
public class CryptoCurrency {

    @Id
    @EqualsAndHashCode.Exclude
    private String id;
    private String currName1;
    private String currName2;
    private List<Price> prices;

    public CryptoCurrency(String currName1, String currName2, List<Price> prices) {
        this.currName1 = currName1;
        this.currName2 = currName2;
        this.prices = prices;
    }

    public CryptoCurrency(String currName1, String currName2) {
        this.currName1 = currName1;
        this.currName2 = currName2;
    }
}
