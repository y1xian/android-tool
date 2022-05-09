package com.yyxnb.lib.java.collection;

import com.yyxnb.lib.java.ReflectUtil;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Map相关工具类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/3/24
 */
public class MapUtil {

	/**
	 * 默认初始大小
	 */
	public static final int DEFAULT_INITIAL_CAPACITY = 16;
	/**
	 * 默认增长因子，当Map的size达到 容量*增长因子时，开始扩充Map
	 */
	public static final float DEFAULT_LOAD_FACTOR = 0.75f;

	/**
	 * Map是否为空
	 *
	 * @param map 集合
	 * @return 是否为空
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return null == map || map.isEmpty();
	}

	/**
	 * Map是否为非空
	 *
	 * @param map 集合
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(Map<?, ?> map) {
		return null != map && !map.isEmpty();
	}

	/**
	 * 如果提供的集合为{@code null}，返回一个不可变的默认空集合，否则返回原集合<br>
	 * 空集合使用{@link Collections#emptyMap()}
	 *
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param set 提供的集合，可能为null
	 * @return 原集合，若为null返回空集合
	 */
	public static <K, V> Map<K, V> emptyIfNull(Map<K, V> set) {
		return (null == set) ? Collections.emptyMap() : set;
	}

	/**
	 * 如果给定Map为空，返回默认Map
	 *
	 * @param <T>        集合类型
	 * @param <K>        键类型
	 * @param <V>        值类型
	 * @param map        Map
	 * @param defaultMap 默认Map
	 * @return 非空（empty）的原Map或默认Map
	 */
	public static <T extends Map<K, V>, K, V> T defaultIfEmpty(T map, T defaultMap) {
		return isEmpty(map) ? defaultMap : map;
	}

	// ----------------------------------------------------------------------- new

