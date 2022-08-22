package org.example.demo.controller;

import org.apache.http.client.utils.URIBuilder;
import org.example.demo.model.Article;
import org.example.demo.service.ArticleService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping(
        path = "/api/news"
)
// Enable scheduling
@Configuration
@EnableScheduling
public class ArticleController {

    private final ArticleService articleService;
    private final static String NEWS_API_URL = "https://newsapi.org/v2/top-headlines";

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }


    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Article> fetchNewsFromDataBase(@QueryParam("cycles") Integer cycles) {
        return articleService.getLastNArticles(Optional.ofNullable(cycles));
    }

    @RequestMapping(
            path = "{articleUniqueNumber}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Integer> deleteNews(@PathVariable("articleUniqueNumber") String title) {
        int result = articleService.deleteArticle(title);
        return integerResponseEntity(result);
    }

    // Reading data from an API method...

//    @Scheduled(initialDelay = 2000L, fixedRate = 10000L)
//    public void printLnEvery10Sec() {
//        System.out.println("10 sec.. passed");
//    }

    @Scheduled(initialDelay = 2000L, fixedRate = 60000L)
    public void fetchNewsFromAPI() throws URISyntaxException {

//         Build request with 3 parameters
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = new URIBuilder(NEWS_API_URL)
                .addParameter("country", "gb")
                .addParameter("category", "sports")
                .addParameter("apiKey", "95f2550c49bc44fdae185c395f84a672")
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        // Build response
        HttpResponse<String> response = httpClient
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .join();

        List<Article> extractedNews = extractNewsFromResponse(response);

        extractedNews.forEach(news -> articleService.addArticle(news));
    }

    private List<Article> extractNewsFromResponse(HttpResponse<String> response) {
        try {
            JSONObject jsonObject = new JSONObject(response.body());
            JSONArray articles = jsonObject.getJSONArray("articles");
            List<Article> newsList = new ArrayList<>();

            for (int i = 0; i < articles.length(); i++) {
                JSONObject articleInside = articles.getJSONObject(i);
                String title = articleInside.get("title").toString();
                String author = articleInside.get("author").toString();
                LocalDate releaseDate = parseDate(articleInside.get("publishedAt").toString());
                String content = articleInside.get("content").toString();
                newsList.add(new Article(title, author, releaseDate, content));
            }
            return newsList;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return List.of();
    }

    private LocalDate parseDate(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date parsedDate = inputFormat.parse(dateString);
            return LocalDate.parse(outputFormat.format(parsedDate));
        } catch (ParseException e) {
            return LocalDate.now();
        }
    }

    private ResponseEntity<Integer> integerResponseEntity(int result) {
        if (result == 1) return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }
}
