package ebulter.quote.service;

import ebulter.quote.model.Quote;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class FallbackFileReaderTest {

    @Test
    void readQuotes_hasExpectedNumberOfQuotesAndContainsAnExpectedQuote() {
        Set<Quote> quotes = FallbackFileReader.readQuotes();
        Quote expectedQuote = new Quote();
        expectedQuote.setQuoteText("The more one judges, the less one loves.");
        Assertions.assertThat(quotes.size()).isEqualTo(50);
        Assertions.assertThat(quotes).contains(expectedQuote);
    }

}