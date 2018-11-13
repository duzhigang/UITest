package com.ggec.uitest.application;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;

/**
 * Created by ggec on 2018/9/14.
 */

public class MyApplication extends Application {
    private static MyApplication myApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                ))
                .commit();

    }

    synchronized public static MyApplication getMyApplication() {
        return myApplication;
    }
}
