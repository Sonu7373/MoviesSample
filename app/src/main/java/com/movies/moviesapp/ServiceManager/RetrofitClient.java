package com.movies.moviesapp.ServiceManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    OkHttpClient.Builder httpClient;
    private static final String BASE_URL = "https://swapi.co/api/";
    ApiInterface apiInterface;

    private RetrofitClient() {
        httpClient = new OkHttpClient.Builder()
                .readTimeout(25, TimeUnit.SECONDS)
                .connectTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .addInterceptor(new LoggingInterceptor());
        //httpClient.addInterceptor(new LoggingInterceptor());
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        apiInterface = retrofit.create(ApiInterface.class);

    }

    private ApiInterface getRestClient() {
        return apiInterface;
    }

    public static ApiInterface getClient() {
        return new RetrofitClient().getRestClient();
    }

}
