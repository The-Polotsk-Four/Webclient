package stockapi.webclient.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import stockapi.webclient.service.FinnhubService;

@RestController
@RequestMapping("/api/finnhub")
public class FinnhubController {

    private final FinnhubService finnhubService;

    public FinnhubController(FinnhubService finnhubService) {
        this.finnhubService = finnhubService;
    }

    public record QuereRequest(String prompt) {}

    @GetMapping
    public Mono<FinnhubService.FinnhubResponseDto> getQuote(@RequestParam String stock) {
        return finnhubService.getFinnhubResponse(stock);
    }

    @PostMapping
    public Mono<ResponseEntity<FinnhubService.FinnhubResponseDto>> getExchange(@RequestParam String stock){
        return finnhubService.getFinnhubResponse(stock)
                .map(ResponseEntity::ok);
    }
}
