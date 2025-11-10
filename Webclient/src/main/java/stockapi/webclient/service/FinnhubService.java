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

    public record FinnhubResponseDto(String response){}

    public Mono<FinnhubResponseDto> getFinnhubResponse(String exchange){
        return finnhubClient.getSymbols(exchange)
                .map(this::mapToDto);
    }

    private FinnhubResponseDto mapToDto(List<FinnhubClient.StockSymbol> stocks){
        String responseText = stocks.stream()
                .limit(10) // max amount of stocks loaded
                .map(s -> s.symbol()+ " - " + s.description() + "(" + s.currency()+")")
                .collect(Collectors.joining("\n"));

        return new FinnhubResponseDto(responseText);


    }



}
