package com.yyxnb.android.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 集合相关工具类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/3/24
 */
public class CollUtil {

	/**
	 * 集合是否为空
	 *
	 * @param collection 集合
	 * @return 是否为空
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * 如果给定集合为空，返回默认集合
	 *
	 * @param <T>               集合类型
	 * @param <E>               集合元素类型
	 * @param collection        集合
	 * @param defaultCollection 默认数组
	 * @return 非空（empty）的原集合或默认集合
	 */
	public static <T extends Collection<E>, E> T defaultIfEmpty(T collection, T defaultCollection) {
		return isEmpty(collection) ? defaultCollection : collection;
	}

	/**
	 * Map是否为空
	 *
	 * @param map 集合
	 * @return 是否为空
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return UtilInner.isEmpty(map);
	}

	/**
	 * Iterable是否为空
	 *
	 * @param iterable Iterable对象
	 * @return 是否为空
	 */
	public static boolean isEmpty(Iterable<?> iterable) {
		return UtilInner.isEmpty(iterable);
	}

	/**
	 * Enumeration是否为空
	 *
	 * @param enumeration {@link Enumeration}
	 * @return 是否为空
	 */
	public static boolean isEmpty(Enumeration<?> enumeration) {
		return null == enumeration || !enumeration.hasMoreElements();
	}

	// ----------------------------------------------- isNotEmpty

	/**
	 * 集合是否为非空
	 *
	 * @param collection 集合
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}

	/**
	 * Map是否为非空
	 *
	 * @param map 集合
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(Map<?, ?> map) {
		return !UtilInner.isEmpty(map);
	}

	/**
	 * Iterable是否为空
	 *
	 * @param iterable Iterable对象
	 * @return 是否为空
	 */
	public static boolean isNotEmpty(Iterable<?> iterable) {
		return !UtilInner.isEmpty(iterable);
	}

	// -----------------------------------------------  new HashSet

	/**
	 * 新建一个HashSet
	 *
	 * @param <T> 集合元素类型
	 * @param ts  元素数组
	 * @return HashSet对象
	 */
	@SafeVarargs
	public static <T> HashSet<T> newHashSet(T... ts) {
		return set(false, ts);
	}

	/**
	 * 新建一个LinkedHashSet
	 *
	 * @param <T> 集合元素类型
	 * @param ts  元素数组
	 * @return HashSet对象
	 */
	@SafeVarargs
	public static <T> LinkedHashSet<T> newLinkedHashSet(T... ts) {
		return (LinkedHashSet<T>) set(true, ts);
	}

	/**
	 * 新建一个HashSet
	 *
	 * @param <T>      集合元素类型
	 * @param isSorted 是否有序，有序返回 {@link LinkedHashSet}，否则返回 {@link HashSet}
	 * @param ts       元素数组
	 * @return HashSet对象
	 */
	@SafeVarargs
	public static <T> HashSet<T> set(boolean isSorted, T... ts) {
		if (null == ts) {
			return isSorted ? new LinkedHashSet<>() : new HashSet<>();
		}
		int initialCapacity = Math.max((int) (ts.length / .75f) + 1, 16);
		final HashSet<T> set = isSorted ? new LinkedHashSet<>(initialCapacity) : new HashSet<>(initialCapacity);
		Collections.addAll(set, ts);
		return set;
	}

	/**
	 * 新建一个HashSet
	 *
	 * @param <T>        集合元素类型
	 * @param collection 集合
	 * @return HashSet对象
	 */
	public static <T> HashSet<T> newHashSet(Collection<T> collection) {
		return newHashSet(false, collection);
	}

	/**
	 * 新建一个HashSet
	 *
	 * @param <T>        集合元素类型
	 * @param isSorted   是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
	 * @param collection 集合，用于初始化Set
	 * @return HashSet对象
	 */
	public static <T> HashSet<T> newHashSet(boolean isSorted, Collection<T> collection) {
		return isSorted ? new LinkedHashSet<>(collection) : new HashSet<>(collection);
	}

