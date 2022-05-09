package com.yyxnb.lib.java.collection;

import com.yyxnb.lib.java.Assert;
import com.yyxnb.lib.java.Matcher;
import com.yyxnb.lib.java.ReflectUtil;
import com.yyxnb.lib.java.exceptions.UtilException;
import com.yyxnb.lib.java.func.Func1;
import com.yyxnb.lib.java.text.StrJoiner;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Iterable 和 Iterator 相关工具类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/3/24
 */
public class IterUtil {


	/**
	 * Iterable是否为空
	 *
	 * @param iterable Iterable对象
	 * @return 是否为空
	 */
	public static boolean isEmpty(Iterable<?> iterable) {
		return null == iterable || isEmpty(iterable.iterator());
	}

	/**
	 * Iterator是否为空
	 *
	 * @param iterator Iterator对象
	 * @return 是否为空
	 */
	public static boolean isEmpty(Iterator<?> iterator) {
		return null == iterator || !iterator.hasNext();
	}

	/**
	 * Iterable是否为空
	 *
	 * @param iterable Iterable对象
	 * @return 是否为空
	 */
	public static boolean isNotEmpty(Iterable<?> iterable) {
		return null != iterable && isNotEmpty(iterable.iterator());
	}

	/**
	 * Iterator是否为空
	 *
	 * @param iterator Iterator对象
	 * @return 是否为空
	 */
	public static boolean isNotEmpty(Iterator<?> iterator) {
		return null != iterator && iterator.hasNext();
	}

	/**
	 * 是否包含{@code null}元素
	 *
	 * @param iterator 被检查的{@link Iterable}对象，如果为{@code null} 返回true
	 * @return 是否包含{@code null}元素
	 */
	public static boolean hasNull(Iterable<?> iterator) {
		return hasNull(null == iterator ? null : iterator.iterator());
	}

