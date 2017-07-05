package com.zdy.retrofitupdate;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.zdy.retrofitupdate.download.DownloadProgressHandler;
import com.zdy.retrofitupdate.download.ProgressHelper;
import com.zdy.retrofitupdate.inter.BaseCallback;
import com.zdy.retrofitupdate.model.VersionModel;
import com.zdy.retrofitupdate.service.UpdateService;
import com.zdy.retrofitupdate.update.APKUtils;
import com.zdy.retrofitupdate.update.UpdateUtils;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    private ServiceConnection serviceConnection;
    private ProgressDialog updateProgress;
    private UpdateService updateService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkUpdate();
    }

    private void checkUpdate() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                UpdateService.MyBinder myBinder = (UpdateService.MyBinder) service;
                updateService = myBinder.getService();
                updateService.setCallback(new BaseCallback<VersionModel>() {
                    @Override
                    public void success(VersionModel updateModel) {
                        updateApp(updateModel);
                    }

                    @Override
                    public void error(String code, String msg) {
                    }
                });
                updateService.getInfo();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        Intent intent = new Intent(this, UpdateService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void updateApp(final VersionModel updateModel) {
        if (updateModel == null)
            return;
        boolean isUpdate = UpdateUtils.isUpdate(getApplicationContext(), updateModel.getVersion());
        if (!isUpdate) {
            return;
        }
        if (!updateModel.getIsUpdate()) {
            return;
        }
        UpdateUtils.showDialog(MainActivity.this, updateModel.getVersion(), updateModel.getLog(), updateModel.getIsForce(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (updateProgress == null) {
                    updateProgress = UpdateUtils.createProgressDialog(MainActivity.this);
                }
                String path = updateModel.getApkUrl();
                String fileName = path.substring(path.lastIndexOf('/') + 1);
                ProgressHelper.setProgressHandler(new DownloadProgressHandler() {
                    @Override
                    protected void onProgress(long bytesRead, long contentLength, boolean done) {
                        updateProgress.setMax((int) (contentLength / 1024));
                        updateProgress.setProgress((int) (bytesRead / 1024));
                    }
                });
                updateProgress.show();
                if (updateService != null) {
                    updateService.download(path, fileName);
                    updateService.setDownLoadCallback(new UpdateService.DownLoadCallback() {
                        @Override
                        public void downError(String result) {
                            showError(result);
                        }

                        @Override
                        public void downSuccess(String result, File file) {
                            APKUtils.installApk(MainActivity.this, file);
                            showError(result);
                        }
                    });
                }
            }
        });
    }


    private void showError(String msg) {
        updateProgress.dismiss();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
