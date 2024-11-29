package ebulter.quote.client;

import ebulter.quote.mapper.QuoteMapper;
import ebulter.quote.model.Quote;
import ebulter.quote.wsmodel.WsZenQuote;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ZenQuotesClient {
    @Value("${zenquotes.url}")
    private String quotesUrl;

    public Set<Quote> getSomeUniqueQuotes(int numberOfRequests) {
        Set<Quote> result = new HashSet<>();
        for (int i = 1; i <= numberOfRequests; i++) {
            result.addAll(getSomeUniqueQuotes());
        }
        return result;
    }

    private Set<Quote> getSomeUniqueQuotes() {
        WebClient webClient = WebClient.builder().baseUrl(quotesUrl).build();
        Mono<List<WsZenQuote>> response = webClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
        List<WsZenQuote> wsZenQuotes = response.block();
        return mapToUniqueQuotes(wsZenQuotes);
    }

    public static Set<Quote> mapToUniqueQuotes(List<WsZenQuote> wsZenQuotes) {
        if (wsZenQuotes != null) {
            return wsZenQuotes.stream().map(QuoteMapper::mapToQuote).collect(Collectors.toSet());
        }
        return new HashSet<>();
    }
}
