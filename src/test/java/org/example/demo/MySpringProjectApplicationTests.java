package org.example.demo;

import org.example.demo.dao.DaoRepository;
import org.example.demo.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class MySpringProjectApplicationTests {

    @Autowired
    private ArticleService newsService;

    @MockBean
    private DaoRepository repository;

    @Test
    void contextLoads() {
    }

}
