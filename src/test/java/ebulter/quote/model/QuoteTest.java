package ebulter.quote.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuoteTest {

    @Test
    public void equals_withDifferentQuoteObjectsWithEqualQuoteString_ShouldReturnTrue() {
        Quote quote = createQuote(1L, "quote_1", "author_1", 2);
        Quote quote2 = new Quote();
        quote2.setQuoteText("quote_1");
        assertEquals(true, quote.equals(quote2));
    }

    @Test
    public void equals_withDifferentQuoteObjectsWithDifferentQuoteString_ShouldReturnFalse() {
        Quote quote = createQuote(1L, "quote_1", "author_1", 2);
        Quote quote2 = new Quote();
        quote2.setQuoteText("quote_X");
        assertEquals(false, quote.equals(quote2));
    }

    public static Quote createQuote(long id, String quote, String author, int likes) {
        Quote quote1 = new Quote();
        quote1.setId(id);
        quote1.setQuoteText(quote);
        quote1.setAuthor(author);
        quote1.setLikes(likes);
        return quote1;
    }
}
