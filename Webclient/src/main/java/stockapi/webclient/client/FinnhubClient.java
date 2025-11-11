package stockapi.webclient.client;

import org.springframework.beans.factory.annotation.Qualifier;
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

            String description,
            String displaySymbol,
            String symbol,
            String type
    ) {}
    public record FinnhubSearchResponse(List<StockSymbol> result){}

    public Mono<FinnhubSearchResponse> getSymbols(String query){
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", query)
                        .queryParam("token", apiKey)
                        .build())
                .retrieve()
                .onStatus(s -> s.value() == 400, r -> r.bodyToMono(String.class)
                        .flatMap(msg -> Mono.error(new IllegalArgumentException("Finnhub 400: " + msg))))
                .onStatus(s -> s.value() == 401, r -> r.bodyToMono(String.class)
                        .flatMap(msg -> Mono.error(new IllegalArgumentException("Finnhub 401 unauthorized: " + msg))))
                .onStatus(HttpStatusCode::isError, r -> r.bodyToMono(String.class)
                        .flatMap(msg -> Mono.error(new IllegalArgumentException("Finnhub Error: " + msg))))
                .bodyToMono(FinnhubSearchResponse.class);
    }


    public record StockQuote (
            String symbol,
            double c,  // current price
         double d,  // change
         double dp, // percent change
         double h,  // high
         double l,  // low
         double o,  // open
         double pc, // previous close
         long t ){}   // timestamp

    public Mono<StockQuote> getQuote(String symbol) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/quote")
                        .queryParam("symbol", symbol.toUpperCase())
                        .queryParam("token", apiKey)
                        .build())
                .retrieve()
                .onStatus(s -> s.value() == 400, r -> r.bodyToMono(String.class)
                        .map(msg -> new IllegalArgumentException("Finnhub 400: " + msg)))
                .onStatus(s -> s.value() == 401, r -> r.bodyToMono(String.class)
                        .map(msg -> new IllegalArgumentException("Finnhub 401 unauthorized: " + msg)))
                .onStatus(HttpStatusCode::isError, r -> r.bodyToMono(String.class)
                        .map(msg -> new IllegalArgumentException("Finnhub Error: " + msg)))
                .bodyToMono(StockQuote.class)
                .doOnNext(System.out::println);
    }




}