	/**
	 * 新建一个HashSet
	 *
	 * @param <T>      集合元素类型
	 * @param isSorted 是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
	 * @param iter     {@link Iterator}
	 * @return HashSet对象
	 */
	public static <T> HashSet<T> newHashSet(boolean isSorted, Iterator<T> iter) {
		if (null == iter) {
			return set(isSorted, (T[]) null);
		}
		final HashSet<T> set = isSorted ? new LinkedHashSet<>() : new HashSet<>();
		while (iter.hasNext()) {
			set.add(iter.next());
		}
		return set;
	}

	/**
	 * 新建一个HashSet
	 *
	 * @param <T>         集合元素类型
	 * @param isSorted    是否有序，有序返回 {@link LinkedHashSet}，否则返回{@link HashSet}
	 * @param enumeration {@link Enumeration}
	 * @return HashSet对象
	 */
	public static <T> HashSet<T> newHashSet(boolean isSorted, Enumeration<T> enumeration) {
		if (null == enumeration) {
			return set(isSorted, (T[]) null);
		}
		final HashSet<T> set = isSorted ? new LinkedHashSet<>() : new HashSet<>();
		while (enumeration.hasMoreElements()) {
			set.add(enumeration.nextElement());
		}
		return set;
	}

	// ----------------------------------------------- List

	/**
	 * 新建一个空List
	 *
	 * @param <T>      集合元素类型
	 * @param isLinked 是否新建LinkedList
	 * @return List对象
	 */
	public static <T> List<T> list(boolean isLinked) {
		return isLinked ? new LinkedList<>() : new ArrayList<>();
	}

	/**
	 * 新建一个List
	 *
	 * @param <T>      集合元素类型
	 * @param isLinked 是否新建LinkedList
	 * @param values   数组
	 * @return List对象
	 */
	@SafeVarargs
	public static <T> List<T> list(boolean isLinked, T... values) {
		if (UtilInner.isEmpty(values)) {
			return list(isLinked);
		}
		final List<T> arrayList = isLinked ? new LinkedList<>() : new ArrayList<>(values.length);
		Collections.addAll(arrayList, values);
		return arrayList;
	}

	/**
	 * 新建一个List
	 *
	 * @param <T>        集合元素类型
	 * @param isLinked   是否新建LinkedList
	 * @param collection 集合
	 * @return List对象
	 */
	public static <T> List<T> list(boolean isLinked, Collection<T> collection) {
		if (null == collection) {
			return list(isLinked);
		}
		return isLinked ? new LinkedList<>(collection) : new ArrayList<>(collection);
	}

	/**
	 * 数组转为ArrayList
	 *
	 * @param <T>    集合元素类型
	 * @param values 数组
	 * @return ArrayList对象
	 */
	@SafeVarargs
	public static <T> ArrayList<T> toList(T... values) {
		return (ArrayList<T>) list(false, values);
	}

	/**
	 * 新建一个ArrayList
	 *
	 * @param <T>        集合元素类型
	 * @param collection 集合
	 * @return ArrayList对象
	 */
	public static <T> ArrayList<T> newArrayList(Collection<T> collection) {
		return (ArrayList<T>) list(false, collection);
	}

	/**
	 * 新建一个ArrayList
	 *
	 * @param <T>    集合元素类型
	 * @param values 数组
	 * @return ArrayList对象
	 * @see #toList(Object[])
	 */
	@SafeVarargs
	public static <T> ArrayList<T> newArrayList(T... values) {
		return (ArrayList<T>) list(false, values);
	}

	/**
	 * 新建一个ArrayList<br>
	 * 提供的参数为null时返回空{@link ArrayList}
	 *
	 * @param <T>      集合元素类型
	 * @param iterable {@link Iterable}
	 * @return ArrayList对象
	 */
	public static <T> ArrayList<T> newArrayList(Iterable<T> iterable) {
		return (ArrayList<T>) list(false, iterable);
	}

