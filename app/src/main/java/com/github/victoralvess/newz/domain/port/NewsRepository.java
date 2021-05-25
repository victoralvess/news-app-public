package com.github.victoralvess.newz.domain.port;

public interface NewsRepository {
    void findByQuery(String q, NewsCallback cb);
    void findByCountry(String countryCode, NewsCallback cb);
}
