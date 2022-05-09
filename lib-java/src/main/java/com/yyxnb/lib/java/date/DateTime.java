package com.yyxnb.lib.java.date;

import com.yyxnb.lib.java.ObjectUtil;

import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 包装java.util.Date
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/3/25
 */
public class DateTime extends Date {

	private static final long serialVersionUID = -5395712593979185936L;

	/**
	 * 是否可变对象
	 */
	private boolean mutable = true;
	/**
	 * 一周的第一天，默认是周一， 在设置或获得 WEEK_OF_MONTH 或 WEEK_OF_YEAR 字段时，Calendar 必须确定一个月或一年的第一个星期，以此作为参考点。
	 */
	private Week firstDayOfWeek = Week.MONDAY;
	/**
	 * 时区
	 */
	private TimeZone timeZone;
	/**
	 * 第一周最少天数
	 */
	private int minimalDaysInFirstWeek;

	/**
	 * 转换时间戳为 DateTime
	 *
	 * @param timeMillis 时间戳，毫秒数
	 * @return DateTime
	 */
	public static DateTime of(long timeMillis) {
		return new DateTime(timeMillis);
	}

	/**
	 * 转换JDK date为 DateTime
	 *
	 * @param date JDK Date
	 * @return DateTime
	 */
	public static DateTime of(Date date) {
		if (date instanceof DateTime) {
			return (DateTime) date;
		}
		return new DateTime(date);
	}

	/**
	 * 转换 {@link Calendar} 为 DateTime
	 *
	 * @param calendar {@link Calendar}
	 * @return DateTime
	 */
	public static DateTime of(Calendar calendar) {
		return new DateTime(calendar);
	}

	/**
	 * 当前时间
	 */
	public DateTime() {
		this(TimeZone.getDefault());
	}

	/**
	 * 给定日期的构造
	 *
	 * @param calendar {@link Calendar}
	 */
	public DateTime(Calendar calendar) {
		this(calendar.getTime(), calendar.getTimeZone());
		this.setFirstDayOfWeek(Week.of(calendar.getFirstDayOfWeek()));
	}

	/**
	 * 给定日期的构造
	 *
	 * @param date 日期
	 */
	public DateTime(Date date) {
		this(date.getTime(), (date instanceof DateTime) ? ((DateTime) date).timeZone : TimeZone.getDefault()
		);
	}

	/**
	 * 给定日期的构造
	 *
	 * @param date     日期
	 * @param timeZone 时区
	 */
	public DateTime(Date date, TimeZone timeZone) {
		this(ObjectUtil.defaultIfNull(date, Date::new).getTime(), timeZone);
	}

	/**
	 * 当前时间
	 *
	 * @param timeZone 时区
	 */
	public DateTime(TimeZone timeZone) {
		this(System.currentTimeMillis(), timeZone);
	}

	/**
	 * 给定日期毫秒数的构造
	 *
	 * @param timeMillis 日期毫秒数
	 */
	public DateTime(long timeMillis) {
		this(timeMillis, TimeZone.getDefault());
	}

	/**
	 * 给定日期毫秒数的构造
	 *
	 * @param timeMillis 日期毫秒数
	 * @param timeZone   时区
	 */
	public DateTime(long timeMillis, TimeZone timeZone) {
		super(timeMillis);
		this.timeZone = ObjectUtil.defaultIfNull(timeZone, TimeZone::getDefault);
	}

	/**
	 * 给定日期TemporalAccessor的构造
	 *
	 * @param temporalAccessor {@link TemporalAccessor} 对象
	 */
//	public DateTime(TemporalAccessor temporalAccessor) {
//		this(TemporalAccessorUtil.toInstant(temporalAccessor));
//	}

	/**
	 * 现在的时间
	 *
	 * @return 现在的时间
	 */
	public static DateTime now() {
		return new DateTime();
	}

	/**
	 * 转换为Calendar, 默认 {@link Locale}
	 *
	 * @return {@link Calendar}
	 */
	public Calendar toCalendar() {
		return toCalendar(Locale.getDefault(Locale.Category.FORMAT));
	}

