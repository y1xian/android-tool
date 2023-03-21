package com.yyxnb.android.modules;

import java.util.concurrent.Callable;

/**
 * IThread
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/3/21
 */
public interface IThread extends IModule {

	/**
	 * 在UI线程执行任务
	 *
	 * @param runnable runnable
	 */
	void runOnUiThread(Runnable runnable);

	/**
	 * 在UI线程执行任务，等待任务完成位置
	 *
	 * @param runnable        runnable
	 * @param waitUntilFinish 是否等待
	 */
	void runOnUiThread(Runnable runnable, boolean waitUntilFinish);

	/**
	 * 在UI线程执行任务
	 *
	 * @param callable callable
	 * @param <T>      T
	 * @return T
	 */
	<T> T executeOnUiThread(Callable<T> callable);

	/**
	 * 在后台线程执行任务
	 *
	 * @param runnable runnable
	 */
	void postOnBackground(Runnable runnable);

	/**
	 * 在后台线程执行任务
	 *
	 * @param callable callable
	 * @param <T>      T
	 * @return T
	 */
	<T> T executeOnBackground(Callable<T> callable);

	/**
	 * 线程休眠时间
	 *
	 * @param millisecond 毫秒单位
	 */
	void sleepSilently(int millisecond);

	/**
	 * 判断当前是否在UI线程
	 *
	 * @return 是否UI线程
	 */
	boolean isInUiThread();
}
