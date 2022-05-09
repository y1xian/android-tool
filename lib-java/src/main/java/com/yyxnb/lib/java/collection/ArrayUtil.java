package com.yyxnb.lib.java.collection;

import com.yyxnb.lib.java.Assert;
import com.yyxnb.lib.java.CompareUtil;
import com.yyxnb.lib.java.Matcher;
import com.yyxnb.lib.java.ObjectUtil;
import com.yyxnb.lib.java.RandomUtil;
import com.yyxnb.lib.java.StrUtil;
import com.yyxnb.lib.java.text.StrJoiner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * 数组工具类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/3/19
 */
public class ArrayUtil {

	/**
	 * 数组中元素未找到的下标，值为-1
	 */
	public static final int INDEX_NOT_FOUND = -1;

	/**
	 * 数组是否为空
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组
	 * @return 是否为空
	 */
	public static <T> boolean isEmpty(T[] array) {
		return array == null || array.length == 0;
	}


	/**
	 * 数组是否为空<br>
	 * 此方法会匹配单一对象，如果此对象为{@code null}则返回true<br>
	 * 如果此对象为非数组，理解为此对象为数组的第一个元素，则返回false<br>
	 * 如果此对象为数组对象，数组长度大于0情况下返回false，否则返回true
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(Object array) {
		if (array != null) {
			if (isArray(array)) {
				return 0 == Array.getLength(array);
			}
			return false;
		}
		return true;
	}

	/**
	 * 如果给定数组为空，返回默认数组
	 *
	 * @param <T>          数组元素类型
	 * @param array        数组
	 * @param defaultArray 默认数组
	 * @return 非空（empty）的原数组或默认数组
	 */
	public static <T> T[] defaultIfEmpty(T[] array, T[] defaultArray) {
		return isEmpty(array) ? defaultArray : array;
	}

	/**
	 * 数组是否为非空
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static <T> boolean isNotEmpty(T[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * 数组是否为非空<br>
	 * 此方法会匹配单一对象，如果此对象为{@code null}则返回false<br>
	 * 如果此对象为非数组，理解为此对象为数组的第一个元素，则返回true<br>
	 * 如果此对象为数组对象，数组长度大于0情况下返回true，否则返回false
	 *
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(Object array) {
		return !isEmpty(array);
	}

	/**
	 * 是否包含{@code null}元素
	 *
	 * @param <T>   数组元素类型
	 * @param array 被检查的数组
	 * @return 是否包含{@code null}元素
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean hasNull(T... array) {
		if (isNotEmpty(array)) {
			for (T element : array) {
				if (null == element) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 返回数组中第一个匹配规则的值
	 *
	 * @param <T>     数组元素类型
	 * @param matcher 匹配接口，实现此接口自定义匹配规则
	 * @param array   数组
	 * @return 非空元素，如果不存在非空元素或数组为空，返回{@code null}
	 */
	@SuppressWarnings("unchecked")
	public static <T> T firstMatch(Matcher<T> matcher, T... array) {
		if (isNotEmpty(array)) {
			for (final T val : array) {
				if (matcher.match(val)) {
					return val;
				}
			}
		}
		return null;
	}

	// ----------------------------------------------------------------------- 新建数组

	/**
	 * 新建一个空数组
	 *
	 * @param <T>           数组元素类型
	 * @param componentType 元素类型
	 * @param newSize       大小
	 * @return 空数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] newArray(Class<?> componentType, int newSize) {
		return (T[]) Array.newInstance(componentType, newSize);
	}

	/**
	 * 新建一个空数组
	 *
	 * @param newSize 大小
	 * @return 空数组
	 */
	public static Object[] newArray(int newSize) {
		return new Object[newSize];
	}

	// ----------------------------------------------------------------------- add

	/**
	 * 将多个数组合并在一起<br>
	 * 忽略null的数组
	 *
	 * @param arrays 数组集合
	 * @return 合并后的数组
	 */
	public static int[] addAll(int[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}

		// 计算总长度
		int length = 0;
		for (int[] array : arrays) {
			if (null != array) {
				length += array.length;
			}
		}

		final int[] result = new int[length];
		length = 0;
		for (int[] array : arrays) {
			if (null != array) {
				System.arraycopy(array, 0, result, length, array.length);
				length += array.length;
			}
		}
		return result;
	}

