package org.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Objects;

public class Article {

    private final String title;
    private final String author;
    private final LocalDate releaseDate;
    private final String content;

    public Article(@JsonProperty("title") String title,
                   @JsonProperty("author") String author,
                   @JsonProperty("publishedAt") LocalDate releaseDate,
                   @JsonProperty("content") String content) {
        this.title = title;
        this.author = author;
        this.releaseDate = releaseDate;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Article)) return false;
        Article other = (Article) obj;
        return Objects.equals(title, other.getTitle())
                && Objects.equals(author, other.getAuthor())
                && Objects.equals(releaseDate, other.getReleaseDate())
                && Objects.equals(content, other.getContent());
    }

}
