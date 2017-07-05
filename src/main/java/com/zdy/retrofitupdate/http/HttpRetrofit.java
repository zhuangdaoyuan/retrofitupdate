package com.zdy.retrofitupdate.http;


import com.zdy.retrofitupdate.model.RequestModel;
import com.zdy.retrofitupdate.model.ResponseModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;


/**
 * get data
 * Created by xybcoder on 2016/3/1.
 */
public interface HttpRetrofit {

    /**
     * @POST 请求方式post
     * @Body 表示将requestBean对象转成成json string作为参数传递给后台
     */
    @GET
    Call<ResponseBody> retrofitDownload(@Url String url);

    /*相对路径*/
    @POST("/dct-web/kernel")
    Call<ResponseModel> refreshToken(@Body RequestModel requestModel);

    @POST("/dct-web/kernel")
    Observable<ResponseModel> requestInfo(@Body RequestModel body);
}
