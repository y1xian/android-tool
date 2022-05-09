package com.yyxnb.lib.java.collection;

import com.yyxnb.lib.java.CharUtil;
import com.yyxnb.lib.java.ClassUtil;
import com.yyxnb.lib.java.CompareUtil;
import com.yyxnb.lib.java.Matcher;
import com.yyxnb.lib.java.ReflectUtil;
import com.yyxnb.lib.java.StrUtil;
import com.yyxnb.lib.java.TypeUtil;
import com.yyxnb.lib.java.convert.ConvertUtil;
import com.yyxnb.lib.java.convert.ConverterRegistry;
import com.yyxnb.lib.java.exceptions.UtilException;

import java.lang.reflect.Type;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Function;

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
	 * @see MapUtil#isEmpty(Map)
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return MapUtil.isEmpty(map);
	}

	/**
	 * Iterable是否为空
	 *
	 * @param iterable Iterable对象
	 * @return 是否为空
	 * @see IterUtil#isEmpty(Iterable)
	 */
	public static boolean isEmpty(Iterable<?> iterable) {
		return IterUtil.isEmpty(iterable);
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

	// ----------------------------------------------------------------------- isNotEmpty

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
	 * @see MapUtil#isNotEmpty(Map)
	 */
	public static boolean isNotEmpty(Map<?, ?> map) {
		return MapUtil.isNotEmpty(map);
	}

	/**
	 * Iterable是否为空
	 *
	 * @param iterable Iterable对象
	 * @return 是否为空
	 * @see IterUtil#isNotEmpty(Iterable)
	 */
	public static boolean isNotEmpty(Iterable<?> iterable) {
		return IterUtil.isNotEmpty(iterable);
	}

	// -----------------------------------------------------------------------  new HashSet

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

	// ----------------------------------------------------------------------- List

	/**
	 * 新建一个空List
	 *
	 * @param <T>      集合元素类型
	 * @param isLinked 是否新建LinkedList
	 * @return List对象
	 */
	public static <T> List<T> list(boolean isLinked) {
		return ListUtil.list(isLinked);
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
		return ListUtil.list(isLinked, values);
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
		return ListUtil.list(isLinked, collection);
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
		return ListUtil.toList(values);
	}

	/**
	 * 新建一个ArrayList
	 *
	 * @param <T>        集合元素类型
	 * @param collection 集合
	 * @return ArrayList对象
	 */
	public static <T> ArrayList<T> newArrayList(Collection<T> collection) {
		return ListUtil.toList(collection);
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
		return ListUtil.toList(values);
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
		return ListUtil.toList(iterable);
	}

	// ----------------------------------------------------------------------- get


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

	// ----------------------------------------------------------------------- add


	/**
	 * 将指定对象全部加入到集合中<br>
	 * 提供的对象如果为集合类型，会自动转换为目标元素类型<br>
	 * 如果为String，支持类似于[1,2,3,4] 或者 1,2,3,4 这种格式
	 *
	 * @param <T>         元素类型
	 * @param collection  被加入的集合
	 * @param value       对象，可能为Iterator、Iterable、Enumeration、Array，或者与集合元素类型一致
	 * @param elementType 元素类型，为空时，使用Object类型来接纳所有类型
	 * @return 被加入集合
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T> Collection<T> addAll(Collection<T> collection, Object value, Type elementType) {
		if (null == collection || null == value) {
			return collection;
		}
		if (TypeUtil.isUnknown(elementType)) {
			// 元素类型为空时，使用Object类型来接纳所有类型
			elementType = Object.class;
		}

		Iterator iter;
		if (value instanceof Iterator) {
			iter = (Iterator) value;
		} else if (value instanceof Iterable) {
			iter = ((Iterable) value).iterator();
		} else if (value instanceof Enumeration) {
			iter = new EnumerationIter<>((Enumeration) value);
		} else if (ArrayUtil.isArray(value)) {
			iter = new ArrayIter<>(value);
		} else if (value instanceof CharSequence) {
			// String按照逗号分隔的列表对待
			final String ArrayStr = StrUtil.unWrap((CharSequence) value, '[', ']');
			iter = StrUtil.splitTrim(ArrayStr, CharUtil.COMMA).iterator();
		} else {
			// 其它类型按照单一元素处理
			iter = CollUtil.newArrayList(value).iterator();
		}

		final ConverterRegistry convert = ConverterRegistry.getInstance();
		while (iter.hasNext()) {
			collection.add(convert.convert(elementType, iter.next()));
		}

		return collection;
	}

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

	// ----------------------------------------------------------------------- join


	/**
	 * 以 conjunction 为分隔符将集合转换为字符串<br>
	 * 如果集合元素为数组、{@link Iterable}或{@link Iterator}，则递归组合其为字符串
	 *
	 * @param <T>         集合元素类型
	 * @param iterable    {@link Iterable}
	 * @param conjunction 分隔符
	 * @return 连接后的字符串
	 * @see IterUtil#join(Iterator, CharSequence)
	 */
	public static <T> String join(Iterable<T> iterable, CharSequence conjunction) {
		if (null == iterable) {
			return null;
		}
		return IterUtil.join(iterable.iterator(), conjunction);
	}

	// ----------------------------------------------------------------------- sub

	/**
	 * 截取列表的部分
	 *
	 * @param <T>   集合元素类型
	 * @param list  被截取的数组
	 * @param start 开始位置（包含）
	 * @param end   结束位置（不包含）
	 * @return 截取后的数组，当开始位置超过最大时，返回空的List
	 * @see ListUtil#sub(List, int, int)
	 */
	public static <T> List<T> sub(List<T> list, int start, int end) {
		return ListUtil.sub(list, start, end);
	}

	/**
	 * 截取列表的部分
	 *
	 * @param <T>   集合元素类型
	 * @param list  被截取的数组
	 * @param start 开始位置（包含）
	 * @param end   结束位置（不包含）
	 * @param step  步进
	 * @return 截取后的数组，当开始位置超过最大时，返回空的List
	 * @see ListUtil#sub(List, int, int, int)
	 */
	public static <T> List<T> sub(List<T> list, int start, int end, int step) {
		return ListUtil.sub(list, start, end, step);
	}

	/**
	 * 对集合按照指定长度分段，每一个段为单独的集合，返回这个集合的列表
	 * <p>
	 * 需要特别注意的是，此方法调用{@link List#subList(int, int)}切分List，
	 * 此方法返回的是原List的视图，也就是说原List有变更，切分后的结果也会变更。
	 * </p>
	 *
	 * @param <T>  集合元素类型
	 * @param list 列表
	 * @param size 每个段的长度
	 * @return 分段列表
	 * @deprecated 请使用 {@link ListUtil#partition(List, int)}
	 */
	@Deprecated
	public static <T> List<List<T>> splitList(List<T> list, int size) {
		return ListUtil.partition(list, size);
	}

	// ----------------------------------------------------------------------- index

	/**
	 * 获取匹配规则定义中匹配到元素的所有位置<br>
	 * 此方法对于某些无序集合的位置信息，以转换为数组后的位置为准。
	 *
	 * @param <T>        元素类型
	 * @param collection 集合
	 * @param matcher    匹配器，为空则全部匹配
	 * @return 位置数组
	 */
	public static <T> int[] indexOfAll(Collection<T> collection, Matcher<T> matcher) {
		final List<Integer> indexList = new ArrayList<>();
		if (null != collection) {
			int index = 0;
			for (T t : collection) {
				if (null == matcher || matcher.match(t)) {
					indexList.add(index);
				}
				index++;
			}
		}
		return ConvertUtil.convert(int[].class, indexList);
	}

	// -----------------------------------------------------------------------


	/**
	 * 创建新的集合对象
	 *
	 * @param <T>            集合类型
	 * @param collectionType 集合类型
	 * @return 集合类型对应的实例
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T> Collection<T> create(Class<?> collectionType) {
		Collection<T> list;
		if (collectionType.isAssignableFrom(AbstractCollection.class)) {
			// 抽象集合默认使用ArrayList
			list = new ArrayList<>();
		}

		// Set
		else if (collectionType.isAssignableFrom(HashSet.class)) {
			list = new HashSet<>();
		} else if (collectionType.isAssignableFrom(LinkedHashSet.class)) {
			list = new LinkedHashSet<>();
		} else if (collectionType.isAssignableFrom(TreeSet.class)) {
			list = new TreeSet<>((o1, o2) -> {
				// 优先按照对象本身比较，如果没有实现比较接口，默认按照toString内容比较
				if (o1 instanceof Comparable) {
					return ((Comparable<T>) o1).compareTo(o2);
				}
				return CompareUtil.compare(o1.toString(), o2.toString());
			});
		} else if (collectionType.isAssignableFrom(EnumSet.class)) {
			list = (Collection<T>) EnumSet.noneOf((Class<Enum>) ClassUtil.getTypeArgument(collectionType));
		}

		// List
		else if (collectionType.isAssignableFrom(ArrayList.class)) {
			list = new ArrayList<>();
		} else if (collectionType.isAssignableFrom(LinkedList.class)) {
			list = new LinkedList<>();
		}

		// Others，直接实例化
		else {
			try {
				list = (Collection<T>) ReflectUtil.newInstance(collectionType);
			} catch (Exception e) {
				// 无法创建当前类型的对象，尝试创建父类型对象
				final Class<?> superclass = collectionType.getSuperclass();
				if (null != superclass && collectionType != superclass) {
					return create(superclass);
				}
				throw new UtilException(e);
			}
		}
		return list;
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

	/**
	 * 使用给定的转换函数，转换源集合为新类型的集合
	 *
	 * @param <F>        源元素类型
	 * @param <T>        目标元素类型
	 * @param collection 集合
	 * @param function   转换函数
	 * @return 新类型的集合
	 */
	public static <F, T> Collection<T> trans(Collection<F> collection, Function<? super F, ? extends T> function) {
		return new TransCollection<>(collection, function);
	}


}
