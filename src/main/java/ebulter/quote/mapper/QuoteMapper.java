package ebulter.quote.mapper;

import ebulter.quote.model.Quote;
import ebulter.quote.wsmodel.WsQuote;
import ebulter.quote.wsmodel.WsZenQuote;

import java.util.List;

public class QuoteMapper {

    public static Quote mapToQuote(WsZenQuote wsZenQuote) {
        Quote quote = new Quote();
        quote.setQuoteText(wsZenQuote.q());
        quote.setAuthor(wsZenQuote.a());
        return quote;
    }

    public static List<WsQuote> mapToWsQuotes(List<Quote> quotes) {
        return quotes.stream().map(QuoteMapper::mapToWsQuote).toList();
    }

    public static WsQuote mapToWsQuote(Quote quote) {
        return new WsQuote(quote.getId(), quote.getQuoteText(), quote.getAuthor());
    }

}
