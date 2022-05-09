package com.yyxnb.lib.java;

import com.yyxnb.lib.java.collection.ArrayUtil;
import com.yyxnb.lib.java.collection.CollUtil;

import java.util.Set;

/**
 * Boolean类型相关工具类
 *
 * @author yyx
 * @date 2022/4/9
 */
public class BooleanUtil {

	/**
	 * 表示为真的字符串
	 */
	private static final Set<String> TRUE_SET = CollUtil.newHashSet("true", "yes", "y", "t", "ok", "1", "on", "是", "对", "真", "對", "√");

	/**
	 * 转换字符串为boolean值
	 *
	 * @param valueStr 字符串
	 * @return boolean值
	 */
	public static boolean toBoolean(String valueStr) {
		if (StrUtil.isNotBlank(valueStr)) {
			valueStr = valueStr.trim().toLowerCase();
			return TRUE_SET.contains(valueStr);
		}
		return false;
	}

	/**
	 * 对Boolean数组取与
	 *
	 * <pre>
	 *   BooleanUtil.and(true, true)         = true
	 *   BooleanUtil.and(false, false)       = false
	 *   BooleanUtil.and(true, false)        = false
	 *   BooleanUtil.and(true, true, false)  = false
	 *   BooleanUtil.and(true, true, true)   = true
	 * </pre>
	 *
	 * @param array {@code Boolean}数组
	 * @return 取与为真返回{@code true}
	 */
	public static boolean and(boolean... array) {
		if (ArrayUtil.isEmpty(array)) {
			throw new IllegalArgumentException("The Array must not be empty !");
		}
		for (final boolean element : array) {
			if (!element) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 对Boolean数组取或
	 *
	 * <pre>
	 *   BooleanUtil.or(true, true)          = true
	 *   BooleanUtil.or(false, false)        = false
	 *   BooleanUtil.or(true, false)         = true
	 *   BooleanUtil.or(true, true, false)   = true
	 *   BooleanUtil.or(true, true, true)    = true
	 *   BooleanUtil.or(false, false, false) = false
	 * </pre>
	 *
	 * @param array {@code Boolean}数组
	 * @return 取或为真返回{@code true}
	 */
	public static boolean or(boolean... array) {
		if (ArrayUtil.isEmpty(array)) {
			throw new IllegalArgumentException("The Array must not be empty !");
		}
		for (final boolean element : array) {
			if (element) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 对Boolean数组取异或
	 *
	 * <pre>
	 *   BooleanUtil.xor(true, true)   = false
	 *   BooleanUtil.xor(false, false) = false
	 *   BooleanUtil.xor(true, false)  = true
	 *   BooleanUtil.xor(true, true)   = false
	 *   BooleanUtil.xor(false, false) = false
	 *   BooleanUtil.xor(true, false)  = true
	 * </pre>
	 *
	 * @param array {@code boolean}数组
	 * @return 如果异或计算为true返回 {@code true}
	 */
	public static boolean xor(boolean... array) {
		if (ArrayUtil.isEmpty(array)) {
			throw new IllegalArgumentException("The Array must not be empty");
		}

		boolean result = false;
		for (final boolean element : array) {
			result ^= element;
		}

		return result;
	}

	/**
	 * 给定类是否为Boolean或者boolean
	 *
	 * @param clazz 类
	 * @return 是否为Boolean或者boolean
	 */
	public static boolean isBoolean(Class<?> clazz) {
		return (clazz == Boolean.class || clazz == boolean.class);
	}

	// ----------------------------------------------------------------------- to

	/**
	 * boolean值转为int
	 *
	 * @param value Boolean值
	 * @return int值
	 */
	public static int toInt(boolean value) {
		return value ? 1 : 0;
	}

	/**
	 * boolean值转为Integer
	 *
	 * @param value Boolean值
	 * @return Integer值
	 */
	public static Integer toInteger(boolean value) {
		return toInt(value);
	}

	/**
	 * boolean值转为char
	 *
	 * @param value Boolean值
	 * @return char值
	 */
	public static char toChar(boolean value) {
		return (char) toInt(value);
	}

	/**
	 * boolean值转为Character
	 *
	 * @param value Boolean值
	 * @return Character值
	 */
	public static Character toCharacter(boolean value) {
		return toChar(value);
	}

	/**
	 * boolean值转为byte
	 *
	 * @param value Boolean值
	 * @return byte值
	 */
	public static byte toByte(boolean value) {
		return (byte) toInt(value);
	}

	/**
	 * boolean值转为Byte
	 *
	 * @param value Boolean值
	 * @return Byte值
	 */
	public static Byte toByteObj(boolean value) {
		return toByte(value);
	}

	/**
	 * boolean值转为long
	 *
	 * @param value Boolean值
	 * @return long值
	 */
	public static long toLong(boolean value) {
		return toInt(value);
	}

	/**
	 * boolean值转为Long
	 *
	 * @param value Boolean值
	 * @return Long值
	 */
	public static Long toLongObj(boolean value) {
		return toLong(value);
	}

	/**
	 * boolean值转为short
	 *
	 * @param value Boolean值
	 * @return short值
	 */
	public static short toShort(boolean value) {
		return (short) toInt(value);
	}

	/**
	 * boolean值转为Short
	 *
	 * @param value Boolean值
	 * @return Short值
	 */
	public static Short toShortObj(boolean value) {
		return toShort(value);
	}

	/**
	 * boolean值转为float
	 *
	 * @param value Boolean值
	 * @return float值
	 */
	public static float toFloat(boolean value) {
		return (float) toInt(value);
	}

	/**
	 * boolean值转为Float
	 *
	 * @param value Boolean值
	 * @return float值
	 */
	public static Float toFloatObj(boolean value) {
		return toFloat(value);
	}

	/**
	 * boolean值转为double
	 *
	 * @param value Boolean值
	 * @return double值
	 */
	public static double toDouble(boolean value) {
		return toInt(value);
	}

	/**
	 * boolean值转为double
	 *
	 * @param value Boolean值
	 * @return double值
	 */
	public static Double toDoubleObj(boolean value) {
		return toDouble(value);
	}

}
