package com.yyxnb.android.utils;

import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;

import androidx.collection.SimpleArrayMap;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 对象工具类
 *
 * <pre>
 *     对象工具类
 * </pre>
 *
 * @author yyx
 * @date 2022/3/19
 */
public class ObjectUtil {


	/**
	 * 比较两个对象是否相等，此方法是 {@link #equal(Object, Object)}的别名方法。<br>
	 * 相同的条件有两个，满足其一即可：<br>
	 * <ol>
	 * <li>obj1 == null &amp;&amp; obj2 == null</li>
	 * <li>obj1.equals(obj2)</li>
	 * <li>如果是BigDecimal比较，0 == obj1.compareTo(obj2)</li>
	 * </ol>
	 *
	 * @param obj1 对象1
	 * @param obj2 对象2
	 * @return 是否相等
	 * @see #equal(Object, Object)
	 */
	public static boolean equals(Object obj1, Object obj2) {
		return equal(obj1, obj2);
	}

	/**
	 * 比较两个对象是否相等。<br>
	 * 相同的条件有两个，满足其一即可：<br>
	 * <ol>
	 * <li>obj1 == null &amp;&amp; obj2 == null</li>
	 * <li>obj1.equals(obj2)</li>
	 * <li>如果是BigDecimal比较，0 == obj1.compareTo(obj2)</li>
	 * </ol>
	 *
	 * @param obj1 对象1
	 * @param obj2 对象2
	 * @return 是否相等
	 * @see Objects#equals(Object, Object)
	 */
	public static boolean equal(Object obj1, Object obj2) {
		return UtilInner.equals(obj1, obj2);
	}

	/**
	 * 计算对象长度，如果是字符串调用其length函数，集合类调用其size函数，数组调用其length属性，其他可遍历对象遍历计算长度<br>
	 * 支持的类型包括：
	 * <ul>
	 * <li>CharSequence</li>
	 * <li>Map</li>
	 * <li>Iterator</li>
	 * <li>Enumeration</li>
	 * <li>Array</li>
	 * </ul>
	 *
	 * @param obj 被计算长度的对象
	 * @return 长度
	 */
	public static int length(Object obj) {
		if (obj == null) {
			return 0;
		}
		if (obj instanceof CharSequence) {
			return ((CharSequence) obj).length();
		}
		if (obj instanceof Collection) {
			return ((Collection<?>) obj).size();
		}
		if (obj instanceof Map) {
			return ((Map<?, ?>) obj).size();
		}

		int count;
		if (obj instanceof Iterator) {
			Iterator<?> iter = (Iterator<?>) obj;
			count = 0;
			while (iter.hasNext()) {
				count++;
				iter.next();
			}
			return count;
		}
		if (obj instanceof Enumeration) {
			Enumeration<?> enumeration = (Enumeration<?>) obj;
			count = 0;
			while (enumeration.hasMoreElements()) {
				count++;
				enumeration.nextElement();
			}
			return count;
		}
		if (obj.getClass().isArray()) {
			return Array.getLength(obj);
		}
		return -1;
	}