	// ----------------------------------------------- get


	/**
	 * 获取集合中指定下标的元素值，下标可以为负数，例如-1表示最后一个元素<br>
	 * 如果元素越界，返回null
	 *
	 * @param <T>        元素类型
	 * @param collection 集合
	 * @param index      下标，支持负数
	 * @return 元素值
	 */
	public static <T> T get(Collection<T> collection, int index) {
		if (null == collection) {
			return null;
		}

		final int size = collection.size();
		if (0 == size) {
			return null;
		}

		if (index < 0) {
			index += size;
		}

		// 检查越界
		if (index >= size || index < 0) {
			return null;
		}

		if (collection instanceof List) {
			final List<T> list = ((List<T>) collection);
			return list.get(index);
		} else {
			int i = 0;
			for (T t : collection) {
				if (i > index) {
					break;
				} else if (i == index) {
					return t;
				}
				i++;
			}
		}
		return null;
	}

	/**
	 * 获取集合中指定多个下标的元素值，下标可以为负数，例如-1表示最后一个元素
	 *
	 * @param <T>        元素类型
	 * @param collection 集合
	 * @param indexes    下标，支持负数
	 * @return 元素值列表
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getAny(Collection<T> collection, int... indexes) {
		final int size = collection.size();
		final ArrayList<T> result = new ArrayList<>();
		if (collection instanceof List) {
			final List<T> list = ((List<T>) collection);
			for (int index : indexes) {
				if (index < 0) {
					index += size;
				}
				result.add(list.get(index));
			}
		} else {
			final Object[] array = collection.toArray();
			for (int index : indexes) {
				if (index < 0) {
					index += size;
				}
				result.add((T) array[index]);
			}
		}
		return result;
	}

	/**
	 * 获取集合的最后一个元素
	 *
	 * @param <T>        集合元素类型
	 * @param collection {@link Collection}
	 * @return 最后一个元素
	 */
	public static <T> T getLast(Collection<T> collection) {
		return get(collection, -1);
	}

	public static Map<Object, Integer> getCardinalityMap(final Collection<?> coll) {
		Map<Object, Integer> count = new HashMap<>();
		if (coll == null) {
			return count;
		}
		for (Object obj : coll) {
			count.merge(obj, 1, Integer::sum);
		}
		return count;
	}

	private static int getFreq(final Object obj, final Map<?, ?> freqMap) {
		Integer count = (Integer) freqMap.get(obj);
		if (count != null) {
			return count;
		}
		return 0;
	}

	// ----------------------------------------------- add

	/**
	 * 加入全部
	 *
	 * @param <T>        集合元素类型
	 * @param collection 被加入的集合 {@link Collection}
	 * @param iterator   要加入的{@link Iterator}
	 * @return 原集合
	 */
	public static <T> Collection<T> addAll(Collection<T> collection, Iterator<T> iterator) {
		if (null != collection && null != iterator) {
			while (iterator.hasNext()) {
				collection.add(iterator.next());
			}
		}
		return collection;
	}

	/**
	 * 加入全部
	 *
	 * @param <T>        集合元素类型
	 * @param collection 被加入的集合 {@link Collection}
	 * @param iterable   要加入的内容{@link Iterable}
	 * @return 原集合
	 */
	public static <T> Collection<T> addAll(Collection<T> collection, Iterable<T> iterable) {
		if (iterable == null) {
			return collection;
		}
		return addAll(collection, iterable.iterator());
	}

	/**
	 * 填充List，以达到最小长度
	 *
	 * @param <T>    集合元素类型
	 * @param list   列表
	 * @param minLen 最小长度
	 * @param padObj 填充的对象
	 */
	public static <T> void padRight(Collection<T> list, int minLen, T padObj) {
		Objects.requireNonNull(list);
		for (int i = list.size(); i < minLen; i++) {
			list.add(padObj);
		}
	}

