package com.github.victoralvess.newz.usecases;

import com.github.victoralvess.newz.domain.port.NewsCallback;
import com.github.victoralvess.newz.domain.port.NewsRepository;

public class SearchNewsUseCase {
    private final NewsRepository newsRepository;

    public SearchNewsUseCase(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public void findByQuery(String q, NewsCallback cb) {
        newsRepository.findByQuery(q, cb);
    }

    public void findByCountry(String countryCode, NewsCallback cb) {
        newsRepository.findByCountry(countryCode, cb);
    }
}
