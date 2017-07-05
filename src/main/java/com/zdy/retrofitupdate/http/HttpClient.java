package com.zdy.retrofitupdate.http;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * Created by zdy on 16/3/1.
 * 使用自定拦截器
 */
public class HttpClient {
    private static HttpRetrofit httpRetrofit;
    protected static final Object monitor = new Object();
    private static Retrofit retrofit;
    private static OkHttpClient okHttpClient;

    private HttpClient() {
    }

    static {
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new TokenInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(URLUtil.DomainBase)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static HttpRetrofit getHttpRetrofitInstance() {
        synchronized (monitor) {
            if (httpRetrofit == null) {
                httpRetrofit = retrofit.create(HttpRetrofit.class);
            }
            return httpRetrofit;
        }
    }

    public <T> T getHttpRetrofitInstance(Class<T> tClass) {
        synchronized (monitor) {
            if (httpRetrofit == null) {
                httpRetrofit = retrofit.create(HttpRetrofit.class);
            }
            return retrofit.create(tClass);
        }
    }

}
