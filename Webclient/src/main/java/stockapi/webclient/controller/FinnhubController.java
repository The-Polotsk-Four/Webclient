package stockapi.webclient.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping
    public Mono<ResponseEntity<FinnhubService.FinnhubResponseDto>> getExchange(@RequestBody QuereRequest quereRequest){
        return finnhubService.getFinnhubResponse(quereRequest.prompt)
                .map(ResponseEntity::ok);
    }
}
