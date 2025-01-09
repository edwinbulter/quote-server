package ebulter.quote.service;

import ebulter.quote.client.ZenQuotesClient;
import ebulter.quote.model.Quote;
import ebulter.quote.repository.QuoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;

@Service
public class QuoteService {
    private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);
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
        Set<Quote> fetchedQuotes = fetchQuotesFromZenWithFallback(numberOfFetches);
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
            logger.info("Saved {} quotes to database", nrOfNewQuotesInTheDB);
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

    public List<Quote> readLikedQuotesFromDatabase() {
        return quoteRepository.findByLikesGreaterThanOrderByLikesDescIdAsc(0);
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

    private Set<Quote> fetchQuotesFromZenWithFallback(int numberOfFetches) {
        Set<Quote> fetchedQuotes = null;
        try {
            fetchedQuotes = zenQuotesClient.getSomeUniqueQuotes(numberOfFetches);
        } catch (Exception e) {
            logger.error("Failed to read quotes from ZenQuotes: {}", e.getMessage());
        }
        if (fetchedQuotes == null || fetchedQuotes.size() < 2) {
            logger.error("Did not receive enough quotes from ZenQuotes, will fallback on using the FallbackFile with quotes.");
            fetchedQuotes = FallbackFileReader.readQuotes();
            logger.warn("Read {} quotes from FallbackFile", fetchedQuotes.size());
        } else {
            logger.info("Fetched {} quotes from Zen", fetchedQuotes.size());
        }
        return fetchedQuotes;
    }

}
