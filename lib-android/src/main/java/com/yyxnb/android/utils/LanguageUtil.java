package com.yyxnb.android.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * 语言相关
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/4/8
 */
public class LanguageUtil {

	public static final String ENGLISH = "en";
	public static final String CHINESE = "ch";
	public static final String TRADITIONAL_CHINESE = "zh_rTW";

	/**
	 * 获取系统的locale
	 *
	 * @return Locale
	 */
	public static Locale getSystemLocale() {
		Locale locale;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			locale = Resources.getSystem().getConfiguration().getLocales().get(0);
		} else {
			locale = Resources.getSystem().getConfiguration().locale;
		}
		return locale;
	}

	/**
	 * 更改应用语言
	 *
	 * @param context 上下文
	 * @param locale  语言地区
	 */
	public static void changeAppLanguage(Context context, Locale locale) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		Configuration configuration = resources.getConfiguration();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			configuration.setLocale(locale);
			configuration.setLocales(new LocaleList(locale));
			context.createConfigurationContext(configuration);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			configuration.setLocale(locale);
		} else {
			configuration.locale = locale;
		}
		resources.updateConfiguration(configuration, metrics);

	}
}
