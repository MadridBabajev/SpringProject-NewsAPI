package org.example.demo.dao;

import org.example.demo.model.Article;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ArticleRowMapper implements RowMapper<Article> {

    @Override
    public Article mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                return new Article(
                resultSet.getString("title"),
                resultSet.getString("author"),
                LocalDate.parse(resultSet.getString("release_date")),
                resultSet.getString("content"));
    }
}