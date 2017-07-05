package com.zdy.retrofitupdate.update;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;


/**
 * 检查更新的工具类
 * Created by Administrator on 2016/7/13.
 */
public class UpdateUtils {

    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo;
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
            versionName = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 判断是否更新
     *
     * @param context
     * @param versionName 从服务器获取到的版本名
     * @return
     */
    public static boolean isUpdate(Context context, String versionName) {
        if (TextUtils.isEmpty(versionName)) {
            return false;
        }
        try {
            String[] versions = versionName.split("\\.");
            String currentVersionName = getVersionName(context);
            String[] curVersions = currentVersionName.split("\\.");
            for (int i = 0; i < versions.length; i++) {
                int newV = Integer.parseInt(versions[i]);
                int oldV = Integer.parseInt(curVersions[i]);
                if (newV == oldV) {
                    continue;
                } else if (newV < oldV) {
                    return false;
                } else if (newV > oldV) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void showDialog(Context context, String versionName, String update_log, boolean force, DialogInterface.OnClickListener positive_click) {
        showDialog(context, versionName, update_log, force, positive_click, true);
    }

    public static void showDialog(final Context context, String versionName, String update_log, boolean force, DialogInterface.OnClickListener positive_click, boolean restrict) {
        //   restrict 是否限制提示次数
        String title;
        if (restrict) {
            title = "(发现新版本" + versionName + ")";
        } else {
            title = "检测到新版本(" + versionName + ")";
        }

//        if (!force && restrict) {
//            long menuSaveTime = UtilSetting.getInstance(context).getTime(versionName);
//            //3小时内不重复提示
//            if (System.currentTimeMillis() - menuSaveTime <= 10800000) {
//                return;
//            }
//        }
//        UtilSetting.getInstance(context).saveTime(versionName, System.currentTimeMillis());
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(update_log)
                .setPositiveButton("立即下载", positive_click)
                .create();
        if (force) {
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        } else {
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        dialog.setCancelable(false);
        dialog.show();
    }

    //创建更新提示框
    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle("");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setMax(100);
        return dialog;
    }
}
