package com.yyxnb.android;

import android.content.Context;

import androidx.annotation.NonNull;

import com.yyxnb.android.log.AndroidLog;
import com.yyxnb.android.modules.ILog;
import com.yyxnb.android.modules.IModule;

/**
 * ModuleManager
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/26
 */
public class ModuleManager {
	private static final String TAG = ModuleManager.class.getSimpleName();
	private volatile static ModuleLoader sModuleLoader;

	/**
	 * 全局初始化
	 *
	 * @param context 上下文
	 */
	public static synchronized void init(@NonNull Context context) {
		// 初始化ModuleLoader
		sModuleLoader = new ModuleLoader(context);
		// 先注册本地，最低优先级
		sModuleLoader.registerMetaModule(ILog.class, AndroidLog.class);
		// 再注册meta下的，第二优先级
		sModuleLoader.initMeta();
	}

	/**
	 * 注册模块
	 *
	 * @param clazz     模块接口
	 * @param implClass 实现类
	 * @param <T>       T
	 * @return 注册结果
	 */
	public static synchronized <T extends IModule> boolean registerModule(final Class<T> clazz, final T implClass) {
		return sModuleLoader.registerModule(clazz, implClass);
	}

	/**
	 * 获取模块
	 *
	 * @param clazz 模块接口
	 * @param <T>   T
	 * @return 实例
	 */
	public static synchronized <T extends IModule> T module(final Class<T> clazz) {
		return sModuleLoader.getModule(clazz);
	}

	// ----------------------------------------------- 实现模块

	/**
	 * log
	 *
	 * @return 实现ILog
	 */
	public static ILog log() {
		// 工具之间使用，还未初始化，使用默认log
		if (sModuleLoader == null) {
			return AndroidLog.getInstance();
		} else {
			if (!sModuleLoader.hasRegister(ILog.class)) {
				return AndroidLog.getInstance();
			}
			return module(ILog.class);
		}
	}

}
