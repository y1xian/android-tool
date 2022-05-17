package com.yyxnb.java;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机工具类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/3/19
 */
public class RandomUtil {

	/**
	 * 用于随机选的数字
	 */
	public static final String BASE_NUMBER = "0123456789";
	/**
	 * 用于随机选的字符
	 */
	public static final String BASE_CHAR = "abcdefghijklmnopqrstuvwxyz";
	/**
	 * 用于随机选的字符和数字
	 */
	public static final String BASE_CHAR_NUMBER = BASE_CHAR + BASE_NUMBER;


	/**
	 * 获取随机数生成器对象<br>
	 * ThreadLocalRandom是JDK 7之后提供并发产生随机数，能够解决多个线程发生的竞争争夺。
	 *
	 * <p>
	 * 注意：此方法返回的{@link ThreadLocalRandom}不可以在多线程环境下共享对象，否则有重复随机数问题。
	 * 见：https://www.jianshu.com/p/89dfe990295c
	 * </p>
	 *
	 * @return {@link ThreadLocalRandom}
	 */
	public static ThreadLocalRandom getRandom() {
		return ThreadLocalRandom.current();
	}

	/**
	 * 随机汉字（'\u4E00'-'\u9FFF'）
	 *
	 * @return 随机的汉字字符
	 */
	public static char randomChinese() {
		return (char) randomInt('\u4E00', '\u9FFF');
	}

	/**
	 * 获得指定范围内的随机数
	 *
	 * @param min 最小数（包含）
	 * @param max 最大数（不包含）
	 * @return 随机数
	 */
	public static int randomInt(int min, int max) {
		return getRandom().nextInt(min, max);
	}

	/**
	 * 获得指定范围内的随机数 [0,limit)
	 *
	 * @param limit 限制随机数的范围，不包括这个数
	 * @return 随机数
	 */
	public static int randomInt(int limit) {
		return getRandom().nextInt(limit);
	}

	/**
	 * 获得指定范围内的随机数[min, max)
	 *
	 * @param min 最小数（包含）
	 * @param max 最大数（不包含）
	 * @return 随机数
	 */
	public static long randomLong(long min, long max) {
		return getRandom().nextLong(min, max);
	}


	/**
	 * 获得一个随机的字符串（只包含数字和字符）
	 *
	 * @param length 字符串的长度
	 * @return 随机字符串
	 */
	public static String randomString(int length) {
		return randomString(BASE_CHAR_NUMBER, length);
	}

	/**
	 * 获得一个只包含数字的字符串
	 *
	 * @param length 字符串的长度
	 * @return 随机字符串
	 */
	public static String randomNumbers(int length) {
		return randomString(BASE_NUMBER, length);
	}

	/**
	 * 获得一个随机的字符串
	 *
	 * @param baseString 随机字符选取的样本
	 * @param length     字符串的长度
	 * @return 随机字符串
	 */
	public static String randomString(String baseString, int length) {
		if (StrUtil.isEmpty(baseString)) {
			return StrUtil.EMPTY;
		}
		final StringBuilder sb = new StringBuilder(length);

		if (length < 1) {
			length = 1;
		}
		int baseLength = baseString.length();
		for (int i = 0; i < length; i++) {
			int number = randomInt(baseLength);
			sb.append(baseString.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 获得一个随机的字符串（只包含数字和字符） 并排除指定字符串
	 *
	 * @param length   字符串的长度
	 * @param elemData 要排除的字符串
	 * @return 随机字符串
	 */
	public static String randomStringWithoutStr(int length, String elemData) {
		String baseStr = BASE_CHAR_NUMBER;
		baseStr = StrUtil.removeAll(baseStr, elemData.toCharArray());
		return randomString(baseStr, length);
	}

	public static void main(String[] args) {
		System.out.println(randomChinese());
	}
}
