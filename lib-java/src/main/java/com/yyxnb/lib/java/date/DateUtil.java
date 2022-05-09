package com.yyxnb.lib.java.date;

import java.text.SimpleDateFormat;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * 时间工具类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/3/25
 */
public class DateUtil extends CalendarUtil {

	/**
	 * 日期格式：yyyy-MM-dd HH:mm:ss
	 **/
	public static final String DF_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 日期格式：yyyy-MM-dd HH:mm
	 **/
	public static final String DF_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	/**
	 * 日期格式：yyyy-MM-dd
	 **/
	public static final String DF_YYYY_MM_DD = "yyyy-MM-dd";
	/**
	 * 日期格式：HH:mm:ss
	 **/
	public static final String DF_HH_MM_SS = "HH:mm:ss";
	/**
	 * 日期格式：HH:mm
	 **/
	public static final String DF_HH_MM = "HH:mm";


	/**
	 * 当前时间，转换为{@link DateTime}对象
	 *
	 * @return 当前时间
	 */
	public static DateTime date() {
		return new DateTime();
	}


	/**
	 * {@link TemporalAccessor}类型时间转为{@link DateTime}<br>
	 * 始终根据已有{@link TemporalAccessor} 产生新的{@link DateTime}对象
	 *
	 * @param temporalAccessor {@link TemporalAccessor},常用子类： {@link LocalDateTime}、 LocalDate
	 * @return 时间对象
	 */
//	public static DateTime date(TemporalAccessor temporalAccessor) {
//		return new DateTime(temporalAccessor);
//	}

	/**
	 * Long类型时间转为{@link DateTime}<br>
	 * 只支持毫秒级别时间戳，如果需要秒级别时间戳，请自行×1000
	 *
	 * @param date Long类型Date（Unix时间戳）
	 * @return 时间对象
	 */
	public static DateTime date(long date) {
		return new DateTime(date);
	}

	/**
	 * {@link Calendar}类型时间转为{@link DateTime}<br>
	 * 始终根据已有{@link Calendar} 产生新的{@link DateTime}对象
	 *
	 * @param calendar {@link Calendar}
	 * @return 时间对象
	 */
	public static DateTime date(Calendar calendar) {
		return new DateTime(calendar);
	}

	/**
	 * 当前时间，转换为{@link DateTime}对象，忽略毫秒部分
	 *
	 * @return 当前时间
	 */
	public static DateTime dateSecond() {
		return beginOfSecond(date());
	}

	/**
	 * 根据已有{@link Date} 产生新的{@link DateTime}对象
	 *
	 * @param date Date对象
	 * @return {@link DateTime}对象
	 */
	public static DateTime dateNew(Date date) {
		return new DateTime(date);
	}

	// ----------------------------------------------------------------------- format

	/**
	 * 格式化日期间隔输出，精确到毫秒
	 *
	 * @param betweenMs 日期间隔
	 * @return XX天XX小时XX分XX秒XX毫秒
	 */
	public static String formatBetween(long betweenMs) {
		return new BetweenFormatter(betweenMs, BetweenFormatter.Level.MILLISECOND).format();
	}

	/**
	 * 创建{@link SimpleDateFormat}，注意此对象非线程安全！<br>
	 * 此对象默认为严格格式模式，即parse时如果格式不正确会报错。
	 *
	 * @param pattern 表达式
	 * @return {@link SimpleDateFormat}
	 */
	public static SimpleDateFormat newSimpleFormat(String pattern) {
		return newSimpleFormat(pattern, null, null);
	}

	/**
	 * 创建{@link SimpleDateFormat}，注意此对象非线程安全！<br>
	 * 此对象默认为严格格式模式，即parse时如果格式不正确会报错。
	 *
	 * @param pattern  表达式
	 * @param locale   {@link Locale}，{@code null}表示默认
	 * @param timeZone {@link TimeZone}，{@code null}表示默认
	 * @return {@link SimpleDateFormat}
	 */
	public static SimpleDateFormat newSimpleFormat(String pattern, Locale locale, TimeZone timeZone) {
		if (null == locale) {
			locale = Locale.getDefault(Locale.Category.FORMAT);
		}
		final SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
		if (null != timeZone) {
			format.setTimeZone(timeZone);
		}
		format.setLenient(false);
		return format;
	}

//	/**
//	 * 根据特定格式格式化日期
//	 *
//	 * @param date   被格式化的日期
//	 * @param format 日期格式，常用格式见： {@link DatePattern} {@link DatePattern#NORM_DATETIME_PATTERN}
//	 * @return 格式化后的字符串
//	 */
//	public static String format(Date date, String format) {
//		if (null == date || StrUtil.isBlank(format)) {
//			return null;
//		}
//
//		// 检查自定义格式
//		if (GlobalCustomFormat.isCustomFormat(format)) {
//			return GlobalCustomFormat.format(date, format);
//		}
//
//		TimeZone timeZone = null;
//		if (date instanceof DateTime) {
//			timeZone = ((DateTime) date).getTimeZone();
//		}
//		return format(date, newSimpleFormat(format, null, timeZone));
//	}

	// -----------------------------------------------------------------------

	/**
	 * 获取时长单位简写
	 *
	 * @param unit 单位
	 * @return 单位简写名称
	 */
	public static String getShotName(TimeUnit unit) {
		switch (unit) {
			case NANOSECONDS:
				return "ns";
			case MICROSECONDS:
				return "μs";
			case MILLISECONDS:
				return "ms";
			case SECONDS:
				return "s";
			case MINUTES:
				return "min";
			case HOURS:
				return "h";
			default:
				return unit.name().toLowerCase();
		}
	}

	/**
	 * 纳秒转毫秒
	 *
	 * @param duration 时长
	 * @return 时长毫秒
	 */
	public static long nanosToMillis(long duration) {
		return TimeUnit.NANOSECONDS.toMillis(duration);
	}

	/**
	 * 纳秒转秒，保留小数
	 *
	 * @param duration 时长
	 * @return 秒
	 */
	public static double nanosToSeconds(long duration) {
		return duration / 1_000_000_000.0;
	}

	// ----------------------------------------------------------------------- Offset for now

	/**
	 * 昨天
	 *
	 * @return 昨天
	 */
	public static DateTime yesterday() {
		return offsetDay(new DateTime(), -1);
	}

	/**
	 * 明天
	 *
	 * @return 明天
	 */
	public static DateTime tomorrow() {
		return offsetDay(new DateTime(), 1);
	}

	/**
	 * 偏移天
	 *
	 * @param date   日期
	 * @param offset 偏移天数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsetDay(Date date, int offset) {
		return offset(date, DateField.DAY_OF_YEAR, offset);
	}

	/**
	 * 获取指定日期偏移指定时间后的时间，生成的偏移日期不影响原日期
	 *
	 * @param date      基准日期
	 * @param dateField 偏移的粒度大小（小时、天、月等）{@link DateField}
	 * @param offset    偏移量，正数为向后偏移，负数为向前偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offset(Date date, DateField dateField, int offset) {
		return dateNew(date).offset(dateField, offset);
	}

	// -----------------------------------------------------------------------

}
