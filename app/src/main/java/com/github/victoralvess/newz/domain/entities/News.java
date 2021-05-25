package com.github.victoralvess.newz.domain.entities;

import java.io.Serializable;

public class News implements Serializable {
    private final String title;
    private final String snippet;
    private final Source source;
    private final String author;
    private final String url;
    private final String pictureUrl;
    private final String publishedAt;
    public Boolean bookmarked = false;

    public News(String title, String snippet, Source source, String author, String url, String pictureUrl, String publishedAt) {
        this.title = title;
        this.snippet = snippet;
        this.source = source;
        this.author = author;
        this.url = url;
        this.pictureUrl = pictureUrl;
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
    }

    public Source getSource() {
        return source;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getPublishedAt() {
        return publishedAt;
    }
}
