package com.yyxnb.oh.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.startup.Initializer;

import java.util.Collections;
import java.util.List;

/**
 * 使用ContentProvider初始化三方库
 *
 * @author yyx
 */
public class AppInitializer implements Initializer<Void> {

    @NonNull
    @Override
    public Void create(@NonNull Context context) {
        Log.e("AppInitializer", "AppInitializer初始化");
        ApplicationUtils.init((Application) context);
        // 注册生命周期
        ApplicationManager.getInstance().init(ApplicationUtils.getApp());
        // 突破65535的限制
        MultiDex.install(context);
        return null;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }
}
