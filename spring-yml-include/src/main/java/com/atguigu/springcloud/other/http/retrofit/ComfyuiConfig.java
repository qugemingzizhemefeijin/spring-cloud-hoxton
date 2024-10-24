package com.atguigu.springcloud.other.http.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
public class ComfyuiConfig {

//    @Bean
//    public ComfyuiApi comfyuiApi() throws IOException {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://127.0.0.1:8080")
//                .addConverterFactory(JacksonConverterFactory.create())
//                .build();
//        return retrofit.create(ComfyuiApi.class);
//    }

    @Bean
    public ComfyuiApi comfyuiApi() throws IOException {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://127.0.0.1:8080")
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ComfyuiApi comfyuiApi = retrofit.create(ComfyuiApi.class);
        return comfyuiApi;
    }

}
