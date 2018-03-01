package com.gregspitz.flashcardapp.data.services;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class to handle building each retrofit service
 */
public class ServiceBuilder {

    private static final String URL = "http://192.168.1.128:8080/";

    // Create logger
    private static HttpLoggingInterceptor sLogger =
            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    // Create OkHttp Client
    private static OkHttpClient.Builder sOkHttp =
            new OkHttpClient.Builder().addInterceptor(sLogger);

    private static Retrofit.Builder sBuilder = new Retrofit.Builder().baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(sOkHttp.build());

    private static Retrofit sRetrofit = sBuilder.build();

    public static <S> S buildService(Class<S> serviceType) {
        return sRetrofit.create(serviceType);
    }
}
