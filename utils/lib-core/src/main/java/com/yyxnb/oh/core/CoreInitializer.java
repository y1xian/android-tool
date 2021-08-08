package com.yyxnb.oh.core;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.startup.Initializer;

import com.yyxnb.oh.app.AppUtils;

import java.util.Collections;
import java.util.List;

/**
 * 使用ContentProvider初始化三方库
 *
 * @author yyx
 */
public class CoreInitializer implements Initializer<Void> {

    @NonNull
    @Override
    public Void create(@NonNull Context context) {
        Log.w("What", "  \n\n\n\n\n\n\n\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t _          __  _   _       ___   _____  \n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t| |        / / | | | |     /   | |_   _| \n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t| |  __   / /  | |_| |    / /| |   | |   \n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t| | /  | / /   |  _  |   / / | |   | |   \n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t| |/   |/ /    | | | |  / /  | |   | |   \n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t|___/|___/     |_| |_| /_/   |_|   |_|   \n" +
                "  \n\n\n\n\n\n ");
        Log.e("CoreInitializer", "第一个初始化的存在");
        AppUtils.init((Application) context);

        CoreManager.getInstance().init(AppUtils.getApp());

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
