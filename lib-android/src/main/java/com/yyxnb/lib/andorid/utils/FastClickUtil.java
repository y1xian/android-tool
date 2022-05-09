package com.yyxnb.lib.andorid.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 防止快速点击
 *
 * <pre>
 *    {@link #isFastClick()} 共用同个最后点击时间
 *    {@link #isFastClick(long)} 自定义间隔时间
 *
 *    {@link #isFastClickId(long)} 区分id的间隔时间
 *    {@link #isFastClickId(long, long)}
 * </pre>
 *
 * @author yyx
 * @date 2022/3/19
 */
public class FastClickUtil {
	// 间隔时间
	private static final int MIN_CLICK_DELAY_TIME = 500;
	private static final int MIN_CLICK_ID_DELAY_TIME = 1000;
	private static final int RECORD_SIZE = 100;
	// 记录最后一次点击时间
	private static long lastClickTime;
	// 记录id的最后一次点击时间
	private static final Map<String, Long> RECORDS = new HashMap<>();

	public static boolean isFastClick() {
		return isFastClick(MIN_CLICK_DELAY_TIME);
	}

	/**
	 * 防止快速点击，共用全局变量{{@link #lastClickTime}}，间隔里会导致别处点击不了
	 *
	 * @param delayTime 间隔时间
	 * @return {@code true} 可以点击，{@code false} 不可点击
	 */
	public static boolean isFastClick(long delayTime) {
		boolean flag = false;
		// 当前点击时间
		long curClickTime = System.currentTimeMillis();
		if ((curClickTime - lastClickTime) >= delayTime) {
			flag = true;
			lastClickTime = curClickTime;
		}
		return flag;
	}

	// ----------------------------------------------------------------------- id 区分

	public static boolean isFastClickId(long id) {
		return isFastClickId(MIN_CLICK_ID_DELAY_TIME, id);
	}

	/**
	 * 防止快速点击，改进{{@link #isFastClick()}}，每个点击时间独立记录
	 *
	 * @param delayTime 间隔时间
	 * @param id        标识，view.getId()
	 * @return {@code true} 可以点击，{@code false} 不可点击
	 */
	public static boolean isFastClickId(long delayTime, long id) {
		if (RECORDS.size() > RECORD_SIZE) {
			RECORDS.clear();
		}
		boolean flag = false;
		String key = String.valueOf(id);
		Long lastClickTime = RECORDS.get(key);
		// 当前点击时间
		long curClickTime = System.currentTimeMillis();
		if (lastClickTime == null) {
			lastClickTime = 0L;
		}
		if ((curClickTime - lastClickTime) >= delayTime) {
			flag = true;
			RECORDS.put(key, curClickTime);
		}
		return flag;
	}
}