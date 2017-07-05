package com.zdy.retrofitupdate.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.zdy.retrofitupdate.http.HttpClient;
import com.zdy.retrofitupdate.http.HttpClientDownload;
import com.zdy.retrofitupdate.inter.BaseCallback;
import com.zdy.retrofitupdate.model.RequestModel;
import com.zdy.retrofitupdate.model.ResponseModel;
import com.zdy.retrofitupdate.model.VersionModel;
import com.zdy.retrofitupdate.update.UpdateUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created with Android Studio.
 * Time: 18:16  2017/2/27
 * Author:ZhuangYuan
 */
public class UpdateService extends Service {

    private MyBinder myBinder = new MyBinder();

    private BaseCallback<VersionModel> callback;

    private DownLoadCallback downLoadCallback;

    public void setDownLoadCallback(DownLoadCallback downLoadCallback) {
        this.downLoadCallback = downLoadCallback;
    }

    public interface DownLoadCallback {
        void downError(String result);

        void downSuccess(String result, File file);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    //获取是否更新信息
    public void getInfo() {
        try {
            final RequestModel requestModel = new RequestModel();
            requestModel.setCode("110012");
            //如果请求头已有token，这里可以不使用（根据api设计）
//            requestModel.setToken(UtilWithContext.getTocken());
            requestModel.setData(UpdateUtils.getVersionName(getApplicationContext()));
            HttpClient.getHttpRetrofitInstance().requestInfo(requestModel)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<ResponseModel>() {
                        @Override
                        public void call(ResponseModel responseModel) {
                            if ("0".equals(responseModel.getCode())) {
                                VersionModel updateModel = JSONObject.parseObject(responseModel.getData(), VersionModel.class);
                                if (callback != null) {
                                    callback.success(updateModel);
                                }
                            } else {
                                if (callback != null) {
                                    callback.error("", "");
                                }
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            if (callback != null) {
                                callback.error("", "");
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //下载更新包
    public void download(String url, final String fileName) {
        Call<ResponseBody> call = HttpClientDownload.getHttpRetrofitInstance().retrofitDownload(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        InputStream is = response.body().byteStream();
                        File parentFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath(), "apk");
                        if (!parentFile.exists()) {
                            parentFile.mkdir();
                        }
                        File file = new File(parentFile, fileName);
                        FileOutputStream fos = new FileOutputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(is);
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = bis.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                            fos.flush();
                        }
                        fos.close();
                        bis.close();
                        is.close();
                        if (downLoadCallback != null) {
                            downLoadCallback.downSuccess("已下载完成", file);
                        }
                    } else {
                        if (downLoadCallback != null) {
                            downLoadCallback.downError("下载出错了");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (downLoadCallback != null) {
                        downLoadCallback.downError("下载出错了");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (downLoadCallback != null) {
                    downLoadCallback.downError("下载出错了");
                }
            }
        });
    }


    public void setCallback(BaseCallback<VersionModel> callback) {
        this.callback = callback;
    }


    public class MyBinder extends Binder {
        public UpdateService getService() {
            return UpdateService.this;
        }
    }
}
