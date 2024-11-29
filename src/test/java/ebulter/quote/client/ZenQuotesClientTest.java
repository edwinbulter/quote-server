package ebulter.quote.client;

import ebulter.quote.model.Quote;
import ebulter.quote.wsmodel.WsZenQuote;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZenQuotesClientTest {

    @Test
    public void mapToUniqueQuotes_EmptyList_ShouldReturnEmptySet() {
        List<WsZenQuote> wsZenQuotes = new ArrayList<>();
        Set<Quote> quotes = ZenQuotesClient.mapToUniqueQuotes(wsZenQuotes);
        assertEquals(0, quotes.size());
    }

    @Test
    public void mapToUniqueQuotes_NullList_ShouldReturnEmptySet() {
        Set<Quote> quotes = ZenQuotesClient.mapToUniqueQuotes(null);
        assertEquals(0, quotes.size());
    }

    @Test
    public void mapToUniqueQuotes_ListWithDoubleZenQuote_ShouldReturnSetWithoutDoubleQuote() {
        List<WsZenQuote> wsZenQuotes = List.of(
                new WsZenQuote("ZenQuote 1", "Author 1", "category 1", "reference 1"),
                new WsZenQuote("ZenQuote 2", "Author 2", "category 2", "reference 2"),
                new WsZenQuote("ZenQuote 2", "Author 3", "category 3", "reference 3"));

        Set<Quote> quotes = ZenQuotesClient.mapToUniqueQuotes(wsZenQuotes);
        assertEquals(2, quotes.size());
        Quote expectedQuote1 = new Quote();
        expectedQuote1.setQuoteText("ZenQuote 1");
        Quote expectedQuote2 = new Quote();
        expectedQuote2.setQuoteText("ZenQuote 2");
        Assertions.assertThat(quotes).containsExactlyInAnyOrder(expectedQuote1, expectedQuote2);
    }
}