	/**
	 * 将新元素添加到已有数组中<br>
	 * 添加新元素会生成一个新的数组，不影响原数组
	 *
	 * @param <T>         数组元素类型
	 * @param buffer      已有数组
	 * @param newElements 新元素
	 * @return 新数组
	 */
	@SafeVarargs
	public static <T> T[] append(T[] buffer, T... newElements) {
		if (isEmpty(buffer)) {
			return newElements;
		}
		return insert(buffer, buffer.length, newElements);
	}

	/**
	 * 将新元素插入到到已有数组中的某个位置<br>
	 * 添加新元素会生成一个新的数组，不影响原数组<br>
	 * 如果插入位置为为负数，从原数组从后向前计数，若大于原数组长度，则空白处用null填充
	 *
	 * @param <T>         数组元素类型
	 * @param buffer      已有数组
	 * @param index       插入位置，此位置为对应此位置元素之前的空档
	 * @param newElements 新元素
	 * @return 新数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] insert(T[] buffer, int index, T... newElements) {
		return (T[]) insert((Object) buffer, index, newElements);
	}

	/**
	 * 将新元素插入到到已有数组中的某个位置<br>
	 * 添加新元素会生成一个新的数组，不影响原数组<br>
	 * 如果插入位置为为负数，从原数组从后向前计数，若大于原数组长度，则空白处用null填充
	 *
	 * @param <T>         数组元素类型
	 * @param array       已有数组
	 * @param index       插入位置，此位置为对应此位置元素之前的空档
	 * @param newElements 新元素
	 * @return 新数组
	 */
	@SuppressWarnings({"unchecked", "SuspiciousSystemArraycopy"})
	public static <T> Object insert(Object array, int index, T... newElements) {
		if (isEmpty(newElements)) {
			return array;
		}
		if (isEmpty(array)) {
			return newElements;
		}

		final int len = length(array);
		if (index < 0) {
			index = (index % len) + len;
		}

		final T[] result = newArray(array.getClass().getComponentType(), Math.max(len, index) + newElements.length);
		System.arraycopy(array, 0, result, 0, Math.min(len, index));
		System.arraycopy(newElements, 0, result, index, newElements.length);
		if (index < len) {
			System.arraycopy(array, index, result, index + newElements.length, len - index);
		}
		return result;
	}

	// ----------------------------------------------------------------------- 获取

	/**
	 * 获取数组对象中指定index的值，支持负数，例如-1表示倒数第一个值<br>
	 * 如果数组下标越界，返回null
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组对象
	 * @param index 下标，支持负数
	 * @return 值
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(Object array, int index) {
		if (null == array) {
			return null;
		}

		if (index < 0) {
			index += Array.getLength(array);
		}
		try {
			return (T) Array.get(array, index);
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * 获取数组中指定多个下标元素值，组成新数组
	 *
	 * @param <T>     数组元素类型
	 * @param array   数组
	 * @param indexes 下标列表
	 * @return 结果
	 */
	public static <T> T[] getAny(Object array, int... indexes) {
		if (null == array) {
			return null;
		}

		final T[] result = newArray(array.getClass().getComponentType(), indexes.length);
		for (int i : indexes) {
			result[i] = get(array, i);
		}
		return result;
	}

	/**
	 * 获取数组长度<br>
	 * 如果参数为{@code null}，返回0
	 *
	 * <pre>
	 * ArrayUtil.length(null)            = 0
	 * ArrayUtil.length([])              = 0
	 * ArrayUtil.length([null])          = 1
	 * ArrayUtil.length([true, false])   = 2
	 * ArrayUtil.length([1, 2, 3])       = 3
	 * ArrayUtil.length(["a", "b", "c"]) = 3
	 * </pre>
	 *
	 * @param array 数组对象
	 * @return 数组长度
	 * @throws IllegalArgumentException 如果参数不为数组，抛出此异常
	 * @see Array#getLength(Object)
	 */
	public static int length(Object array) throws IllegalArgumentException {
		if (null == array) {
			return 0;
		}
		return Array.getLength(array);
	}

	// --------------------------------------------------------------------- remove

