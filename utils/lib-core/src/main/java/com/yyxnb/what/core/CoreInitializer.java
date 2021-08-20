package com.yyxnb.what.core;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import com.yyxnb.what.application.AppInitializer;

import java.util.List;

import cn.hutool.core.collection.CollUtil;

/**
 * 使用ContentProvider初始化三方库
 *
 * @author yyx
 */
public class CoreInitializer implements Initializer<Void> {

    @NonNull
    @Override
    public Void create(@NonNull Context context) {
        Log.e("CoreInitializer", "CoreInitializer初始化");

        return null;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return CollUtil.list(true, AppInitializer.class);
    }
}
