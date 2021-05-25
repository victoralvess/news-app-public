package com.github.victoralvess.newz.domain.port;

import com.github.victoralvess.newz.domain.entities.News;

import java.util.List;

public interface NewsCallback {
    void onResult(List<News> news);
}
