package com.yyxnb.android;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.yyxnb.android.constant.BuildVersion;
import com.yyxnb.android.modules.IModule;
import com.yyxnb.android.utils.StrUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * ModuleLoader
 *
 * <pre>
 *     模块接口实现 {@link com.yyxnb.android.modules.IModule }
 *     1.优先初始化本地
 *     2.再初始化模块下的注册的meta {@link #metaModuleConfigs}
 *     3.最后再初始化手动注册的 {@link #registerModule }
 * </pre>
 *
 * @author yyx
 * @date 2022/5/17
 */
final class ModuleLoader {

	private static final String MODULE_KEY_PREFIX = "Module.";
	/**
	 * 本地 & meta 注册
	 */
	private final Map<Class<? extends IModule>, Class<? extends IModule>> mLoadModuleClasses = new HashMap<>();
	/**
	 * 动态注册，已经实例化
	 */
	private final Map<Class<? extends IModule>, IModule> mModuleInstances = new HashMap<>();
	private final Context mContext;
	private static long initTime;

	public ModuleLoader(Context mContext) {
		this.mContext = mContext;
	}

	public synchronized void initMeta() {
		this.metaModuleConfigs(this.mContext);
	}

	/**
	 * 判断是否已注册
	 *
	 * @param moduleClass 继承接口 {@link IModule}
	 * @param <T>         继承IModule
	 * @return {@code true} or {@code false}
	 */
	public <T extends IModule> boolean hasRegister(Class<? extends T> moduleClass) {
		return mModuleInstances.containsKey(moduleClass) || mLoadModuleClasses.containsKey(moduleClass);
	}

	// ----------------------------------------------- 模块<meta>注册

	/**
	 * 读取<application>中的<meta-data>
	 *
	 * @param context 上下文
	 */
	private synchronized void metaModuleConfigs(Context context) {
		try {
			Bundle bundle;
			if (BuildVersion.isOver13()) {
				bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(),
						PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA)
				).metaData;
			} else {
				bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(),
						PackageManager.GET_META_DATA
				).metaData;
			}
			for (String s : bundle.keySet()) {
				String value;
				if ((value = s).startsWith(MODULE_KEY_PREFIX)) {
					String key = value.substring(MODULE_KEY_PREFIX.length());
					value = bundle.getString(value).trim();
					this.registerMetaModule(key, value);
				}
			}
		} catch (Exception e) {
			ModuleManager.log().e(e);
		}
	}

	/**
	 * 从 AndroidManifest.xml 里的 <meta name="{@link #MODULE_KEY_PREFIX .完整路径 value="实现类完整路径"}"></>
	 * <pre>
	 *     <meta
	 *     name="{@link  #MODULE_KEY_PREFIX}.继承IModule的接口完整路径"
	 *     value="接口实现类完整路径"
	 *     />
	 *     例如：
	 *     <meta
	 *     name="Module.com.yyxnb.android.modules.ILog"
	 *     value="com.yyxnb.android.log.AndroidLog"
	 *     />
	 * </pre>
	 * 解析注册
	 *
	 * @param module 继承接口 {@link IModule}
	 * @param impl   实现类
	 */
	private synchronized void registerMetaModule(@NonNull String module, @NonNull String impl) {
		if (!StrUtil.isAllBlank(module, impl)) {
			try {
				Class moduleClass = Class.forName(module);
				if (IModule.class.isAssignableFrom(moduleClass)) {
					impl = impl.trim();
					Class implClass = Class.forName(Objects.requireNonNull(impl));
					if (moduleClass.isAssignableFrom(implClass)) {
						ModuleManager.log().w("will register module: ".concat(module));
						this.registerMetaModule(moduleClass, implClass);
					}
				}
			} catch (Exception e) {
				ModuleManager.log().e("failed to register module: ".concat(module), e);
			}
		} else {
			ModuleManager.log().e("moduleClsName is null");
		}
	}

	/**
	 * 本地注册
	 *
	 * @param moduleClass 继承接口 {@link IModule}
	 * @param implClass   实现类
	 * @param <T>         是否注册成功
	 */
	public synchronized <T extends IModule> void registerMetaModule(
			@NonNull Class<? extends T> moduleClass, @NonNull Class<? extends T> implClass) {
		this.mLoadModuleClasses.put(moduleClass, implClass);
	}

	// ----------------------------------------------- 动态注册

	/**
	 * 动态注册
	 *
	 * @param moduleClass 继承接口 {@link IModule}
	 * @param implClass   实现类
	 * @param <T>         extends IModule
	 * @return 是否注册成功
	 */
	public synchronized <T extends IModule> boolean registerModule(Class<? extends T> moduleClass, T implClass) {
		if (implClass == null) {
			ModuleManager.log().e("instance is null");
			return false;
		} else {
			this.mModuleInstances.put(moduleClass, implClass);
			return true;
		}
	}


	/**
	 * 获取实例
	 *
	 * @param moduleClass 继承接口 {@link IModule}
	 * @param <T>         extends IModule
	 * @return 实现类
	 */
	public synchronized <T extends IModule> T getModule(@NonNull Class<T> moduleClass) {
		// 先从动态获取
		T implClass = (T) mModuleInstances.get(moduleClass);
		// 再从本地获取
		Class<?> localImplClass = mLoadModuleClasses.get(moduleClass);
		if (implClass == null && localImplClass != null) {
			try {
				Constructor<?> constructor = localImplClass.getConstructor();
				constructor.setAccessible(true);
				implClass = (T) constructor.newInstance();
				this.registerModule(moduleClass, implClass);
			} catch (InvocationTargetException | NoSuchMethodException
					 | IllegalAccessException | InstantiationException e) {
				ModuleManager.log().e("Failed to init module <" + moduleClass.getName() + ">" +
						", constructor <init>() may not be defined or is not public", e);
			}
		}
		return implClass;
	}

}
