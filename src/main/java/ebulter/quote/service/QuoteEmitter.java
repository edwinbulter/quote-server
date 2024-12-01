package ebulter.quote.service;

import ebulter.quote.model.Quote;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class QuoteEmitter {

    private final Sinks.Many<String> sink;

    public QuoteEmitter() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void publishQuote(Quote quote) {
        sink.tryEmitNext("%s - %s".formatted(quote.getQuoteText(), quote.getAuthor()));
    }

    public Flux<String> getQuoteFlux() {
        return sink.asFlux();
    }
}