	/**
	 * 移除数组中对应位置的元素<br>
	 * copy from commons-lang
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param index 位置，如果位置小于0或者大于长度，返回原数组
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] remove(T[] array, int index) throws IllegalArgumentException {
		return (T[]) remove((Object) array, index);
	}

	/**
	 * 移除数组中对应位置的元素<br>
	 * copy from commons-lang
	 *
	 * @param array 数组对象，可以是对象数组，也可以原始类型数组
	 * @param index 位置，如果位置小于0或者大于长度，返回原数组
	 * @return 去掉指定元素后的新数组或原数组
	 * @throws IllegalArgumentException 参数对象不为数组对象
	 */
	@SuppressWarnings("SuspiciousSystemArraycopy")
	public static Object remove(Object array, int index) throws IllegalArgumentException {
		if (null == array) {
			return null;
		}
		int length = Array.getLength(array);
		if (index < 0 || index >= length) {
			return array;
		}

		final Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
		System.arraycopy(array, 0, result, 0, index);
		if (index < length - 1) {
			// 后半部分
			System.arraycopy(array, index + 1, result, index, length - index - 1);
		}

		return result;
	}


	/**
	 * 去除{@code null} 元素
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组
	 * @return 处理后的数组
	 */
	public static <T> T[] removeNull(T[] array) {
		return filter(array, (Editor<T>) t -> {
			// 返回null便不加入集合
			return t;
		});
	}

	/**
	 * 去除{@code null}或者"" 元素
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组
	 * @return 处理后的数组
	 */
	public static <T extends CharSequence> T[] removeEmpty(T[] array) {
		return filter(array, (Filter<T>) t -> !StrUtil.isEmpty(t));
	}


	/**
	 * 去除{@code null}或者""或者空白字符串 元素
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组
	 * @return 处理后的数组
	 */
	public static <T extends CharSequence> T[] removeBlank(T[] array) {
		return filter(array, (Filter<T>) t -> !StrUtil.isBlank(t));
	}

