package com.yyxnb.android.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DateUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/4/3
 */
public class DateUtil {

	/**
	 * 标准日期格式：yyyy-MM-dd
	 */
	public static final String NORM_DATE_PATTERN = "yyyy-MM-dd";
	/**
	 * 标准日期时间格式，精确到分：yyyy-MM-dd HH:mm
	 */
	public static final String NORM_DATETIME_MINUTE_PATTERN = "yyyy-MM-dd HH:mm";
	/**
	 * 标准日期时间格式，精确到秒：yyyy-MM-dd HH:mm:ss
	 */
	public static final String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 当前时间的时间戳
	 *
	 * @return 时间
	 */
	public static long current() {
		return System.currentTimeMillis();
	}

	/**
	 * 当前时间的时间戳（秒）
	 *
	 * @return 当前时间秒数
	 */
	public static long currentSeconds() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 当前时间，格式 yyyy-MM-dd HH:mm:ss
	 *
	 * @return 当前时间的标准形式字符串
	 */
	public static String now() {
		return formatDateTime(new Date());
	}

	/**
	 * 当前日期，格式 yyyy-MM-dd
	 *
	 * @return 当前日期的标准形式字符串
	 */
	public static String today() {
		return formatDate(new Date());
	}

	/**
	 * 格式化日期时间<br>
	 * 格式 yyyy-MM-dd HH:mm:ss
	 *
	 * @param date 被格式化的日期
	 * @return 格式化后的日期
	 */
	public static String formatDateTime(Date date) {
		if (null == date) {
			return null;
		}
		return new SimpleDateFormat(NORM_DATE_PATTERN).format(date);
	}

	/**
	 * 格式化日期部分（不包括时间）<br>
	 * 格式 yyyy-MM-dd
	 *
	 * @param date 被格式化的日期
	 * @return 格式化后的字符串
	 */
	public static String formatDate(Date date) {
		if (null == date) {
			return null;
		}
		return new SimpleDateFormat(NORM_DATE_PATTERN).format(date);
	}
}