	/**
	 * 转换为Calendar
	 *
	 * @param locale 地域 {@link Locale}
	 * @return {@link Calendar}
	 */
	public Calendar toCalendar(Locale locale) {
		return toCalendar(this.timeZone, locale);
	}

	/**
	 * 转换为Calendar
	 *
	 * @param zone 时区 {@link TimeZone}
	 * @return {@link Calendar}
	 */
	public Calendar toCalendar(TimeZone zone) {
		return toCalendar(zone, Locale.getDefault(Locale.Category.FORMAT));
	}

	/**
	 * 转换为Calendar
	 *
	 * @param zone   时区 {@link TimeZone}
	 * @param locale 地域 {@link Locale}
	 * @return {@link Calendar}
	 */
	public Calendar toCalendar(TimeZone zone, Locale locale) {
		if (null == locale) {
			locale = Locale.getDefault(Locale.Category.FORMAT);
		}
		final Calendar cal = (null != zone) ? Calendar.getInstance(zone, locale) : Calendar.getInstance(locale);
		//noinspection MagicConstant
		cal.setFirstDayOfWeek(firstDayOfWeek.getValue());
		// issue#1988@Github
		if (minimalDaysInFirstWeek > 0) {
			cal.setMinimalDaysInFirstWeek(minimalDaysInFirstWeek);
		}
		cal.setTime(this);
		return cal;
	}

	/**
	 * 设置一周的第一天<br>
	 * JDK的Calendar中默认一周的第一天是周日，Hutool中将此默认值设置为周一<br>
	 * 设置一周的第一天主要影响{@link #weekOfMonth()}和{@link #weekOfYear()} 两个方法
	 *
	 * @param firstDayOfWeek 一周的第一天
	 * @return this
	 * @see #weekOfMonth()
	 * @see #weekOfYear()
	 */
	public DateTime setFirstDayOfWeek(Week firstDayOfWeek) {
		this.firstDayOfWeek = firstDayOfWeek;
		return this;
	}

	/**
	 * 设置对象是否可变 如果为不可变对象，以下方法将返回新方法：
	 * <ul>
	 * <li>{@link DateTime#offset(DateField, int)}</li>
	 * <li>{@link DateTime#setField(DateField, int)}</li>
	 * <li>{@link DateTime#setField(int, int)}</li>
	 * </ul>
	 * 如果为不可变对象，{@link DateTime#setTime(long)}将抛出异常
	 *
	 * @param mutable 是否可变
	 * @return this
	 */
	public DateTime setMutable(boolean mutable) {
		this.mutable = mutable;
		return this;
	}

	/**
	 * 获取时区
	 *
	 * @return 时区
	 */
	public TimeZone getTimeZone() {
		return this.timeZone;
	}

	/**
	 * 获取时区ID
	 *
	 * @return 时区ID
	 */
//	public ZoneId getZoneId() {
//		return this.timeZone.toZoneId();
//	}


	/**
	 * 获得指定日期是所在年份的第几周<br>
	 * 此方法返回值与一周的第一天有关，比如：<br>
	 * 2016年1月3日为周日，如果一周的第一天为周日，那这天是第二周（返回2）<br>
	 * 如果一周的第一天为周一，那这天是第一周（返回1）<br>
	 * 跨年的那个星期得到的结果总是1
	 *
	 * @return 周
	 * @see #setFirstDayOfWeek(Week)
	 */
	public int weekOfYear() {
		return getField(DateField.WEEK_OF_YEAR);
	}

	/**
	 * 获得指定日期是所在月份的第几周<br>
	 * 此方法返回值与一周的第一天有关，比如：<br>
	 * 2016年1月3日为周日，如果一周的第一天为周日，那这天是第二周（返回2）<br>
	 * 如果一周的第一天为周一，那这天是第一周（返回1）
	 *
	 * @return 周
	 * @see #setFirstDayOfWeek(Week)
	 */
	public int weekOfMonth() {
		return getField(DateField.WEEK_OF_MONTH);
	}

