package com.zdy.retrofitupdate.update;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Time: 15:37  2017/6/14
 * Author:ZhuangYuan
 */

public class CheckModel implements Serializable {

    //自动检测更新（不需要提示）
    private boolean autoCheck = false;
    //主动检测更新（需要提示）
    private boolean toCheck = false;


    public boolean isAutoCheck() {
        return autoCheck;
    }

    public void setAutoCheck(boolean autoCheck) {
        this.autoCheck = autoCheck;
    }

    public boolean isToCheck() {
        return toCheck;
    }

    public void setToCheck(boolean toCheck) {
        this.toCheck = toCheck;
    }
}
