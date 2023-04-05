package com.yyxnb.android.callback;

/**
 * StreamProgress
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/12/1
 */
public interface StreamProgress {

	/**
	 * 开始
	 *
	 * @param key key
	 */
	default void start(String key) {
		start();
	}

	/**
	 * 开始
	 */
	default void start() {
	}

	/**
	 * 结束
	 *
	 * @param fileName 本地文件路径
	 */
	default void end(String fileName) {
		end();
	}

	/**
	 * 结束
	 */
	default void end() {
	}

	/**
	 * 进度
	 *
	 * @param total   总进度
	 * @param current 当前进度
	 * @param length  位置
	 */
	default void onProgress(long total, long current, long length) {
		onProgress(total, current);
	}

	/**
	 * 进度
	 *
	 * @param total   总进度
	 * @param current 当前进度
	 */
	default void onProgress(long total, long current) {
		onProgress(current);
	}

	/**
	 * 进度
	 *
	 * @param current 当前进度
	 */
	default void onProgress(long current) {
	}
}
