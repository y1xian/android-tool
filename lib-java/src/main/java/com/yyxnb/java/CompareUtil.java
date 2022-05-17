package com.yyxnb.java;

import java.util.Comparator;

/**
 * 比较工具类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/3/19
 */
public class CompareUtil {

	// ----------------------------------------------------------------------- 版本号

	/**
	 * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
	 *
	 * @param version1 版本1
	 * @param version2 版本2
	 * @return 排序值。负数：version1 < version2，正数：version1 > version2, 0：version1 == version2
	 */
	public static int compareVersion(String version1, String version2) {
		version1 = StrUtil.str(version1);
		version2 = StrUtil.str(version2);

		if (ObjectUtil.equal(version1, version2)) {
			return 0;
		}
		// null视为最小版本，排在前
		if (version1 == null && version2 == null) {
			return 0;
		} else if (version1 == null) {
			return -1;
		} else if (version2 == null) {
			return 1;
		}
		String[] versionArray1 = version1.split("\\.");
		String[] versionArray2 = version2.split("\\.");
		int idx = 0;
		// 取最小长度值
		int minLength = Math.min(versionArray1.length, versionArray2.length);
		int diff = 0;
		while (idx < minLength
				// 先比较长度
				&& (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0
				// 再比较字符
				&& (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {
			++idx;
		}
		// 如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
		diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
		return diff;
	}

	// ----------------------------------------------------------------------- 对比

	/**
	 * 对象比较，比较结果取决于comparator，如果被比较对象为null，传入的comparator对象应处理此情况<br>
	 * 如果传入comparator为null，则使用默认规则比较（此时被比较对象必须实现Comparable接口）
	 *
	 * <p>
	 * 一般而言，如果c1 &lt; c2，返回数小于0，c1==c2返回0，c1 &gt; c2 大于0
	 *
	 * @param <T>        被比较对象类型
	 * @param c1         对象1
	 * @param c2         对象2
	 * @param comparator 比较器
	 * @return 比较结果
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static <T> int compare(T c1, T c2, Comparator<T> comparator) {
		if (null == comparator) {
			return compare((Comparable) c1, (Comparable) c2);
		}
		return comparator.compare(c1, c2);
	}

	/**
	 * {@code null}安全的对象比较，{@code null}对象小于任何对象
	 *
	 * @param <T> 被比较对象类型
	 * @param c1  对象1，可以为{@code null}
	 * @param c2  对象2，可以为{@code null}
	 * @return 比较结果，如果c1 &lt; c2，返回数小于0，c1==c2返回0，c1 &gt; c2 大于0
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public static <T extends Comparable<? super T>> int compare(T c1, T c2) {
		return compare(c1, c2, false);
	}

	/**
	 * {@code null}安全的对象比较
	 *
	 * @param <T>           被比较对象类型（必须实现Comparable接口）
	 * @param c1            对象1，可以为{@code null}
	 * @param c2            对象2，可以为{@code null}
	 * @param isNullGreater 当被比较对象为null时是否排在前面，true表示null大于任何对象，false反之
	 * @return 比较结果，如果c1 &lt; c2，返回数小于0，c1==c2返回0，c1 &gt; c2 大于0
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public static <T extends Comparable<? super T>> int compare(T c1, T c2, boolean isNullGreater) {
		if (c1 == c2) {
			return 0;
		} else if (c1 == null) {
			return isNullGreater ? 1 : -1;
		} else if (c2 == null) {
			return isNullGreater ? -1 : 1;
		}
		return c1.compareTo(c2);
	}

	public static void main(String[] args) {
		System.out.println(compareVersion("V0.0.20170102", "V0.0.20170101"));
		System.out.println(compareVersion("V0.0.20170102", "0.0.20170103"));
		System.out.println(compareVersion("1.0.2", "v1.0.2"));
		System.out.println();
		System.out.println(compareVersion("V0.0.20170102", "V0.0.20170101"));
		System.out.println(compareVersion("V0.0.20170102", "0.0.20170103"));
		System.out.println(compareVersion("V1.0.2", "v1.0.2"));
		System.out.println();
		System.out.println(compareVersion(null, "v1"));
		System.out.println(compareVersion("1", null));
		System.out.println(compareVersion("1.13.0", "1.12.1c"));
	}

}
