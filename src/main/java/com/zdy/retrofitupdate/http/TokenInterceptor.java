package com.zdy.retrofitupdate.http;

import android.accounts.NetworkErrorException;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.zdy.retrofitupdate.model.AccessTokenDTO;
import com.zdy.retrofitupdate.model.RequestModel;
import com.zdy.retrofitupdate.model.ResponseModel;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

public class TokenInterceptor implements Interceptor {
    private long save = 0;
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        try {
            Log.e("URL", request.url().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Response originalResponse = null;
        originalResponse = dealChain(0, chain, request);
        if (originalResponse == null) {
            throw new NullPointerException();
        }
        if (originalResponse.code() != 200) {
            throw new SocketException();
        }

        /**
         * 通过如下的办法曲线取到请求完成的数据
         * 原本想通过  originalResponse.body().string() 去取到请求完成的数据,但是一直报错,不知道是okhttp的bug还是操作不当
         * 然后去看了okhttp的源码,找到了这个曲线方法,取到请求完成的数据后,根据特定的判断条件去判断token过期
         */
        ResponseModel responseModel = getResponseModel(originalResponse);

        if ("001008".equals(responseModel.getCode())) {
            synchronized (TokenInterceptor.class) {
                //拿到就token
                String refreshToken = "oldtoken";
                long cur = System.currentTimeMillis();
                Log.e("Token", refreshToken + "|" + Math.abs(cur - save));
                if (Math.abs(cur - save) < 10 * 1000) {
                    //10秒内不需要重新更新token
                    Log.e("Token--0", refreshToken + "|");
                    Request requestNew = request.newBuilder().header("token", refreshToken).build();
                    originalResponse.body().close();
                    return dealChain(1, chain, requestNew);
                } else {
                    Log.e("Token---1", refreshToken);
                    //目的是避免循环
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(URLUtil.DomainBase)
                            .addConverterFactory(FastJsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(new OkHttpClient())
                            .build();
                    //根据后台设计更改请求方式
                    RequestModel requestModel = new RequestModel("APICode", refreshToken, refreshToken);
                    Call<ResponseModel> call = retrofit.create(HttpRetrofit.class).refreshToken(requestModel);
                    call.request().newBuilder().header("token", refreshToken);
                    //存在危险（出现异常）
                    ResponseModel newToken = call.execute().body();
                    if (responseSuccess(newToken)) {
                        save = System.currentTimeMillis();
                        //解析返回值
                        AccessTokenDTO accessTokenDTO = JSONObject.parseObject(newToken.getData(), AccessTokenDTO.class);
                        //存入新token
//                        UtilWithContext.setTocken(accessTokenDTO.getAccessTok());
                        //使用新token继续请求
                        Request newRequest = request.newBuilder().header("token", accessTokenDTO.getAccessTok()).build();
                        originalResponse.body().close();
                        return dealChain(2, chain, newRequest);
                    } else {
                        //token失效处理---退回或进入登录页
//                        EventBus.getDefault().post("toLogin");
                    }
                }
            }
        }
        return originalResponse;
    }

    //处理返回值
    private boolean responseSuccess(ResponseModel response) {
        return true;
    }

    //不会再进入拦截器里（所以不会再进入加锁代码块中）
    private Response dealChain(int check, Chain chain, Request request) throws IOException {
        Response response = null;
        try {
//            String refreshToken = UtilWithContext.getTocken();
            String refreshToken = "";//当前token
            Request requestNew = request.newBuilder().header("token", refreshToken).build();
            response = chain.proceed(requestNew);
            if (check == 1) {
                ResponseModel responseModel = getResponseModel(response);
                if ("001008".equals(responseModel.getCode())) {
                    getResponseModel(chain.proceed(request));
                }
            } else if (check == 2) {
                ResponseModel responseModel = getResponseModel(response);
                if ("001008".equals(responseModel.getCode())) {
//                    EventBus.getDefault().post("toLogin");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof ConnectException) {
                throw new ConnectException();
            } else if (e instanceof NetworkErrorException) {
                try {
                    throw new NetworkErrorException();
                } catch (NetworkErrorException e1) {
                    e1.printStackTrace();
                }
            } else if (e instanceof SocketException) {
                throw new SocketException();
            } else if (e instanceof UnknownHostException) {
                throw new UnknownHostException();
            } else if (e instanceof SocketTimeoutException) {
                throw new SocketTimeoutException();
            } else {
                throw new NullPointerException();
            }
        }
        if (response == null) {
            throw new NullPointerException();
        }
        return response;
    }


    private ResponseModel getResponseModel(Response response) throws IOException {
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }
        String bodyString = buffer.clone().readString(charset);
        if (bodyString.length() > 2000) {
            for (int i = 0; i < bodyString.length(); i += 2000) {
                if (i + 2000 < bodyString.length())
                    Log.e("Response" + i, bodyString.substring(i, i + 2000));
                else
                    Log.e("Response" + i, bodyString.substring(i, bodyString.length()));
            }
        } else {
            Log.e("Response", bodyString);
        }
        return JSONObject.parseObject(bodyString, ResponseModel.class);
    }

}