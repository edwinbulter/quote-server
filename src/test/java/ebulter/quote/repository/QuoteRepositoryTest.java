package ebulter.quote.repository;

import ebulter.quote.model.Quote;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuoteRepositoryTest {

    @Autowired
    private QuoteRepository quoteRepository;

    @Test
    void whenNoQuotesWithLikesGreaterThanProvidedValue_thenReturnEmptyList() {
        // Arrange
        Quote quote1 = new Quote();
        quote1.setQuoteText("Life is beautiful.");
        quote1.setAuthor("Author1");
        quote1.setLikes(3);
        quoteRepository.save(quote1);

        Quote quote2 = new Quote();
        quote2.setQuoteText("Stay positive.");
        quote2.setAuthor("Author2");
        quote2.setLikes(5);
        quoteRepository.save(quote2);

        // Act
        List<Quote> result = quoteRepository.findByLikesGreaterThanOrderByLikesDescIdAsc(10);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void whenQuotesWithLikesGreaterThanProvidedValueExist_thenReturnCorrectOrder() {
        // Arrange
        Quote quote1 = new Quote();
        quote1.setQuoteText("Happiness is key.");
        quote1.setAuthor("Author1");
        quote1.setLikes(15);
        quote1 = quoteRepository.save(quote1); // Save to ensure id is generated

        Quote quote2 = new Quote();
        quote2.setQuoteText("Focus on the good.");
        quote2.setAuthor("Author2");
        quote2.setLikes(20);
        quote2 = quoteRepository.save(quote2);

        Quote quote3 = new Quote();
        quote3.setQuoteText("Consistency is key.");
        quote3.setAuthor("Author3");
        quote3.setLikes(15);
        quote3 = quoteRepository.save(quote3);

        // Act
        List<Quote> result = quoteRepository.findByLikesGreaterThanOrderByLikesDescIdAsc(10);

        // Assert
        assertThat(result).hasSize(3);
        assertThat(result.get(0)).isEqualTo(quote2); // Highest likes
        assertThat(result.get(1)).isEqualTo(quote1); // Equal likes, lower ID appears first
        assertThat(result.get(2)).isEqualTo(quote3);
    }

    @Test
    void whenNoQuotesInDatabase_thenReturnEmptyList() {
        // Act
        List<Quote> result = quoteRepository.findByLikesGreaterThanOrderByLikesDescIdAsc(0);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void whenAllQuoteLikesAreLessThanOrEqualToProvidedValue_thenReturnEmptyList() {
        // Arrange
        Quote quote1 = new Quote();
        quote1.setQuoteText("Dream big.");
        quote1.setAuthor("Author1");
        quote1.setLikes(3);
        quoteRepository.save(quote1);

        Quote quote2 = new Quote();
        quote2.setQuoteText("Be yourself.");
        quote2.setAuthor("Author2");
        quote2.setLikes(10);
        quoteRepository.save(quote2);

        // Act
        List<Quote> result = quoteRepository.findByLikesGreaterThanOrderByLikesDescIdAsc(10);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void whenSomeQuotesHaveLikesGreaterThanProvidedValue_thenReturnOnlyMatchingQuotes() {
        // Arrange
        Quote quote1 = new Quote();
        quote1.setQuoteText("Enjoy the journey.");
        quote1.setAuthor("Author1");
        quote1.setLikes(12);
        quoteRepository.save(quote1);

        Quote quote2 = new Quote();
        quote2.setQuoteText("Chase dreams.");
        quote2.setAuthor("Author2");
        quote2.setLikes(9);
        quoteRepository.save(quote2);

        // Act
        List<Quote> result = quoteRepository.findByLikesGreaterThanOrderByLikesDescIdAsc(10);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(quote1);
    }
}