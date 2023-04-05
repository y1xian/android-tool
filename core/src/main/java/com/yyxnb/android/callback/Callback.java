package com.yyxnb.android.callback;

/**
 * Callback
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/7/10
 */
public interface Callback<T> {

	/**
	 * 成功
	 *
	 * @param t T
	 */
	void onSuccess(T t);

	/**
	 * 失败
	 *
	 * @param code     错误码
	 * @param errorMsg 错误信息
	 */
	void onFailure(int code, String errorMsg);
}