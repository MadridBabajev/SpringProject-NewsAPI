package org.example.demo.service;

import org.example.demo.dao.Dao;
import org.example.demo.exceptions.NotFoundException;
import org.example.demo.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private final Dao dao;

    @Autowired
    public ArticleService(Dao dao) {
        this.dao = dao;
    }

    public void addArticle(Article article) {
        dao.insertData(article.getTitle(),
                article.getAuthor(),
                article.getReleaseDate(),
                article.getContent());
    }

    public int deleteArticle(String title) {
        Optional<Article> articleOptional = dao.selectByTitleName(title);

        if (articleOptional.isPresent()) {
            int result = dao.deleteData(title);
            if (result < 1) throw new IllegalStateException("Could not delete article..");
            return result;
        }
        throw new NotFoundException("Article with title %s was not found".formatted(title));
    }

    public List<Article> getLastNArticles(Optional<Integer> cycles) {
        if (cycles.isPresent()) return dao.selectLastNEntries(cycles.get());
        return dao.selectLastNEntries();
    }

}
