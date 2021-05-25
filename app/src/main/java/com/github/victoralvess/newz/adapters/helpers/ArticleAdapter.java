package com.github.victoralvess.newz.adapters.helpers;

import com.github.victoralvess.newz.domain.entities.News;
import com.github.victoralvess.newz.domain.entities.Source;
import com.kwabenaberko.newsapilib.models.Article;

public class ArticleAdapter {
    public static News asNews(Article article) {
        com.kwabenaberko.newsapilib.models.Source source = article.getSource();

        return new News(
                article.getTitle(),
                article.getDescription(),
                new Source(source.getId() != null ? source.getId() : source.getName(), source.getName()),
                article.getAuthor() != null ? article.getAuthor() : source.getName(),
                article.getUrl(),
                article.getUrlToImage(),
                article.getPublishedAt()
        );
    }
}