	// --------------------------------------------------------------------- reverse

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 变更后的原数组
	 */
	public static char[] reverse(char[] array) {
		return reverse(array, 0, array.length);
	}

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param array               数组，会变更
	 * @param startIndexInclusive 其实位置（包含）
	 * @param endIndexExclusive   结束位置（不包含）
	 * @return 变更后的原数组
	 */
	public static char[] reverse(char[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = Math.max(startIndexInclusive, 0);
		int j = Math.min(array.length, endIndexExclusive) - 1;
		while (j > i) {
			swap(array, i, j);
			j--;
			i++;
		}
		return array;
	}

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param array 数组，会变更
	 * @return 变更后的原数组
	 */
	public static int[] reverse(int[] array) {
		return reverse(array, 0, array.length);
	}

	/**
	 * 反转数组，会变更原数组
	 *
	 * @param array               数组，会变更
	 * @param startIndexInclusive 其实位置（包含）
	 * @param endIndexExclusive   结束位置（不包含）
	 * @return 变更后的原数组
	 */
	public static int[] reverse(int[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (isEmpty(array)) {
			return array;
		}
		int i = Math.max(startIndexInclusive, 0);
		int j = Math.min(array.length, endIndexExclusive) - 1;
		while (j > i) {
			swap(array, i, j);
			j--;
			i++;
		}
		return array;
	}

	// -----------------------------------------------------------------------

	/**
	 * 对象是否为数组对象
	 *
	 * @param obj 对象
	 * @return 是否为数组对象，如果为{@code null} 返回false
	 */
	public static boolean isArray(Object obj) {
		if (null == obj) {
			return false;
		}
		return obj.getClass().isArray();
	}

	/**
	 * 克隆数组
	 *
	 * @param <T>   数组元素类型
	 * @param array 被克隆的数组
	 * @return 新数组
	 */
	public static <T> T[] clone(T[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * 克隆数组，如果非数组返回{@code null}
	 *
	 * @param <T> 数组元素类型
	 * @param obj 数组对象
	 * @return 克隆后的数组对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T clone(final T obj) {
		if (null == obj) {
			return null;
		}
		if (isArray(obj)) {
			final Object result;
			final Class<?> componentType = obj.getClass().getComponentType();
			if (componentType.isPrimitive()) {// 原始类型
				int length = Array.getLength(obj);
				result = Array.newInstance(componentType, length);
				while (length-- > 0) {
					Array.set(result, length, Array.get(obj, length));
				}
			} else {
				result = ((Object[]) obj).clone();
			}
			return (T) result;
		}
		return null;
	}

	// ----------------------------------------------------------------------- contains

	/**
	 * 数组中是否包含元素
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 */
	public static <T> boolean contains(T[] array, T value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}

	/**
	 * 数组中是否包含元素
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 */
	public static boolean contains(int[] array, int value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}

	/**
	 * 数组中是否包含元素
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 */
	public static boolean contains(char[] array, char value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}

	/**
	 * 数组中是否包含指定元素中的任意一个
	 *
	 * @param <T>    数组元素类型
	 * @param array  数组
	 * @param values 被检查的多个元素
	 * @return 是否包含指定元素中的任意一个
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean containsAny(T[] array, T... values) {
		for (T value : values) {
			if (contains(array, value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 数组中是否包含指定元素中的全部
	 *
	 * @param <T>    数组元素类型
	 * @param array  数组
	 * @param values 被检查的多个元素
	 * @return 是否包含指定元素中的全部
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean containsAll(T[] array, T... values) {
		for (T value : values) {
			if (!contains(array, value)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 数组中是否包含元素，忽略大小写
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 */
	public static boolean containsIgnoreCase(CharSequence[] array, CharSequence value) {
		return indexOfIgnoreCase(array, value) > INDEX_NOT_FOUND;
	}


	// ----------------------------------------------------------------------- index

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param <T>   数组类型
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 */
	public static <T> int indexOf(T[] array, Object value) {
		if (null != array) {
			for (int i = 0; i < array.length; i++) {
				if (ObjectUtil.equal(value, array[i])) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 */
	public static int indexOf(int[] array, int value) {
		if (null != array) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 */
	public static int indexOf(char[] array, char value) {
		if (null != array) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在最后的位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param <T>   数组类型
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 */
	public static <T> int lastIndexOf(T[] array, Object value) {
		if (null != array) {
			for (int i = array.length - 1; i >= 0; i--) {
				if (ObjectUtil.equal(value, array[i])) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素所在位置，忽略大小写，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 */
	public static int indexOfIgnoreCase(CharSequence[] array, CharSequence value) {
		if (null != array) {
			for (int i = 0; i < array.length; i++) {
				if (StrUtil.equalsIgnoreCase(array[i], value)) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	// ----------------------------------------------------------------------- 过滤

	/**
	 * 过滤<br>
	 * 过滤过程通过传入的Editor实现来返回需要的元素内容，这个Editor实现可以实现以下功能：
	 *
	 * <pre>
	 * 1、过滤出需要的对象，如果返回null表示这个元素对象抛弃
	 * 2、修改元素对象，返回集合中为修改后的对象
	 * </pre>
	 *
	 * @param <T>    数组元素类型
	 * @param array  数组
	 * @param editor 编辑器接口
	 * @return 过滤后的数组
	 */
	public static <T> T[] filter(T[] array, Editor<T> editor) {
		ArrayList<T> list = new ArrayList<>(array.length);
		T modified;
		for (T t : array) {
			modified = editor.edit(t);
			if (null != modified) {
				list.add(modified);
			}
		}
		return list.toArray(Arrays.copyOf(array, list.size()));
	}


	/**
	 * 过滤<br>
	 * 过滤过程通过传入的Filter实现来过滤返回需要的元素内容，这个Filter实现可以实现以下功能：
	 *
	 * <pre>
	 * 1、过滤出需要的对象，{@link Filter#accept(Object)}方法返回true的对象将被加入结果集合中
	 * </pre>
	 *
	 * @param <T>    数组元素类型
	 * @param array  数组
	 * @param filter 过滤器接口，用于定义过滤规则，null表示不过滤，返回原数组
	 * @return 过滤后的数组
	 */
	public static <T> T[] filter(T[] array, Filter<T> filter) {
		if (null == filter) {
			return array;
		}

		final ArrayList<T> list = new ArrayList<>(array.length);
		for (T t : array) {
			if (filter.accept(t)) {
				list.add(t);
			}
		}
		final T[] result = newArray(array.getClass().getComponentType(), list.size());
		return list.toArray(result);
	}


	// ----------------------------------------------------------------------- sub

	/**
	 * 获取子数组
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组
	 * @param start 开始位置（包括）
	 * @param end   结束位置（不包括）
	 * @return 新的数组
	 * @see Arrays#copyOfRange(Object[], int, int)
	 */
	public static <T> T[] sub(T[] array, int start, int end) {
		int length = length(array);
		if (start < 0) {
			start += length;
		}
		if (end < 0) {
			end += length;
		}
		if (start == length) {
			return newArray(array.getClass().getComponentType(), 0);
		}
		if (start > end) {
			int tmp = start;
			start = end;
			end = tmp;
		}
		if (end > length) {
			if (start >= length) {
				return newArray(array.getClass().getComponentType(), 0);
			}
			end = length;
		}
		return Arrays.copyOfRange(array, start, end);
	}

	/**
	 * 获取子数组
	 *
	 * @param array 数组
	 * @param start 开始位置（包括）
	 * @param end   结束位置（不包括）
	 * @return 新的数组
	 */
	public static Object[] sub(Object array, int start, int end) {
		return sub(array, start, end, 1);
	}

	/**
	 * 获取子数组
	 *
	 * @param array 数组
	 * @param start 开始位置（包括）
	 * @param end   结束位置（不包括）
	 * @param step  步进
	 * @return 新的数组
	 */
	public static Object[] sub(Object array, int start, int end, int step) {
		int length = length(array);
		if (start < 0) {
			start += length;
		}
		if (end < 0) {
			end += length;
		}
		if (start == length) {
			return new Object[0];
		}
		if (start > end) {
			int tmp = start;
			start = end;
			end = tmp;
		}
		if (end > length) {
			if (start >= length) {
				return new Object[0];
			}
			end = length;
		}

		if (step <= 1) {
			step = 1;
		}

		final ArrayList<Object> list = new ArrayList<>();
		for (int i = start; i < end; i += step) {
			list.add(get(array, i));
		}

		return list.toArray();
	}

	// ----------------------------------------------------------------------- 最大最小值

	/**
	 * 取最小值
	 *
	 * @param numberArray 数字数组
	 * @return 最小值
	 */
	public static long min(long... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		long min = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
			if (min > numberArray[i]) {
				min = numberArray[i];
			}
		}
		return min;
	}

	/**
	 * 取最小值
	 *
	 * @param numberArray 数字数组
	 * @return 最小值
	 */
	public static int min(int... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		int min = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
			if (min > numberArray[i]) {
				min = numberArray[i];
			}
		}
		return min;
	}

	/**
	 * 取最小值
	 *
	 * @param <T>         元素类型
	 * @param numberArray 数字数组
	 * @return 最小值
	 */
	public static <T extends Comparable<? super T>> T min(T[] numberArray) {
		return min(numberArray, null);
	}

	/**
	 * 取最小值
	 *
	 * @param <T>         元素类型
	 * @param numberArray 数字数组
	 * @param comparator  比较器，null按照默认比较
	 * @return 最小值
	 */
	public static <T extends Comparable<? super T>> T min(T[] numberArray, Comparator<T> comparator) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		T min = numberArray[0];
		for (T t : numberArray) {
			if (CompareUtil.compare(min, t, comparator) > 0) {
				min = t;
			}
		}
		return min;
	}

	/**
	 * 取最大值
	 *
	 * @param numberArray 数字数组
	 * @return 最大值
	 */
	public static long max(long... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		long max = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
			if (max < numberArray[i]) {
				max = numberArray[i];
			}
		}
		return max;
	}

	/**
	 * 取最大值
	 *
	 * @param numberArray 数字数组
	 * @return 最大值
	 */
	public static int max(int... numberArray) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		int max = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
			if (max < numberArray[i]) {
				max = numberArray[i];
			}
		}
		return max;
	}

	/**
	 * 取最大值
	 *
	 * @param <T>         元素类型
	 * @param numberArray 数字数组
	 * @return 最大值
	 */
	public static <T extends Comparable<? super T>> T max(T[] numberArray) {
		return max(numberArray, null);
	}

	/**
	 * 取最大值
	 *
	 * @param <T>         元素类型
	 * @param numberArray 数字数组
	 * @param comparator  比较器，null表示默认比较器
	 * @return 最大值
	 */
	public static <T extends Comparable<? super T>> T max(T[] numberArray, Comparator<T> comparator) {
		if (isEmpty(numberArray)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		T max = numberArray[0];
		for (int i = 1; i < numberArray.length; i++) {
			if (CompareUtil.compare(max, numberArray[i], comparator) < 0) {
				max = numberArray[i];
			}
		}
		return max;
	}

	// ----------------------------------------------------------------------- 顺序

	/**
	 * 交换数组中两个位置的值
	 *
	 * @param array  数组
	 * @param index1 位置1
	 * @param index2 位置2
	 */
	public static void swap(int[] array, int index1, int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Number array must not empty !");
		}
		int tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
	}

	/**
	 * 交换数组中两个位置的值
	 *
	 * @param <T>    元素类型
	 * @param array  数组
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 */
	public static <T> T[] swap(T[] array, int index1, int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Array must not empty !");
		}
		T tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
		return array;
	}

	/**
	 * 交换数组中两个位置的值
	 *
	 * @param array  数组对象
	 * @param index1 位置1
	 * @param index2 位置2
	 * @return 交换后的数组，与传入数组为同一对象
	 */
	public static Object swap(Object array, int index1, int index2) {
		if (isEmpty(array)) {
			throw new IllegalArgumentException("Array must not empty !");
		}
		Object tmp = get(array, index1);
		Array.set(array, index1, Array.get(array, index2));
		Array.set(array, index2, tmp);
		return array;
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param <T>   元素类型
	 * @param array 数组，会变更
	 * @return 打乱后的数组
	 */
	public static <T> T[] shuffle(T[] array) {
		return shuffle(array, RandomUtil.getRandom());
	}

	/**
	 * 打乱数组顺序，会变更原数组
	 *
	 * @param <T>    元素类型
	 * @param array  数组，会变更
	 * @param random 随机数生成器
	 * @return 打乱后的数组
	 */
	public static <T> T[] shuffle(T[] array, Random random) {
		if (array == null || random == null || array.length <= 1) {
			return array;
		}

		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i));
		}

		return array;
	}


	/**
	 * 检查数组是否升序，即array[i] &lt;= array[i+1]，若传入空数组，则返回false
	 *
	 * @param array 数组
	 * @return 数组是否升序
	 */
	public static boolean isSorted(int[] array) {
		return isSortedASC(array);
	}

	/**
	 * 检查数组是否升序，即array[i] &lt;= array[i+1]，若传入空数组，则返回false
	 *
	 * @param array 数组
	 * @return 数组是否升序
	 */
	public static boolean isSortedASC(int[] array) {
		if (array == null) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] > array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查数组是否降序，即array[i] &gt;= array[i+1]，若传入空数组，则返回false
	 *
	 * @param array 数组
	 * @return 数组是否降序
	 */
	public static boolean isSortedDESC(int[] array) {
		if (array == null) {
			return false;
		}

		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] < array[i + 1]) {
				return false;
			}
		}

		return true;
	}

	// ----------------------------------------------------------------------- join

	/**
	 * 以 conjunction 为分隔符将数组转换为字符串
	 *
	 * @param <T>         被处理的集合
	 * @param array       数组
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static <T> String join(T[] array, CharSequence conjunction) {
		return join(array, conjunction, null, null);
	}

	/**
	 * 以 conjunction 为分隔符将数组转换为字符串
	 *
	 * @param <T>       被处理的集合
	 * @param array     数组
	 * @param delimiter 分隔符
	 * @param prefix    每个元素添加的前缀，null表示不添加
	 * @param suffix    每个元素添加的后缀，null表示不添加
	 * @return 连接后的字符串
	 */
	public static <T> String join(T[] array, CharSequence delimiter, String prefix, String suffix) {
		if (null == array) {
			return null;
		}

		return StrJoiner.of(delimiter, prefix, suffix)
				// 每个元素都添加前后缀
				.setWrapElement(true)
				.append(array)
				.toString();
	}

	// -----------------------------------------------------------------------

	/**
	 * 数组或集合转String
	 *
	 * @param obj 集合或数组对象
	 * @return 数组字符串，与集合转字符串格式相同
	 */
	public static String toString(Object obj) {
		if (null == obj) {
			return null;
		}

		if (obj instanceof long[]) {
			return Arrays.toString((long[]) obj);
		} else if (obj instanceof int[]) {
			return Arrays.toString((int[]) obj);
		} else if (obj instanceof short[]) {
			return Arrays.toString((short[]) obj);
		} else if (obj instanceof char[]) {
			return Arrays.toString((char[]) obj);
		} else if (obj instanceof byte[]) {
			return Arrays.toString((byte[]) obj);
		} else if (obj instanceof boolean[]) {
			return Arrays.toString((boolean[]) obj);
		} else if (obj instanceof float[]) {
			return Arrays.toString((float[]) obj);
		} else if (obj instanceof double[]) {
			return Arrays.toString((double[]) obj);
		} else if (ArrayUtil.isArray(obj)) {
			// 对象数组
			try {
				return Arrays.deepToString((Object[]) obj);
			} catch (Exception ignore) {
				//ignore
			}
		}

		return obj.toString();
	}

	/**
	 * 判断两个数组是否相等，判断依据包括数组长度和每个元素都相等。
	 *
	 * @param array1 数组1
	 * @param array2 数组2
	 * @return 是否相等
	 */
	public static boolean equals(Object array1, Object array2) {
		if (array1 == array2) {
			return true;
		}
		if (hasNull(array1, array2)) {
			return false;
		}

		Assert.isTrue(isArray(array1), "First is not a Array !");
		Assert.isTrue(isArray(array2), "Second is not a Array !");

		if (array1 instanceof long[]) {
			return Arrays.equals((long[]) array1, (long[]) array2);
		} else if (array1 instanceof int[]) {
			return Arrays.equals((int[]) array1, (int[]) array2);
		} else if (array1 instanceof short[]) {
			return Arrays.equals((short[]) array1, (short[]) array2);
		} else if (array1 instanceof char[]) {
			return Arrays.equals((char[]) array1, (char[]) array2);
		} else if (array1 instanceof byte[]) {
			return Arrays.equals((byte[]) array1, (byte[]) array2);
		} else if (array1 instanceof double[]) {
			return Arrays.equals((double[]) array1, (double[]) array2);
		} else if (array1 instanceof float[]) {
			return Arrays.equals((float[]) array1, (float[]) array2);
		} else if (array1 instanceof boolean[]) {
			return Arrays.equals((boolean[]) array1, (boolean[]) array2);
		} else {
			// Not an array of primitives
			return Arrays.deepEquals((Object[]) array1, (Object[]) array2);
		}
	}

	/**
	 * 获取数组对象的元素类型
	 *
	 * @param array 数组对象
	 * @return 元素类型
	 */
	public static Class<?> getComponentType(Object array) {
		return null == array ? null : array.getClass().getComponentType();
	}

	/**
	 * 获取数组对象的元素类型
	 *
	 * @param arrayClass 数组类
	 * @return 元素类型
	 */
	public static Class<?> getComponentType(Class<?> arrayClass) {
		return null == arrayClass ? null : arrayClass.getComponentType();
	}

	/**
	 * 根据数组元素类型，获取数组的类型<br>
	 * 方法是通过创建一个空数组从而获取其类型
	 *
	 * @param componentType 数组元素类型
	 * @return 数组类型
	 */
	public static Class<?> getArrayType(Class<?> componentType) {
		return Array.newInstance(componentType, 0).getClass();
	}

	/**
	 * 包装 {@link System#arraycopy(Object, int, Object, int, int)}<br>
	 * 数组复制
	 *
	 * @param src     源数组
	 * @param srcPos  源数组开始位置
	 * @param dest    目标数组
	 * @param destPos 目标数组开始位置
	 * @param length  拷贝数组长度
	 * @return 目标数组
	 */
	public static Object copy(Object src, int srcPos, Object dest, int destPos, int length) {
		//noinspection SuspiciousSystemArraycopy
		System.arraycopy(src, srcPos, dest, destPos, length);
		return dest;
	}

	/**
	 * 包装 {@link System#arraycopy(Object, int, Object, int, int)}<br>
	 * 数组复制，缘数组和目标数组都是从位置0开始复制
	 *
	 * @param src    源数组
	 * @param dest   目标数组
	 * @param length 拷贝数组长度
	 * @return 目标数组
	 */
	public static Object copy(Object src, Object dest, int length) {
		//noinspection SuspiciousSystemArraycopy
		System.arraycopy(src, 0, dest, 0, length);
		return dest;
	}

}
