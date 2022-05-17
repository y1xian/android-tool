package com.yyxnb.java.exceptions;

import com.yyxnb.java.StrUtil;

/**
 * 异常工具类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/3/20
 */
public class ExceptionUtil {


	/**
	 * 获得完整消息，包括异常名，消息格式为：{SimpleClassName}: {ThrowableMessage}
	 *
	 * @param e 异常
	 * @return 完整消息
	 */
	public static String getMessage(Throwable e) {
		if (null == e) {
			return StrUtil.NULL;
		}
		return StrUtil.format("%s: %s", e.getClass().getSimpleName(), e.getMessage());
	}

	/**
	 * 获得消息，调用异常类的getMessage方法
	 *
	 * @param e 异常
	 * @return 消息
	 */
	public static String getSimpleMessage(Throwable e) {
		return (null == e) ? StrUtil.NULL : e.getMessage();
	}

	/**
	 * 使用运行时异常包装编译异常<br>
	 * <p>
	 * 如果传入参数已经是运行时异常，则直接返回，不再额外包装
	 *
	 * @param throwable 异常
	 * @return 运行时异常
	 */
	public static RuntimeException wrapRuntime(Throwable throwable) {
		if (throwable instanceof RuntimeException) {
			return (RuntimeException) throwable;
		}
		return new RuntimeException(throwable);
	}

	/**
	 * 将指定的消息包装为运行时异常
	 *
	 * @param message 异常消息
	 * @return 运行时异常
	 */
	public static RuntimeException wrapRuntime(String message) {
		return new RuntimeException(message);
	}

	/**
	 * 获取当前栈信息
	 *
	 * @return 当前栈信息
	 */
	public static StackTraceElement[] getStackElements() {
		return Thread.currentThread().getStackTrace();
	}

	/**
	 * 获取指定层的堆栈信息
	 *
	 * @param i 层数
	 * @return 指定层的堆栈信息
	 */
	public static StackTraceElement getStackElement(int i) {
		return getStackElements()[i];
	}

	/**
	 * 获取入口堆栈信息
	 *
	 * @return 入口堆栈信息
	 */
	public static StackTraceElement getRootStackElement() {
		final StackTraceElement[] stackElements = getStackElements();
		return stackElements[stackElements.length - 1];
	}

	public static void main(String[] args) {

		String format = String.format("%s %s", "123", "321");
		String format1 = StrUtil.format("%s %s", "1123", "321");
		System.out.println(format);
		System.out.println(format1);
	}
}
