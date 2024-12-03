package ebulter.quote.service;

import ebulter.quote.model.Quote;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class QuoteEmitterTest {

    @Test
    public void testPublishQuote() {
        QuoteEmitter quoteEmitter = new QuoteEmitter();
        Quote quote = new Quote();

        quote.setId(10L);
        quote.setAuthor("Test Author");
        quote.setQuoteText("Test Quote");
        quote.setLikes(100);

        quoteEmitter.publishQuote(quote);

        StepVerifier.create(quoteEmitter.getQuoteFlux())
                .expectNext("%s - %s".formatted(quote.getQuoteText(), quote.getAuthor()))
                .thenCancel()
                .verify();
    }
}