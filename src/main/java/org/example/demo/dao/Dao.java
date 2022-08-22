package org.example.demo.dao;

import org.example.demo.model.Article;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface Dao {

    Optional<Article> selectByTitleName(String title);

    void insertData(String title, String author, LocalDate releaseDate, String content);

    int deleteData(String title);

    List<Article> selectLastNEntries(int cycles);

    List<Article> selectLastNEntries();
}
