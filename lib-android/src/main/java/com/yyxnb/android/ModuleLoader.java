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
	 * @param var1
	 */
	private void loadModuleConfigs(Context var1) {
		try {
			Bundle var6;

			for (String s : (var6 = var1.getPackageManager().getPackageInfo(var1.getPackageName(),
					PackageManager.GET_META_DATA).applicationInfo.metaData).keySet()) {
				String var3;
				if ((var3 = s).startsWith(MODULE_KEY_PREFIX)) {
					String var4 = var3.substring(MODULE_KEY_PREFIX.length());
					var3 = var6.getString(var3).trim();
					this.registerModule(var4, var3);
				}
			}

		} catch (Exception var5) {
			Oh.log().e(var5.getMessage());
		}
	}

	private void registerModule(String var1, String var2) {
		if (!TextUtils.isEmpty(var1) && !TextUtils.isEmpty(var2)) {
			try {
				Class var3 = Class.forName(var1);
				if (IModule.class.isAssignableFrom(var3)) {
					if (var2 != null) {
						var2 = var2.trim();
					}

					Class var5 = Class.forName(var2);
					if (var3.isAssignableFrom(var5)) {
						Oh.log().w("will register module: ".concat(String.valueOf(var1)));
						this.registerModule(var3, var5);
					}
				}

			} catch (Exception var4) {
				Oh.log().e("failed to register module: ".concat(String.valueOf(var1)), var4);
			}
		} else {
			Oh.log().e("moduleClsName is null");
		}
	}

	public synchronized <T extends IModule> boolean registerModule(Class<? extends T> var1, T var2) {
		if (var2 == null) {
			Oh.log().e("instance is null");
			return false;
		} else {
			this.mModuleInstances.put(var1, var2);
			return true;
		}
	}

	public synchronized <T extends IModule> boolean registerModule(Class<T> var1, Class<? extends T> var2) {
		if (var2 == null) {
			Oh.log().e("implClass is null");
			return false;
		} else if (this.mModuleClasses.get(var1) == null) {
			this.mModuleClasses.put(var1, var2);
			return true;
		} else {
			Oh.log().w("module <" + var1.getName() + "> is already registered as <" + (this.mModuleClasses.get(var1)).getName() + ">, ignore: " + var2.getName());
			return false;
		}
	}

	public synchronized <T extends IModule> T getModule(Class<T> var1) {
		T var2 = (T) mModuleInstances.get(var1);
		Class<?> var3 = mModuleClasses.get(var1);
		if (var2 == null && var3 != null) {
			try {
				try {
					var2 = (T) var3.getConstructor(Context.class).newInstance(this.mContext);
				} catch (Exception e) {
					var2 = (T) var3.getConstructor().newInstance();
				}
				this.registerModule(var1, var2);
			} catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException var4) {
				Oh.log().e("Failed to init module <" + var1.getName() + ">, constructor <init>(Context context) may not be defined or is not public", var4);
			}
		}
		return var2;
	}

	public static long getInitTime() {
		return initTime;
	}

	public static void setInitTime(long time) {
		initTime = time;
	}
}
