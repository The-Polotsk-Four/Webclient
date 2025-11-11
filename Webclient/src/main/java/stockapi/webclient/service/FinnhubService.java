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
            double currentPrice,
            double change,
            double percentChange,
            double high,
            double low,
            double open,
            double previousClose
    ) {}

    public Mono<FinnhubResponseDto> getFinnhubResponse(String stock) {
        return finnhubClient.getQuote(stock)
                .map(q -> new FinnhubResponseDto(q.c(), q.d(), q.dp(), q.h(), q.l(), q.o(), q.pc()));
    }

    private FinnhubResponseDto mapToDto(FinnhubClient.StockQuote quote) {
        return new FinnhubResponseDto(
                quote.c(),
                quote.d(),
                quote.dp(),
                quote.h(),
                quote.l(),
                quote.o(),
                quote.pc()
        );
    }
    }
