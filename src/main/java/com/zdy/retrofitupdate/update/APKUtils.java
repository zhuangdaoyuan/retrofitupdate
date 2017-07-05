package com.zdy.retrofitupdate.update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * apk安装的工具类
 * Created by Administrator on 2016/7/13.
 */
public class APKUtils {

    //安装apk
    public static void installApk(Context context, File file) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            context.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
