package com.jairus;

import android.app.Application;
import android.content.Context;


/**
 * 自定义Application
 * 作者： JairusTse
 * 日期： 18/5/15 16:59
 */
public class AppApplication extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}
