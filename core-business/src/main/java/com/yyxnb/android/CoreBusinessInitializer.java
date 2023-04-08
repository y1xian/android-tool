package com.yyxnb.android;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import com.yyxnb.android.utils.AppUtil;
import com.yyxnb.android.utils.CollUtil;

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
public class CoreBusinessInitializer implements Initializer<Void> {

	@NonNull
	@Override
	public Void create(@NonNull Context context) {
		Log.e("CoreBusinessInitializer", "第2个初始化的存在");
		AppUtil.init((Application) context.getApplicationContext());
		return null;
	}

	@NonNull
	@Override
	public List<Class<? extends Initializer<?>>> dependencies() {
		return CollUtil.newArrayList(CoreInitializer.class);
	}
}