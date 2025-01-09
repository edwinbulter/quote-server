package ebulter.quote.controller;

import ebulter.quote.mapper.QuoteMapper;
import ebulter.quote.service.QuoteService;
import ebulter.quote.wsmodel.WsQuote;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class QuoteController {

    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping("/quote")
    public WsQuote getRandomQuote() {
        return QuoteMapper.mapToWsQuote(quoteService.readRandomQuoteFromDatabase());
    }

    @PostMapping("/quote")
    public WsQuote getRandomQuoteWithIdsToExclude(@RequestBody Set<Long> idsToExclude) {
        return QuoteMapper.mapToWsQuote(quoteService.readRandomQuoteFromDatabase(idsToExclude));
    }

    @PatchMapping("/quote/{id}/like")
    public int likeQuote(@PathVariable long id) {
        return quoteService.likeQuote(id);
    }

    @GetMapping("/quote/stream")
    public Flux<String> streamLikedQuotes() {
        return quoteService.streamLikedQuotes();
    }

    @GetMapping("/quote/liked")
    public List<WsQuote> getLikedQuotes() {
        return QuoteMapper.mapToWsQuotes(quoteService.readLikedQuotesFromDatabase());
    }
}
