package org.example.demo.service;

import org.example.demo.dao.DaoRepository;
import org.example.demo.model.Article;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = {ArticleService.class, DaoRepository.class}
)
@EnableAutoConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ArticleServiceTest {

    @Autowired
    private DaoRepository daoRepository;

    @Autowired
    private ArticleService articleService;

    @Test
    @Order(1)
    void addNewsTest() {
        Article article = new Article("title1", "author1", LocalDate.now(), "content1");
        articleService.addArticle(article);
        List<Article> articles = articleService.getLastNArticles(Optional.of(1));
        assertThat(articles.get(0)).isEqualTo(article);
    }

    @Test
    @Order(2)
    void deleteNewsTest() {
        Article article = articleService.getLastNArticles(Optional.of(1)).get(0);
        articleService.deleteArticle(article.getTitle());
        assertThat(articleService.getLastNArticles(Optional.of(1)).size()).isEqualTo(0);
    }

    @Test
    @Order(3)
    void getLastNNewsTest() {
        List<Article> articles = new ArrayList<>();
        articles.add(new Article("title1", "author1", LocalDate.now(), "content1"));
        articles.add(new Article("title2", "author2", LocalDate.now(), "content2"));
        articles.add(new Article("title3", "author3", LocalDate.now(), "content3"));
        articles.forEach(article -> articleService.addArticle(article));
        assertThat(articleService.getLastNArticles(Optional.of(3)).size())
                .isEqualTo(3);
    }
}
