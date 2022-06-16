package com.yyxnb.android;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.yyxnb.android.interfaces.ILog;
import com.yyxnb.android.interfaces.IModule;
import com.yyxnb.android.log.LoggerImpl;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * ModuleLoader
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/5/17
 */
public class ModuleLoader {

	private static final String MODULE_KEY_PREFIX = "MODULE.";
	private static final String TAG = ModuleLoader.class.getSimpleName();
	private final Map<Class<? extends IModule>, Class<? extends IModule>> mModuleClasses = new HashMap<>();
	private final Map<Class<? extends IModule>, IModule> mModuleInstances = new HashMap<>();
	private final Context mContext;
	private static long initTime;

	public ModuleLoader(Context mContext) {
		this.mContext = mContext;
		this.mModuleClasses.put(ILog.class, LoggerImpl.class);
	}

	public void init() {
		this.loadModuleConfigs(this.mContext);
		setInitTime(System.currentTimeMillis());
	}

	/**
	 * 获取 <meta name> 的值
	 *
	 * @param context 上下文
	 */
	private void loadModuleConfigs(Context context) {
		try {
			Bundle bundle;
			for (String s : (bundle = context.getPackageManager().getPackageInfo(context.getPackageName(),
					PackageManager.GET_META_DATA).applicationInfo.metaData).keySet()) {
				String value;
				if ((value = s).startsWith(MODULE_KEY_PREFIX)) {
					String key = value.substring(MODULE_KEY_PREFIX.length());
					value = bundle.getString(value).trim();
					this.registerModule(key, value);
				}
			}
		} catch (Exception e) {
			Oh.log().e(e.getMessage());
		}
	}

	private void registerModule(String module, String impl) {
		if (!TextUtils.isEmpty(module) && !TextUtils.isEmpty(impl)) {
			try {
				Class moduleClass = Class.forName(module);
				if (IModule.class.isAssignableFrom(moduleClass)) {
					if (impl != null) {
						impl = impl.trim();
					}
					Class implClass = Class.forName(impl);
					if (moduleClass.isAssignableFrom(implClass)) {
						Oh.log().w("will register module: ".concat(module));
						this.registerModule(moduleClass, implClass);
					}
				}
			} catch (Exception e) {
				Oh.log().e("failed to register module: ".concat(String.valueOf(module)), e);
			}
		} else {
			Oh.log().e("moduleClsName is null");
		}
	}

	public synchronized <T extends IModule> boolean registerModule(Class<? extends T> moduleClass, T t) {
		if (t == null) {
			Oh.log().e("instance is null");
			return false;
		} else {
			this.mModuleInstances.put(moduleClass, t);
			return true;
		}
	}

	public synchronized <T extends IModule> boolean registerModule(Class<T> moduleClass, Class<? extends T> implClass) {
		if (implClass == null) {
			Oh.log().e("implClass is null");
			return false;
		} else if (this.mModuleClasses.get(moduleClass) == null) {
			this.mModuleClasses.put(moduleClass, implClass);
			return true;
		} else {
			Oh.log().w("module <" + moduleClass.getName() + "> is already registered as <" + (this.mModuleClasses.get(moduleClass)).getName() + ">, ignore: " + implClass.getName());
			return false;
		}
	}

	public synchronized <T extends IModule> T getModule(Class<T> tClass) {
		T implClass = (T) mModuleInstances.get(tClass);
		Class<?> moduleClass = mModuleClasses.get(tClass);
		if (implClass == null && moduleClass != null) {
			try {
				try {
					implClass = (T) moduleClass.getConstructor(Context.class).newInstance(this.mContext);
				} catch (Exception e) {
					implClass = (T) moduleClass.getConstructor().newInstance();
				}
				this.registerModule(tClass, implClass);
			} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
				Oh.log().e("Failed to init module <" + tClass.getName() + ">, constructor <init>(Context context) may not be defined or is not public", e);
			}
		}
		return implClass;
	}

	public static long getInitTime() {
		return initTime;
	}

	public static void setInitTime(long time) {
		initTime = time;
	}
}
