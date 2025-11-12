package stockapi.webclient.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import stockapi.webclient.client.FinnhubClient;
import stockapi.webclient.service.FinnhubService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/api/finnhub")
public class FinnhubController {

    private final FinnhubService finnhubService;

    public FinnhubController(FinnhubService finnhubService) {
        this.finnhubService = finnhubService;
    }

    public record QuereRequest(String prompt) {}

    @GetMapping("/search")
    public Mono<ResponseEntity<List<FinnhubClient.StockSymbol>>> getQuote(@RequestParam String stock) {
        return finnhubService.getFinnhubSymbols(stock)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<FinnhubService.FinnhubResponseDto>> getExchange(@RequestParam String stock){
        return finnhubService.getFinnhubResponse(stock)
                .map(ResponseEntity::ok);
    }
}
