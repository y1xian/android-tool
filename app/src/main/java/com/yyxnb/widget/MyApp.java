package com.yyxnb.widget;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
    }
}
