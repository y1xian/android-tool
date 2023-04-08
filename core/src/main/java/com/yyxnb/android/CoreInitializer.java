package com.yyxnb.android;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.startup.Initializer;

import java.util.Collections;
import java.util.List;

/**
 * CoreInitializer
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/4/8
 */
public class CoreInitializer implements Initializer<Void> {

	@NonNull
	@Override
	public Void create(@NonNull Context context) {
		Log.e("CoreInitializer", "第1个初始化的存在");
		ModuleManager.init(context);
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