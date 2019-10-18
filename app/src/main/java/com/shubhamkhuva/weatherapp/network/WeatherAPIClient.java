package com.shubhamkhuva.weatherapp.network;

import com.shubhamkhuva.weatherapp.utils.CommonKey;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class WeatherAPIClient {
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder oktHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CommonKey.DEFAULT_CONNECT_TIMEOUT_IN_MS, MILLISECONDS)
                .writeTimeout(CommonKey.DEFAULT_WRITE_TIMEOUT_IN_MS, MILLISECONDS)
                .readTimeout(CommonKey.DEFAULT_READ_TIMEOUT_IN_MS, MILLISECONDS);
        oktHttpClient.addInterceptor(logging);
        oktHttpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        });
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(CommonKey.END_POINT)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(oktHttpClient.build())
                    .build();
        }
        return retrofit;
    }
}
