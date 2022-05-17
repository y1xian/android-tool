package com.yyxnb.java.regex;

import com.yyxnb.java.collection.CollUtil;

import java.util.Set;

/**
 * 正则相关工具类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/3/24
 */
public class ReUtil {

	/**
	 * 正则表达式匹配中文汉字
	 */
	public final static String RE_CHINESE = "[\u4E00-\u9FFF]";
	/**
	 * 正则表达式匹配中文字符串
	 */
	public final static String RE_CHINESES = RE_CHINESE + "+";

	/**
	 * 正则中需要被转义的关键字
	 */
	public final static Set<Character> RE_KEYS = CollUtil.newHashSet('$', '(', ')', '*', '+', '.', '[', ']', '?', '\\', '^', '{', '}', '|');


}
