package stockapi.webclient.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import stockapi.webclient.client.FinnhubClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinnhubService {
    private final FinnhubClient finnhubClient;

    public FinnhubService(FinnhubClient finnhubClient) {
        this.finnhubClient = finnhubClient;
    }

    public record FinnhubResponseDto(
            String symbol,
            double currentPrice,
            double change,
            double percentChange,
            double high,
            double low,
            double open,
            double previousClose
    ) {}

    public Mono<FinnhubResponseDto> getFinnhubResponse(String stock) {
        String symbol=stock.toUpperCase();
        return finnhubClient.getQuote(stock)
                .map(q -> new FinnhubResponseDto(symbol,q.c(), q.d(), q.dp(), q.h(), q.l(), q.o(), q.pc()));
    }

    private FinnhubResponseDto mapToDto(FinnhubClient.StockQuote quote) {
        return new FinnhubResponseDto(
                quote.symbol(),
                quote.c(),
                quote.d(),
                quote.dp(),
                quote.h(),
                quote.l(),
                quote.o(),
                quote.pc()
        );
    }

    public record FinnhubSearchResultDto(
            String description,
            String displaySymbol,
            String symbol,
            String type
    ) {}


    public Mono<List<FinnhubClient.StockSymbol>> getFinnhubSymbols(String query){
        return finnhubClient.getSymbols(query)
                .map(FinnhubClient.FinnhubSearchResponse::result);
    }

}
