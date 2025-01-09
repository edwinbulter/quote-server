package ebulter.quote.repository;

import ebulter.quote.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {

    List<Quote> findByLikesGreaterThanOrderByLikesDescIdAsc(int likes);
}
