package stockapi.webclient.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class finnhubClient {
    private final WebClient client;

    public finnhubClient(@Qualifier("finnhubWebClient") WebClient client){
        this.client=client;
    }

    public record finnhubData(String currency, String description, String value){}
    public record content(String input){}


}