	/**
	 * 新建一个HashMap
	 *
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @return HashMap对象
	 */
	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<>();
	}


	/**
	 * 新建一个HashMap
	 *
	 * @param <K>     Key类型
	 * @param <V>     Value类型
	 * @param size    初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75 + 1
	 * @param isOrder Map的Key是否有序，有序返回 {@link LinkedHashMap}，否则返回 {@link HashMap}
	 * @return HashMap对象
	 */
	public static <K, V> HashMap<K, V> newHashMap(int size, boolean isOrder) {
		int initialCapacity = (int) (size / DEFAULT_LOAD_FACTOR) + 1;
		return isOrder ? new LinkedHashMap<>(initialCapacity) : new HashMap<>(initialCapacity);
	}

	/**
	 * 新建一个HashMap
	 *
	 * @param <K>  Key类型
	 * @param <V>  Value类型
	 * @param size 初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75 + 1
	 * @return HashMap对象
	 */
	public static <K, V> HashMap<K, V> newHashMap(int size) {
		return newHashMap(size, false);
	}

	/**
	 * 新建一个HashMap
	 *
	 * @param <K>     Key类型
	 * @param <V>     Value类型
	 * @param isOrder Map的Key是否有序，有序返回 {@link LinkedHashMap}，否则返回 {@link HashMap}
	 * @return HashMap对象
	 */
	public static <K, V> HashMap<K, V> newHashMap(boolean isOrder) {
		return newHashMap(DEFAULT_INITIAL_CAPACITY, isOrder);
	}

	/**
	 * 新建TreeMap，Key有序的Map
	 *
	 * @param <K>        key的类型
	 * @param <V>        value的类型
	 * @param comparator Key比较器
	 * @return TreeMap
	 */
	public static <K, V> TreeMap<K, V> newTreeMap(Comparator<? super K> comparator) {
		return new TreeMap<>(comparator);
	}

	/**
	 * 新建TreeMap，Key有序的Map
	 *
	 * @param <K>        key的类型
	 * @param <V>        value的类型
	 * @param map        Map
	 * @param comparator Key比较器
	 * @return TreeMap
	 */
	public static <K, V> TreeMap<K, V> newTreeMap(Map<K, V> map, Comparator<? super K> comparator) {
		final TreeMap<K, V> treeMap = new TreeMap<>(comparator);
		if (isNotEmpty(map)) {
			treeMap.putAll(map);
		}
		return treeMap;
	}

	/**
	 * 创建键不重复Map
	 *
	 * @param <K>  key的类型
	 * @param <V>  value的类型
	 * @param size 初始容量
	 * @return {@link IdentityHashMap}
	 */
	public static <K, V> Map<K, V> newIdentityMap(int size) {
		return new IdentityHashMap<>(size);
	}


	/**
	 * 新建一个初始容量为{@link MapUtil#DEFAULT_INITIAL_CAPACITY} 的ConcurrentHashMap
	 *
	 * @param <K> key的类型
	 * @param <V> value的类型
	 * @return ConcurrentHashMap
	 */
	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
		return new ConcurrentHashMap<>(DEFAULT_INITIAL_CAPACITY);
	}

	/**
	 * 新建一个ConcurrentHashMap
	 *
	 * @param size 初始容量，当传入的容量小于等于0时，容量为{@link MapUtil#DEFAULT_INITIAL_CAPACITY}
	 * @param <K>  key的类型
	 * @param <V>  value的类型
	 * @return ConcurrentHashMap
	 */
	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(int size) {
		final int initCapacity = size <= 0 ? DEFAULT_INITIAL_CAPACITY : size;
		return new ConcurrentHashMap<>(initCapacity);
	}

	/**
	 * 传入一个Map将其转化为ConcurrentHashMap类型
	 *
	 * @param map map
	 * @param <K> key的类型
	 * @param <V> value的类型
	 * @return ConcurrentHashMap
	 */
	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(Map<K, V> map) {
		if (isEmpty(map)) {
			return new ConcurrentHashMap<>(DEFAULT_INITIAL_CAPACITY);
		}
		return new ConcurrentHashMap<>(map);
	}

	// ----------------------------------------------------------------------- remove

	/**
	 * 去除Map中值为{@code null}的键值对<br>
	 * 注意：此方法在传入的Map上直接修改。
	 *
	 * @param <K> key的类型
	 * @param <V> value的类型
	 * @param map Map
	 * @return map
	 */
	public static <K, V> Map<K, V> removeNullValue(Map<K, V> map) {
		if (isEmpty(map)) {
			return map;
		}

		final Iterator<Entry<K, V>> iter = map.entrySet().iterator();
		Entry<K, V> entry;
		while (iter.hasNext()) {
			entry = iter.next();
			if (null == entry.getValue()) {
				iter.remove();
			}
		}
		return map;
	}

	/**
	 * 清除一个或多个Map集合内的元素，每个Map调用clear()方法
	 *
	 * @param maps 一个或多个Map
	 */
	public static void clear(Map<?, ?>... maps) {
		for (Map<?, ?> map : maps) {
			if (isNotEmpty(map)) {
				map.clear();
			}
		}
	}

	// -----------------------------------------------------------------------

	/**
	 * 创建Map<br>
	 * 传入抽象Map{@link AbstractMap}和{@link Map}类将默认创建{@link HashMap}
	 *
	 * @param <K>     map键类型
	 * @param <V>     map值类型
	 * @param mapType map类型
	 * @return {@link Map}实例
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> createMap(Class<?> mapType) {
		if (mapType.isAssignableFrom(AbstractMap.class)) {
			return new HashMap<>();
		} else {
			return (Map<K, V>) ReflectUtil.newInstance(mapType);
		}
	}

	/**
	 * 将键和值转换为{@link AbstractMap.SimpleImmutableEntry}<br>
	 * 返回的Entry不可变
	 *
	 * @param key   键
	 * @param value 值
	 * @param <K>   键类型
	 * @param <V>   值类型
	 * @return {@link AbstractMap.SimpleImmutableEntry}
	 */
	public static <K, V> Map.Entry<K, V> entry(K key, V value) {
		return entry(key, value, true);
	}

	/**
	 * 将键和值转换为{@link AbstractMap.SimpleEntry} 或者 {@link AbstractMap.SimpleImmutableEntry}
	 *
	 * @param key         键
	 * @param value       值
	 * @param <K>         键类型
	 * @param <V>         值类型
	 * @param isImmutable 是否不可变Entry
	 * @return {@link AbstractMap.SimpleEntry} 或者 {@link AbstractMap.SimpleImmutableEntry}
	 */
	public static <K, V> Map.Entry<K, V> entry(K key, V value, boolean isImmutable) {
		return isImmutable ?
				new AbstractMap.SimpleEntry<>(key, value) :
				new AbstractMap.SimpleImmutableEntry<>(key, value);
	}

}