	/**
	 * 获得指定日期是这个日期所在月份的第几天，从1开始
	 *
	 * @return 天，1表示第一天
	 */
	public int dayOfMonth() {
		return getField(DateField.DAY_OF_MONTH);
	}

	/**
	 * 获得指定日期是这个日期所在年份的第几天，从1开始
	 *
	 * @return 天，1表示第一天
	 */
	public int dayOfYear() {
		return getField(DateField.DAY_OF_YEAR);
	}

	/**
	 * 获得指定日期是星期几，1表示周日，2表示周一
	 *
	 * @return 星期几
	 */
	public int dayOfWeek() {
		return getField(DateField.DAY_OF_WEEK);
	}

	/**
	 * 获得天所在的周是这个月的第几周
	 *
	 * @return 天
	 */
	public int dayOfWeekInMonth() {
		return getField(DateField.DAY_OF_WEEK_IN_MONTH);
	}

	/**
	 * 获得指定日期是星期几
	 *
	 * @return {@link Week}
	 */
	public Week dayOfWeekEnum() {
		return Week.of(dayOfWeek());
	}

	/**
	 * 获得日期的某个部分<br>
	 * 例如获得年的部分，则使用 getField(DatePart.YEAR)
	 *
	 * @param field 表示日期的哪个部分的枚举 {@link DateField}
	 * @return 某个部分的值
	 */
	public int getField(DateField field) {
		return getField(field.getValue());
	}

	/**
	 * 获得日期的某个部分<br>
	 * 例如获得年的部分，则使用 getField(Calendar.YEAR)
	 *
	 * @param field 表示日期的哪个部分的int值 {@link Calendar}
	 * @return 某个部分的值
	 */
	public int getField(int field) {
		return toCalendar().get(field);
	}

	/**
	 * 设置日期的某个部分<br>
	 * 如果此对象为可变对象，返回自身，否则返回新对象，设置是否可变对象见{@link #setMutable(boolean)}
	 *
	 * @param field 表示日期的哪个部分的枚举 {@link DateField}
	 * @param value 值
	 * @return this
	 */
	public DateTime setField(DateField field, int value) {
		return setField(field.getValue(), value);
	}

	/**
	 * 设置日期的某个部分<br>
	 * 如果此对象为可变对象，返回自身，否则返回新对象，设置是否可变对象见{@link #setMutable(boolean)}
	 *
	 * @param field 表示日期的哪个部分的int值 {@link Calendar}
	 * @param value 值
	 * @return this
	 */
	public DateTime setField(int field, int value) {
		final Calendar calendar = toCalendar();
		calendar.set(field, value);

		DateTime dt = this;
		if (!mutable) {
			dt = ObjectUtil.clone(this);
		}
		return dt.setTimeInternal(calendar.getTimeInMillis());
	}

	// ----------------------------------------------------------------------- offset

	/**
	 * 调整日期和时间<br>
	 * 如果此对象为可变对象，返回自身，否则返回新对象，设置是否可变对象见{@link #setMutable(boolean)}
	 *
	 * @param datePart 调整的部分 {@link DateField}
	 * @param offset   偏移量，正数为向后偏移，负数为向前偏移
	 * @return 如果此对象为可变对象，返回自身，否则返回新对象
	 */
	public DateTime offset(DateField datePart, int offset) {
		if (DateField.ERA == datePart) {
			throw new IllegalArgumentException("ERA is not support offset!");
		}

		final Calendar cal = toCalendar();
		//noinspection MagicConstant
		cal.add(datePart.getValue(), offset);

		DateTime dt = mutable ? this : ObjectUtil.clone(this);
		return dt.setTimeInternal(cal.getTimeInMillis());
	}

	// -----------------------------------------------------------------------

	/**
	 * 设置日期时间
	 *
	 * @param time 日期时间毫秒
	 * @return this
	 */
	private DateTime setTimeInternal(long time) {
		super.setTime(time);
		return this;
	}

}
