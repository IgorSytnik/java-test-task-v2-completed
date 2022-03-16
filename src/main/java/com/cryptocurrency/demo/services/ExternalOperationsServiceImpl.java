package com.cryptocurrency.demo.services;

import com.cryptocurrency.demo.entities.CryptoCurrency;
import com.cryptocurrency.demo.entities.Price;
import com.cryptocurrency.demo.repositories.CurrencyRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ExternalOperationsServiceImpl implements ExternalOperationsService {

    @Autowired
    private final CurrencyRepository currencyRepository;

    /**
     * Fetches data about the cryptocurrency from <a href="https://cex.io">cex.io</a>
     * and then saves it to the database.
     *
     * @param symbol1 primary currency name.
     * @param symbol2 secondary currency name. The currency to buy the
     *                <i>symbol1</i> currency with.
     * @param body configurations, what data to fetch.<br>
     *             Example:
     * <pre>{@code
     *  {
     *      "lastHours": 24,
     *      "maxRespArrSize": 100
     *  }
     * }</pre>
     * @return the saved {@link CryptoCurrency}.
     * @throws IOException if exception occurred with one of the used tools.
     * @see <a href="https://cex.io/rest-api#chart">https://cex.io/rest-api#chart</a>
     * @see CurrencyRepository
     */
    @Override
    public CryptoCurrency fetchAndSave(String symbol1, String symbol2, String body) throws IOException {
        URL url = new URL(String.format("https://cex.io/api/price_stats/%s/%s", symbol1, symbol2));
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = body.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            List<Price> prices = JsonToList(response.toString());

            CryptoCurrency cryptoCurrency = currencyRepository.findOne(
                    Example.of(new CryptoCurrency(symbol1, symbol2))).
                    orElseGet(() -> new CryptoCurrency(symbol1, symbol2));

            cryptoCurrency.setPrices(prices);
            return currencyRepository.save(cryptoCurrency);
        }
    }

    /**
     * Decodes json string to {@link List}<{@link Price}>
     *
     * @param jsonString json string.
     * @return {@link List} of {@link Price}
     * @see Gson
     */
    private List<Price> JsonToList(String jsonString) {
        Gson g = new Gson();
        return g.fromJson(jsonString, new TypeToken<List<Price>>(){}.getType());
    }
}
