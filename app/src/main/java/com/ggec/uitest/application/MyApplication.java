package com.ggec.uitest.application;

import android.app.Application;

/**
 * Created by ggec on 2018/9/14.
 */

public class MyApplication extends Application {
    private static MyApplication myApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }

    synchronized public static MyApplication getMyApplication() {
        return myApplication;
    }
}