	// -----------------------------------------------

	/**
	 * 删除集合元素
	 *
	 * @param collection 目标集合
	 * @param remove     要删除的集合元素
	 * @param <E>
	 * @return
	 */
	public static <E> Collection<E> removeAll(Collection<E> collection, Collection<E> remove) {
		if (collection == null) {
			return new ArrayList<>();
		}
		if (remove == null) {
			return new ArrayList<>(collection);
		}
		List<E> list = new ArrayList<>();
		for (E obj : collection) {
			if (!remove.contains(obj)) {
				list.add(obj);
			}
		}
		return list;
	}

	/**
	 * 保留集合元素
	 *
	 * @param collection 目标集合
	 * @param retain     要保留的集合元素
	 * @param <E>
	 * @return
	 */
	public static <E> Collection<E> retainAll(Collection<E> collection, Collection<E> retain) {
		if (collection == null || retain == null) {
			return new ArrayList<>();
		}
		List<E> list = new ArrayList<>();
		for (E item : collection) {
			if (retain.contains(item)) {
				list.add(item);
			}
		}
		return list;
	}

	// -----------------------------------------------

	/**
	 * 获取并集
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static Collection<?> union(final Collection<?> a, final Collection<?> b) {
		if (a == null && b == null) {
			return new ArrayList<>();
		}
		if (a == null) {
			return new ArrayList<Object>(b);
		}
		if (b == null) {
			return new ArrayList<Object>(a);
		}
		ArrayList<Object> list = new ArrayList<>();
		Map<Object, Integer> mapA = getCardinalityMap(a);
		Map<Object, Integer> mapB = getCardinalityMap(b);
		Set<Object> elts = new HashSet<>(a);
		elts.addAll(b);
		for (Object obj : elts) {
			for (int i = 0, m = Math.max(getFreq(obj, mapA), getFreq(obj, mapB)); i < m; i++) {
				list.add(obj);
			}
		}
		return list;
	}

	/**
	 * 获取交集
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static Collection<?> intersection(final Collection<?> a, final Collection<?> b) {
		if (a == null || b == null) {
			return new ArrayList<>();
		}
		ArrayList<Object> list = new ArrayList<>();
		Map<?, ?> mapA = getCardinalityMap(a);
		Map<?, ?> mapB = getCardinalityMap(b);
		Set<Object> elts = new HashSet<>(a);
		elts.addAll(b);
		for (Object obj : elts) {
			for (int i = 0, m = Math.min(getFreq(obj, mapA), getFreq(obj, mapB)); i < m; i++) {
				list.add(obj);
			}
		}
		return list;
	}

	/**
	 * 获取并集减交集
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static Collection<?> disjunction(final Collection<?> a, final Collection<?> b) {
		if (a == null && b == null) {
			return new ArrayList<>();
		}
		if (a == null) {
			return new ArrayList<Object>(b);
		}
		if (b == null) {
			return new ArrayList<Object>(a);
		}
		ArrayList<Object> list = new ArrayList<>();
		Map<?, ?> mapA = getCardinalityMap(a);
		Map<?, ?> mapB = getCardinalityMap(b);
		Set<Object> elts = new HashSet<Object>(a);
		elts.addAll(b);
		for (Object obj : elts) {
			for (int i = 0, m = ((Math.max(getFreq(obj, mapA), getFreq(obj, mapB)))
					- (Math.min(getFreq(obj, mapA), getFreq(obj, mapB)))); i < m; i++) {
				list.add(obj);
			}
		}
		return list;
	}

	/**
	 * 获取差集
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static Collection<?> subtract(final Collection<?> a, final Collection<?> b) {
		if (a == null) {
			return new ArrayList<>();
		}
		if (b == null) {
			return new ArrayList<Object>(a);
		}
		ArrayList<Object> list = new ArrayList<>(a);
		for (Object o : b) {
			list.remove(o);
		}
		return list;
	}

}
