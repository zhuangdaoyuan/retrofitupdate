package com.zdy.retrofitupdate.model;


import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/28.
 */
public class RequestModel implements Serializable {

    private String code;

    private Object data;

    private String token;

    public RequestModel() {

    }

    public RequestModel(String code, Object data, String token) {
        this.code = code;
        this.data = data;
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "RequestModel{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", token='" + token + '\'' +
                '}';
    }
}
