package com.github.victoralvess.newz.domain.port;

import com.github.victoralvess.newz.domain.CheckData;
import com.github.victoralvess.newz.domain.entities.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NewsService {
    @GET("bookmarks/{user}")
    Call<List<News>> listBookmarks(@Path("user") String user);

    @POST("bookmarks/{user}")
    Call<News> createBookmark(@Path("user") String user, @Body News news);

    @POST("bookmarks/{user}/check")
    Call<Boolean> isBookmarked(@Path("user") String user, @Body CheckData data);

    @POST("bookmarks/{user}/delete")
    Call<Void> deleteBookmark(@Path("user") String user, @Body CheckData data);
}