	/**
	 * 是否包含{@code null}元素
	 *
	 * @param iterator 被检查的{@link Iterator}对象，如果为{@code null} 返回true
	 * @return 是否包含{@code null}元素
	 */
	public static boolean hasNull(Iterator<?> iterator) {
		if (null == iterator) {
			return true;
		}
		while (iterator.hasNext()) {
			if (null == iterator.next()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据集合返回一个元素计数的 {@link Map}<br>
	 * 所谓元素计数就是假如这个集合中某个元素出现了n次，那将这个元素做为key，n做为value<br>
	 * 例如：[a,b,c,c,c] 得到：<br>
	 * a: 1<br>
	 * b: 1<br>
	 * c: 3<br>
	 *
	 * @param <T>  集合元素类型
	 * @param iter {@link Iterable}，如果为null返回一个空的Map
	 * @return {@link Map}
	 * @deprecated 如果对象同时实现Iterable和Iterator接口会产生歧义，请使用CollUtil.countMap
	 */
	@Deprecated
	public static <T> Map<T, Integer> countMap(Iterable<T> iter) {
		return countMap(null == iter ? null : iter.iterator());
	}

	/**
	 * 根据集合返回一个元素计数的 {@link Map}<br>
	 * 所谓元素计数就是假如这个集合中某个元素出现了n次，那将这个元素做为key，n做为value<br>
	 * 例如：[a,b,c,c,c] 得到：<br>
	 * a: 1<br>
	 * b: 1<br>
	 * c: 3<br>
	 *
	 * @param <T>  集合元素类型
	 * @param iter {@link Iterator}，如果为null返回一个空的Map
	 * @return {@link Map}
	 */
	public static <T> Map<T, Integer> countMap(Iterator<T> iter) {
		final HashMap<T, Integer> countMap = new HashMap<>();
		if (null != iter) {
			Integer count;
			T t;
			while (iter.hasNext()) {
				t = iter.next();
				count = countMap.get(t);
				if (null == count) {
					countMap.put(t, 1);
				} else {
					countMap.put(t, count + 1);
				}
			}
		}
		return countMap;
	}

	/**
	 * 获取{@link Iterator}
	 *
	 * @param iterable {@link Iterable}
	 * @param <T>      元素类型
	 * @return 当iterable为null返回{@code null}，否则返回对应的{@link Iterator}
	 */
	public static <T> Iterator<T> getIter(Iterable<T> iterable) {
		return null == iterable ? null : iterable.iterator();
	}

	// ----------------------------------------------------------------------- join


	/**
	 * 以 conjunction 为分隔符将集合转换为字符串<br>
	 * 如果集合元素为数组、{@link Iterable}或{@link Iterator}，则递归组合其为字符串
	 *
	 * @param <T>         集合元素类型
	 * @param iterator    集合
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 */
	public static <T> String join(Iterator<T> iterator, CharSequence conjunction) {
		return StrJoiner.of(conjunction).append(iterator).toString();
	}

	/**
	 * 以 conjunction 为分隔符将集合转换为字符串<br>
	 * 如果集合元素为数组、{@link Iterable}或{@link Iterator}，则递归组合其为字符串
	 *
	 * @param <T>         集合元素类型
	 * @param iterator    集合
	 * @param conjunction 分隔符
	 * @param prefix      每个元素添加的前缀，null表示不添加
	 * @param suffix      每个元素添加的后缀，null表示不添加
	 * @return 连接后的字符串
	 */
	public static <T> String join(Iterator<T> iterator, CharSequence conjunction, String prefix, String suffix) {
		return StrJoiner.of(conjunction, prefix, suffix)
				// 每个元素都添加前后缀
				.setWrapElement(true)
				.append(iterator)
				.toString();
	}

	/**
	 * 以 conjunction 为分隔符将集合转换为字符串<br>
	 * 如果集合元素为数组、{@link Iterable}或{@link Iterator}，则递归组合其为字符串
	 *
	 * @param <T>         集合元素类型
	 * @param iterator    集合
	 * @param conjunction 分隔符
	 * @param func        集合元素转换器，将元素转换为字符串
	 * @return 连接后的字符串
	 */
	public static <T> String join(Iterator<T> iterator, CharSequence conjunction, Function<T, ? extends CharSequence> func) {
		if (null == iterator) {
			return null;
		}
		return StrJoiner.of(conjunction).append(iterator, func).toString();
	}

	// ----------------------------------------------------------------------- to

	/**
	 * Iterator转List<br>
	 * 不判断，直接生成新的List
	 *
	 * @param <E>  元素类型
	 * @param iter {@link Iterator}
	 * @return List
	 */
	public static <E> List<E> toList(Iterable<E> iter) {
		if (null == iter) {
			return null;
		}
		return toList(iter.iterator());
	}

	/**
	 * Iterator转List<br>
	 * 不判断，直接生成新的List
	 *
	 * @param <E>  元素类型
	 * @param iter {@link Iterator}
	 * @return List
	 */
	public static <E> List<E> toList(Iterator<E> iter) {
		return ListUtil.toList(iter);
	}

	/**
	 * Iterator转换为Map，转换规则为：<br>
	 * 按照keyFunc函数规则根据元素对象生成Key，元素作为值
	 *
	 * @param <K>      Map键类型
	 * @param <V>      Map值类型
	 * @param iterator 数据列表
	 * @param map      Map对象，转换后的键值对加入此Map，通过传入此对象自定义Map类型
	 * @param keyFunc  生成key的函数
	 * @return 生成的map
	 */
	public static <K, V> Map<K, V> toMap(Iterator<V> iterator, Map<K, V> map, Func1<V, K> keyFunc) {
		return toMap(iterator, map, keyFunc, (value) -> value);
	}

	/**
	 * 集合转换为Map，转换规则为：<br>
	 * 按照keyFunc函数规则根据元素对象生成Key，按照valueFunc函数规则根据元素对象生成value组成新的Map
	 *
	 * @param <K>       Map键类型
	 * @param <V>       Map值类型
	 * @param <E>       元素类型
	 * @param iterator  数据列表
	 * @param map       Map对象，转换后的键值对加入此Map，通过传入此对象自定义Map类型
	 * @param keyFunc   生成key的函数
	 * @param valueFunc 生成值的策略函数
	 * @return 生成的map
	 */
	public static <K, V, E> Map<K, V> toMap(Iterator<E> iterator, Map<K, V> map, Func1<E, K> keyFunc, Func1<E, V> valueFunc) {
		if (null == iterator) {
			return map;
		}

		if (null == map) {
			map = MapUtil.newHashMap(true);
		}

		E element;
		while (iterator.hasNext()) {
			element = iterator.next();
			try {
				map.put(keyFunc.call(element), valueFunc.call(element));
			} catch (Exception e) {
				throw new UtilException(e);
			}
		}
		return map;
	}

	// ----------------------------------------------------------------------- get

	/**
	 * 遍历{@link Iterator}，获取指定index位置的元素
	 *
	 * @param iterator {@link Iterator}
	 * @param index    位置
	 * @param <E>      元素类型
	 * @return 元素，找不到元素返回{@code null}
	 */
	public static <E> E get(final Iterator<E> iterator, int index) throws IndexOutOfBoundsException {
		Assert.isTrue(index >= 0, "[index] must be >= 0");
		while (iterator.hasNext()) {
			index--;
			if (-1 == index) {
				return iterator.next();
			}
			iterator.next();
		}
		return null;
	}

	/**
	 * 获取集合的第一个元素，如果集合为空（null或者空集合），返回{@code null}
	 *
	 * @param <T>      集合元素类型
	 * @param iterable {@link Iterable}
	 * @return 第一个元素，为空返回{@code null}
	 */
	public static <T> T getFirst(Iterable<T> iterable) {
		return getFirst(getIter(iterable));
	}

	/**
	 * 获取集合的第一个非空元素
	 *
	 * @param <T>      集合元素类型
	 * @param iterable {@link Iterable}
	 * @return 第一个元素
	 */
	public static <T> T getFirstNoneNull(Iterable<T> iterable) {
		if (null == iterable) {
			return null;
		}
		return getFirstNoneNull(iterable.iterator());
	}

	/**
	 * 获取集合的第一个元素
	 *
	 * @param <T>      集合元素类型
	 * @param iterator {@link Iterator}
	 * @return 第一个元素
	 */
	public static <T> T getFirst(Iterator<T> iterator) {
		return get(iterator, 0);
	}

	/**
	 * 获取集合的第一个非空元素
	 *
	 * @param <T>      集合元素类型
	 * @param iterator {@link Iterator}
	 * @return 第一个非空元素，null表示未找到
	 */
	public static <T> T getFirstNoneNull(Iterator<T> iterator) {
		return firstMatch(iterator, Objects::nonNull);
	}

	/**
	 * 返回{@link Iterator}中第一个匹配规则的值
	 *
	 * @param <T>      数组元素类型
	 * @param iterator {@link Iterator}
	 * @param matcher  匹配接口，实现此接口自定义匹配规则
	 * @return 匹配元素，如果不存在匹配元素或{@link Iterator}为空，返回 {@code null}
	 */
	public static <T> T firstMatch(Iterator<T> iterator, Matcher<T> matcher) {
		Assert.notNull(matcher, "Matcher must be not null !");
		if (null != iterator) {
			while (iterator.hasNext()) {
				final T next = iterator.next();
				if (matcher.match(next)) {
					return next;
				}
			}
		}
		return null;
	}

	/**
	 * 获得{@link Iterable}对象的元素类型（通过第一个非空元素判断）<br>
	 * 注意，此方法至少会调用多次next方法
	 *
	 * @param iterable {@link Iterable}
	 * @return 元素类型，当列表为空或元素全部为null时，返回null
	 */
	public static Class<?> getElementType(Iterable<?> iterable) {
		return getElementType(getIter(iterable));
	}

	/**
	 * 获得{@link Iterator}对象的元素类型（通过第一个非空元素判断）<br>
	 * 注意，此方法至少会调用多次next方法
	 *
	 * @param iterator {@link Iterator}，为 {@code null}返回{@code null}
	 * @return 元素类型，当列表为空或元素全部为{@code null}时，返回{@code null}
	 */
	public static Class<?> getElementType(Iterator<?> iterator) {
		if (null == iterator) {
			return null;
		}
		final Object ele = getFirstNoneNull(iterator);
		return null == ele ? null : ele.getClass();
	}

	/**
	 * 从给定的对象中获取可能存在的{@link Iterator}，规则如下：
	 * <ul>
	 *   <li>null - null</li>
	 *   <li>Iterator - 直接返回</li>
	 *   <li>Enumeration - {@link EnumerationIter}</li>
	 *   <li>Collection - 调用{@link Collection#iterator()}</li>
	 *   <li>Map - Entry的{@link Iterator}</li>
	 *   <li>Dictionary - values (elements) enumeration returned as iterator</li>
	 *   <li>array - {@link ArrayIter}</li>
	 *   <li>NodeList - {@link NodeListIter}</li>
	 *   <li>Node - 子节点</li>
	 *   <li>object with iterator() public method，通过反射访问</li>
	 *   <li>object - 单对象的{@link ArrayIter}</li>
	 * </ul>
	 *
	 * @param obj 可以获取{@link Iterator}的对象
	 * @return {@link Iterator}，如果提供对象为{@code null}，返回{@code null}
	 */
	public static Iterator<?> getIter(final Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof Iterator) {
			return (Iterator<?>) obj;
		} else if (obj instanceof Iterable) {
			return ((Iterable<?>) obj).iterator();
		} else if (ArrayUtil.isArray(obj)) {
			return new ArrayIter<>(obj);
		} else if (obj instanceof Enumeration) {
			return new EnumerationIter<>((Enumeration<?>) obj);
		} else if (obj instanceof Map) {
			return ((Map<?, ?>) obj).entrySet().iterator();
		} else if (obj instanceof NodeList) {
			return new NodeListIter((NodeList) obj);
		} else if (obj instanceof Node) {
			// 遍历子节点
			return new NodeListIter(((Node) obj).getChildNodes());
		} else if (obj instanceof Dictionary) {
			return new EnumerationIter<>(((Dictionary<?, ?>) obj).elements());
		}

		// 反射获取
		try {
			final Object iterator = ReflectUtil.invoke(obj, "iterator");
			if (iterator instanceof Iterator) {
				return (Iterator<?>) iterator;
			}
		} catch (final RuntimeException ignore) {
			// ignore
		}
		return new ArrayIter<>(new Object[]{obj});
	}

	// ----------------------------------------------------------------------- filter

	/**
	 * 过滤集合，此方法在原集合上直接修改<br>
	 * 通过实现Filter接口，完成元素的过滤，这个Filter实现可以实现以下功能：
	 *
	 * <pre>
	 * 1、过滤出需要的对象，{@link Filter#accept(Object)}方法返回false的对象将被使用{@link Iterator#remove()}方法移除
	 * </pre>
	 *
	 * @param <T>    集合类型
	 * @param <E>    集合元素类型
	 * @param iter   集合
	 * @param filter 过滤器接口
	 * @return 编辑后的集合
	 */
	public static <T extends Iterable<E>, E> T filter(T iter, Filter<E> filter) {
		if (null == iter) {
			return null;
		}

		filter(iter.iterator(), filter);

		return iter;
	}

	/**
	 * 过滤集合，此方法在原集合上直接修改<br>
	 * 通过实现Filter接口，完成元素的过滤，这个Filter实现可以实现以下功能：
	 *
	 * <pre>
	 * 1、过滤出需要的对象，{@link Filter#accept(Object)}方法返回false的对象将被使用{@link Iterator#remove()}方法移除
	 * </pre>
	 *
	 * @param <E>    集合元素类型
	 * @param iter   集合
	 * @param filter 过滤器接口，删除{@link Filter#accept(Object)}为{@code false}的元素
	 * @return 编辑后的集合
	 */
	public static <E> Iterator<E> filter(Iterator<E> iter, Filter<E> filter) {
		if (null == iter || null == filter) {
			return iter;
		}

		while (iter.hasNext()) {
			if (!filter.accept(iter.next())) {
				iter.remove();
			}
		}
		return iter;
	}

	/**
	 * 过滤{@link Iterator}并将过滤后满足条件的元素添加到List中
	 *
	 * @param <E>    元素类型
	 * @param iter   {@link Iterator}
	 * @param filter 过滤器，保留{@link Filter#accept(Object)}为{@code true}的元素
	 * @return ArrayList
	 */
	public static <E> List<E> filterToList(Iterator<E> iter, Filter<E> filter) {
		return toList(filtered(iter, filter));
	}

	/**
	 * 获取一个新的 {@link FilterIter}，用于过滤指定元素
	 *
	 * @param iterator 被包装的 {@link Iterator}
	 * @param filter   过滤断言，当{@link Filter#accept(Object)}为{@code true}时保留元素，{@code false}抛弃元素
	 * @param <E>      元素类型
	 * @return {@link FilterIter}
	 */
	public static <E> FilterIter<E> filtered(final Iterator<? extends E> iterator, final Filter<? super E> filter) {
		return new FilterIter<>(iterator, filter);
	}

	// -----------------------------------------------------------------------

	/**
	 * 按照给定函数，转换{@link Iterator}为另一种类型的{@link Iterator}
	 *
	 * @param <F>      源元素类型
	 * @param <T>      目标元素类型
	 * @param iterator 源{@link Iterator}
	 * @param function 转换函数
	 * @return 转换后的{@link Iterator}
	 */
	public static <F, T> Iterator<T> trans(Iterator<F> iterator, Function<? super F, ? extends T> function) {
		return new TransIter<>(iterator, function);
	}

	/**
	 * 返回 Iterable 对象的元素数量
	 *
	 * @param iterable Iterable对象
	 * @return Iterable对象的元素数量
	 */
	public static int size(Iterable<?> iterable) {
		if (null == iterable) {
			return 0;
		}

		if (iterable instanceof Collection<?>) {
			return ((Collection<?>) iterable).size();
		} else {
			return size(iterable.iterator());
		}
	}

	/**
	 * 返回 Iterator 对象的元素数量
	 *
	 * @param iterator Iterator对象
	 * @return Iterator对象的元素数量
	 */
	public static int size(Iterator<?> iterator) {
		int size = 0;
		if (iterator != null) {
			while (iterator.hasNext()) {
				iterator.next();
				size++;
			}
		}
		return size;
	}

	/**
	 * 判断两个{@link Iterable} 是否元素和顺序相同，返回{@code true}的条件是：
	 * <ul>
	 *     <li>两个{@link Iterable}必须长度相同</li>
	 *     <li>两个{@link Iterable}元素相同index的对象必须equals，满足{@link Objects#equals(Object, Object)}</li>
	 * </ul>
	 * 此方法来自Apache-Commons-Collections4。
	 *
	 * @param list1 列表1
	 * @param list2 列表2
	 * @return 是否相同
	 */
	public static boolean isEqualList(Iterable<?> list1, Iterable<?> list2) {
		if (list1 == list2) {
			return true;
		}

		final Iterator<?> it1 = list1.iterator();
		final Iterator<?> it2 = list2.iterator();
		Object obj1;
		Object obj2;
		while (it1.hasNext() && it2.hasNext()) {
			obj1 = it1.next();
			obj2 = it2.next();

			if (!Objects.equals(obj1, obj2)) {
				return false;
			}
		}

		// 当两个Iterable长度不一致时返回false
		return !(it1.hasNext() || it2.hasNext());
	}

	/**
	 * 清空指定{@link Iterator}，此方法遍历后调用{@link Iterator#remove()}移除每个元素
	 *
	 * @param iterator {@link Iterator}
	 */
	public static void clear(Iterator<?> iterator) {
		if (null != iterator) {
			while (iterator.hasNext()) {
				iterator.next();
				iterator.remove();
			}
		}
	}
}
