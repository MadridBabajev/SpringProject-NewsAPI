package org.example.demo.dao;

import org.example.demo.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class DaoRepository implements Dao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DaoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Article> selectByTitleName(String title) {
        String sql = """
                SELECT title, author, release_date, content
                FROM articles
                WHERE title = ?;
                """;
        return jdbcTemplate.query(sql, new ArticleRowMapper(), title)
                .stream().findFirst();
    }

    @Override
    public void insertData(String title, String author, LocalDate releaseDate, String content) {
        String sql = """
            INSERT INTO articles(title, author, release_date, content)
            VALUES (?, ?, ?, ?);
            """;
        try {
            jdbcTemplate.update(sql, title, author, releaseDate, content);
        } catch (DuplicateKeyException e) {
//            System.out.println("Duplicate with title {%s} found"
//                    .formatted(title));
        }
    }

    @Override
    public int deleteData(String title) {
        String sql = """
                    DELETE FROM articles
                    WHERE title = ?;
                    """;
        System.out.println("Deleting article with title %s".formatted(title));
        return jdbcTemplate.update(sql, title);
    }

    @Override
    public List<Article> selectLastNEntries() {
        String sql = """
                SELECT title, author, release_date, content
                FROM articles
                LIMIT 10;
                """;
        return jdbcTemplate.query(sql, new ArticleRowMapper());
    }

    @Override
    public List<Article> selectLastNEntries(int cycles) {
        String sql = """
                SELECT title, author, release_date, content
                FROM articles
                LIMIT %s;
                """.formatted(cycles);
        return jdbcTemplate.query(sql, new ArticleRowMapper());
    }
}
