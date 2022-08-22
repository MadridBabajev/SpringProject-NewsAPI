package org.example.demo.dao;

import org.example.demo.model.Article;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(
        classes = {DaoRepository.class}
)
@EnableAutoConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DaoRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DaoRepository daoRepository;

    private List<Article> getInitialEntries() {
        return Arrays.asList(
                new Article("title1", "author1", getRandomDate(), "content1"),
                new Article("title2", "author2", getRandomDate(), "content2"),
                new Article("title3", "author3", getRandomDate(), "content3"),
                new Article("title4", "author4", getRandomDate(), "content4"),
                new Article("title5", "author5", getRandomDate(), "content5")
                );
    }

    private LocalDate getRandomDate() {
        long from = LocalDate.of(1985, 1, 1).toEpochDay();
        long to = LocalDate.of(2022, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(from, to);
        return LocalDate.ofEpochDay(randomDay);
    }

    @Test
    @Order(1)
    void insertDataTest() {
        Article article = new Article("title1", "author1", LocalDate.now(), "content1");
        daoRepository.insertData(article.getTitle(), article.getAuthor(), article.getReleaseDate(), article.getContent());
        System.out.println(daoRepository.selectLastNEntries().size());
        assertThat(daoRepository.selectLastNEntries().size()).isEqualTo(1);
        assertThat(daoRepository.selectByTitleName("title1").get()).isEqualTo(article);
    }
    @Test
    @Order(2)
    void deleteDataTest() {
        Article article = new Article("title1", "author1", LocalDate.now(), "content1");
        daoRepository.insertData(article.getTitle(), article.getAuthor(), article.getReleaseDate(), article.getContent());
        assertThat(daoRepository.selectLastNEntries().size()).isEqualTo(1);
        daoRepository.deleteData(article.getTitle());
        assertThat(daoRepository.selectLastNEntries().size()).isEqualTo(0);
    }

    @Test
    @Order(3)
    void selectByTitleNameTest() {
        List<Article> articles = new ArrayList<>();
        getInitialEntries().forEach(article -> {
            daoRepository.insertData(article.getTitle(), article.getAuthor(), article.getReleaseDate(), article.getContent());
            articles.add(article);
        });

        Optional<Article> selected = daoRepository.selectByTitleName("title2");
        assertThat(selected.isPresent()).isTrue();
        LocalDate foundDate = selected.get().getReleaseDate();
        assertThat(selected.get()).isEqualTo(new Article("title2", "author2", foundDate, "content2"));
    }

    @Test
    @Order(4)
    void testSelectLastNEntriesTest() {
        List<Article> articles = daoRepository.selectLastNEntries();
        getInitialEntries().forEach(article -> daoRepository.insertData(article.getTitle(), article.getAuthor(), article.getReleaseDate(), article.getContent()));
        assertThat(articles.size()).isEqualTo(5);
    }
}