	/**
	 * 对象中是否包含元素<br>
	 * 支持的对象类型包括：
	 * <ul>
	 * <li>String</li>
	 * <li>Collection</li>
	 * <li>Map</li>
	 * <li>Iterator</li>
	 * <li>Enumeration</li>
	 * <li>Array</li>
	 * </ul>
	 *
	 * @param obj     对象
	 * @param element 元素
	 * @return 是否包含
	 */
	public static boolean contains(Object obj, Object element) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof String) {
			if (element == null) {
				return false;
			}
			return ((String) obj).contains(element.toString());
		}
		if (obj instanceof Collection) {
			return ((Collection<?>) obj).contains(element);
		}
		if (obj instanceof Map) {
			return ((Map<?, ?>) obj).containsValue(element);
		}

		if (obj instanceof Iterator) {
			Iterator<?> iter = (Iterator<?>) obj;
			while (iter.hasNext()) {
				Object o = iter.next();
				if (equal(o, element)) {
					return true;
				}
			}
			return false;
		}
		if (obj instanceof Enumeration) {
			Enumeration<?> enumeration = (Enumeration<?>) obj;
			while (enumeration.hasMoreElements()) {
				Object o = enumeration.nextElement();
				if (equal(o, element)) {
					return true;
				}
			}
			return false;
		}
		if (obj.getClass().isArray()) {
			int len = Array.getLength(obj);
			for (int i = 0; i < len; i++) {
				Object o = Array.get(obj, i);
				if (equal(o, element)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断指定对象是否为空，支持：
	 *
	 * <pre>
	 * 1. CharSequence
	 * 2. Map
	 * 3. Iterable
	 * 4. Iterator
	 * 5. Array
	 * </pre>
	 *
	 * @param obj 被判断的对象
	 * @return 是否为空，如果类型不支持，返回false
	 */
	public static boolean isEmpty(Object obj) {
		if (null == obj) {
			return true;
		} else if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
			return true;
		} else if (obj instanceof CharSequence && obj.toString().length() == 0) {
			return true;
		} else if (obj instanceof Collection && ((Collection<?>) obj).isEmpty()) {
			return true;
		} else if (obj instanceof Map && ((Map<?, ?>) obj).isEmpty()) {
			return true;
		} else if (obj instanceof SimpleArrayMap && ((SimpleArrayMap<?, ?>) obj).isEmpty()) {
			return true;
		} else if (obj instanceof SparseArray && ((SparseArray<?>) obj).size() == 0) {
			return true;
		} else if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() == 0) {
			return true;
		} else if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() == 0) {
			return true;
		} else if (obj instanceof Iterable && !((Iterable<?>) obj).iterator().hasNext()) {
			return true;
		} else if (obj instanceof Iterable && !((Iterator<?>) obj).hasNext()) {
			return true;
		} else if (obj instanceof Enumeration && !((Enumeration<?>) obj).hasMoreElements()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断指定对象是否为非空，支持：
	 *
	 * <pre>
	 * 1. CharSequence
	 * 2. Map
	 * 3. Iterable
	 * 4. Iterator
	 * 5. Array
	 * </pre>
	 *
	 * @param obj 被判断的对象
	 * @return 是否为空，如果类型不支持，返回true
	 */
	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	/**
	 * 如果给定对象为{@code null}返回默认值
	 *
	 * <pre>
	 * ObjectUtil.defaultIfNull(null, null)      = null
	 * ObjectUtil.defaultIfNull(null, "")        = ""
	 * ObjectUtil.defaultIfNull(null, "zz")      = "zz"
	 * ObjectUtil.defaultIfNull("abc", *)        = "abc"
	 * ObjectUtil.defaultIfNull(Boolean.TRUE, *) = Boolean.TRUE
	 * </pre>
	 *
	 * @param <T>          对象类型
	 * @param object       被检查对象，可能为{@code null}
	 * @param defaultValue 被检查对象为{@code null}返回的默认值，可以为{@code null}
	 * @return 被检查对象为{@code null}返回默认值，否则返回原值
	 */
	public static <T> T defaultIfNull(final T object, final T defaultValue) {
		return isEmpty(object) ? defaultValue : object;
	}

	/**
	 * 如果被检查对象为 {@code null}， 返回默认值（由 defaultValueSupplier 提供）；否则直接返回
	 *
	 * @param source               被检查对象
	 * @param defaultValueSupplier 默认值提供者
	 * @param <T>                  对象类型
	 * @return 被检查对象为{@code null}返回默认值，否则返回自定义handle处理后的返回值
	 * @throws NullPointerException {@code defaultValueSupplier == null} 时，抛出
	 */
	public static <T> T defaultIfNull(T source, Supplier<? extends T> defaultValueSupplier) {
		if (isEmpty(source)) {
			return defaultValueSupplier.get();
		}
		return source;
	}

	/**
	 * 如果给定对象为{@code null} 返回默认值, 如果不为null 返回自定义handle处理后的返回值
	 *
	 * @param source       Object 类型对象
	 * @param handle       非空时自定义的处理方法
	 * @param defaultValue 默认为空的返回值
	 * @param <T>          被检查对象为{@code null}返回默认值，否则返回自定义handle处理后的返回值
	 * @return 处理后的返回值
	 */
	public static <T> T defaultIfNull(Object source, Supplier<? extends T> handle, final T defaultValue) {
		if (isNotEmpty(source)) {
			return handle.get();
		}
		return defaultValue;
	}

}
