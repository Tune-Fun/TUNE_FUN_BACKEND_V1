package com.tune_fun.v1.external.http;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitClient {

    private RetrofitClient() {
    }

    public static Retrofit getInstance() {
        return RetrofitClientHolder.INSTANCE;
    }

    private static class RetrofitClientHolder {
        private static final Retrofit INSTANCE = getRetrofit();

    }

    private static Retrofit getRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        return new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

}
