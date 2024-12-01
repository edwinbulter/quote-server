package ebulter.quote.service;

import ebulter.quote.client.ZenQuotesClient;
import ebulter.quote.model.Quote;
import ebulter.quote.repository.QuoteRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;

@Service
public class QuoteService {
    private static final int DEFAULT_LIKES = 0;

    private final ZenQuotesClient zenQuotesClient;
    private final QuoteRepository quoteRepository;
    private final QuoteEmitter quoteEmitter;


    public QuoteService(ZenQuotesClient zenQuotesClient, QuoteRepository quoteRepository, QuoteEmitter quoteEmitter) {
        this.zenQuotesClient = zenQuotesClient;
        this.quoteRepository = quoteRepository;
        this.quoteEmitter = quoteEmitter;
    }

    public long countQuotesInDatabase() {
        return quoteRepository.count();
    }

    public int fetchQuotesFromZenAndAddToDatabase(int numberOfFetches) {
        Set<Quote> fetchedQuotes = zenQuotesClient.getSomeUniqueQuotes(numberOfFetches);
        Set<Quote> currentDatabaseQuotes = new HashSet<>(quoteRepository.findAll());
        //Note: In the following statement, the quotes are compared (and subsequently removed) solely by quoteText.
        fetchedQuotes.removeAll(currentDatabaseQuotes);
        List<Quote> savedQuotes = quoteRepository.saveAll(fetchedQuotes);
        return savedQuotes.size();
    }

    public Quote readRandomQuoteFromDatabase() {
        return readRandomQuoteFromDatabase(null);
    }

    public Quote readRandomQuoteFromDatabase(Set<Long> idsToExclude) {
        List<Quote> currentDatabaseQuotes = quoteRepository.findAll();
        if (idsToExclude != null && idsToExclude.size() >= currentDatabaseQuotes.size()) {
            //all quotes from the db have already been returned once to this user, so try to add some new to the db
            int nrOfNewQuotesInTheDB = fetchQuotesFromZenAndAddToDatabase(1);
            if (nrOfNewQuotesInTheDB == 0) {
                //no new quotes found, so we can't exclude any this time
                idsToExclude.clear();
            } else {
                currentDatabaseQuotes = quoteRepository.findAll();
            }
        }
        Random random = new Random();
        if (idsToExclude != null && !idsToExclude.isEmpty()) {
            List<Quote> filteredDatabaseQuotes = currentDatabaseQuotes.stream().filter(quote -> !idsToExclude.contains(quote.getId())).toList();
            int randomIndex = random.nextInt(filteredDatabaseQuotes.size());
            return filteredDatabaseQuotes.get(randomIndex);
        } else {
            int randomIndex = random.nextInt(currentDatabaseQuotes.size());
            return currentDatabaseQuotes.get(randomIndex);
        }
    }

    public Flux<String> streamLikedQuotes() {
        return quoteEmitter.getQuoteFlux();
    }


    public int likeQuote(long id) {
        Optional<Quote> foundQuote = quoteRepository.findById(id);
        foundQuote.ifPresent(quoteEmitter::publishQuote);
        return foundQuote.map(this::incrementQuoteLikes).orElse(DEFAULT_LIKES);
    }

    private int incrementQuoteLikes(Quote quote) {
        quote.setLikes(quote.getLikes() + 1);
        quoteRepository.save(quote);
        return quote.getLikes();
    }

}
