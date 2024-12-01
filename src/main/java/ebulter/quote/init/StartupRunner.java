package ebulter.quote.init;

import ebulter.quote.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(StartupRunner.class);

    private final QuoteService quoteService;

    public StartupRunner(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Initialize database when empty");
        if (quoteService.countQuotesInDatabase() < 50) {
            int nrOfSavedQuotes = quoteService.fetchQuotesFromZenAndAddToDatabase(1);
            logger.info("Saved {} quotes to database", nrOfSavedQuotes);
        }
        logger.info("Finished initializing database");
    }
}