package stockapi.webclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient.Builder webClientBuilder(){
        return WebClient.builder()
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    }

    @Bean
    WebClient finnhubWebClient(
            WebClient.Builder b,
            @Value("${finnhub.api.key}") String apikey,
            @Value("${finnhub.api.baseUrl}") String baseUrl
    ) {
        if (apikey == null || apikey.isBlank()) {
            throw new IllegalArgumentException("Finnhub Api key must be provided in application.properties");
        }

        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalArgumentException("Finnhub Api baseUrl must be provided in application.properties");
        }

        return b.clone()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apikey)
                .build();
    }
}
