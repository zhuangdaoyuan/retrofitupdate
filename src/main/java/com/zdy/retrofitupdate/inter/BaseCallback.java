package com.zdy.retrofitupdate.inter;

/**
 * created by szw at 2017/1/18
 */
public interface BaseCallback<T> {

    void success(T t);

    void error(String code, String msg);
}
