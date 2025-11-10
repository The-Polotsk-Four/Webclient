package stockapi.webclient.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class FinnhubClient {
    private final WebClient client;

//    @Value("${finnhub.api.key}")
    private String apiKey="d48u9jhr01qrjsnvcsq0d48u9jhr01qrjsnvcsqg";

    public FinnhubClient(@Qualifier("finnhubWebClient") WebClient client){
        this.client=client;
    }

    public record StockSymbol(
            String currency,
            String description,
            String displaySymbol,
            String figi,
            String mic,
            String symbol,
            String type
    ) {}

    public Mono<List<StockSymbol>> getSymbols(String exchange){
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stock/symbol")
                        .queryParam("exchange", exchange)
                        .queryParam("token", apiKey)
                        .build())
                .retrieve()
                .onStatus(s -> s.value() == 400, r -> r.bodyToMono(String.class)
                        .map(msg -> new IllegalArgumentException("Finnhub 400: " +msg)))
                .onStatus(s -> s.value() == 401, r -> r.bodyToMono(String.class)
                        .map(msg -> new IllegalArgumentException("Finnhub 401 unauthorized: " +msg)))
                .onStatus(HttpStatusCode::isError, r -> r.bodyToMono(String.class)
                        .map(msg -> new IllegalArgumentException("Finnhub Error: " +msg)))
                .bodyToFlux(StockSymbol.class)
                .collectList();
    }




}
