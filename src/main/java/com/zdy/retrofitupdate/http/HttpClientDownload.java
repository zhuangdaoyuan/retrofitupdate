package com.zdy.retrofitupdate.http;


import com.zdy.retrofitupdate.download.ProgressHelper;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * Created by zdy on 16/3/1.
 */

//下载不需要使用自定拦截器
public class HttpClientDownload {
    private static HttpRetrofit httpRetrofit;
    protected static final Object monitor = new Object();
    private static Retrofit retrofit;

    private HttpClientDownload() {
    }

    static {
        OkHttpClient.Builder builder = ProgressHelper.addProgress(null);
        retrofit = new Retrofit.Builder()
                .baseUrl(URLUtil.DomainBase)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(builder.build())
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
