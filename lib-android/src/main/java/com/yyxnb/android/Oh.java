package com.yyxnb.android;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.yyxnb.android.app.AppUtil;
import com.yyxnb.android.interfaces.ILog;
import com.yyxnb.android.interfaces.IModule;
import com.yyxnb.android.log.AndroidLog;
import com.yyxnb.android.utils.LifecycleUtil;

/**
 * 全局配置
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/5/17
 */
public class Oh {

	private static final String TAG = Oh.class.getSimpleName();
	private static Context mContext;
	private static ModuleLoader sModuleLoader;

	private Oh() {
	}

	/**
	 * 全局初始化
	 *
	 * @param context 上下文
	 */
	public static synchronized void init(@NonNull Context context) {
		mContext = context;
		AppUtil.init((Application) context);
		(sModuleLoader = new ModuleLoader(context)).init();

		// 注册生命周期
		LifecycleUtil.setLifecycleCallbacks(AppUtil.getApp());
	}

	public static synchronized <T extends IModule> boolean registerModule(Class<T> clazz, T t) {
		return sModuleLoader.registerModule(clazz, t);
	}

	public static <T extends IModule> T module(Class<T> clazz) {
		return sModuleLoader.getModule(clazz);
	}

	// -----------------------------------------------------------------------

	public static ILog log() {
		if (sModuleLoader == null) {
			return AndroidLog.getInstance();
		} else {
			return module(ILog.class);
		}
	}


}
