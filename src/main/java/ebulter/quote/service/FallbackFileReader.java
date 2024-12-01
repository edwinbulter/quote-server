package ebulter.quote.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ebulter.quote.mapper.QuoteMapper;
import ebulter.quote.model.Quote;
import ebulter.quote.wsmodel.WsZenQuote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


public class FallbackFileReader {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(FallbackFileReader.class);
    private static final String FALLBACK_FILE = "quotes-fallback-data.json";

    public static Set<Quote> readQuotes() {
        try (InputStream inputStream = FallbackFileReader.class.getClassLoader().getResourceAsStream(FALLBACK_FILE);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8))) {
            List<WsZenQuote> zenQuotes = objectMapper.readValue(bufferedReader, new TypeReference<>() {});
            return mapToUniqueQuotes(zenQuotes);
        } catch (IOException e) {
            logger.error("Unable to read quotes from " + FALLBACK_FILE, e);
            return new HashSet<>();
        }
    }

    private static Set<Quote> mapToUniqueQuotes(List<WsZenQuote> wsZenQuotes) {
        if (wsZenQuotes != null) {
            return wsZenQuotes.stream().map(QuoteMapper::mapToQuote).collect(Collectors.toSet());
        }
        return new HashSet<>();
    }
}
