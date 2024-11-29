package ebulter.quote.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Quote {

    @Id
    @GeneratedValue
    private long id;
    private String quoteText;
    private String author;
    private int likes;

    public void setId(Long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public void setQuoteText(String quote) {
        this.quoteText = quote;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quote quote1 = (Quote) o;
        return Objects.equals(quoteText, quote1.quoteText);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(quoteText);
    }
}
