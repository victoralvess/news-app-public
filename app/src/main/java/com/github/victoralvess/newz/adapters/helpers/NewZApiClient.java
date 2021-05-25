package com.github.victoralvess.newz.adapters.helpers;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewZApiClient {
    private static NewZApiClient instance = null;

    private Retrofit retrofit;

    private NewZApiClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://newz-api.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NewZApiClient getInstance() {
        if (instance == null) {
            instance = new NewZApiClient();
        }

        return instance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
