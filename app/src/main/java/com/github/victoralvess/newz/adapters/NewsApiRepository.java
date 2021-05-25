package com.github.victoralvess.newz.adapters;

import android.util.Log;

import com.github.victoralvess.newz.adapters.helpers.ArticleAdapter;
import com.github.victoralvess.newz.domain.entities.News;
import com.github.victoralvess.newz.domain.port.NewsCallback;
import com.github.victoralvess.newz.domain.port.NewsRepository;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

public class NewsApiRepository implements NewsRepository {
    private final NewsApiClient client = new NewsApiClient("xxx");

    @Override
    public void findByQuery(String q, NewsCallback cb) {
        Log.i("NEWS_API", "[START]");
        client.getEverything(
                new EverythingRequest.Builder()
                        .q(q)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        Log.i("NEWS_API", "[SUCCESS]");
                        cb.onResult(parseResponse(response));
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.i("NEWS_API", "[FAILURE]");
                        cb.onResult(parseResponse(null));
                    }
                }
        );
    }

    @Override
    public void findByCountry(String countryCode, NewsCallback cb) {
        client.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .country(countryCode)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        cb.onResult(parseResponse(response));
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        cb.onResult(parseResponse(null));
                    }
                }
        );
    }

    private List<News> parseResponse(ArticleResponse response) {
        if (response == null)
            return new ArrayList<>();

        List<News> news = new ArrayList<>();

        for (Article article : response.getArticles())
            news.add(ArticleAdapter.asNews(article));

        return news;
    }
}
