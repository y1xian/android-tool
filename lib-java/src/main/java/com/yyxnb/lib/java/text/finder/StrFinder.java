package com.yyxnb.lib.java.text.finder;

import com.yyxnb.lib.java.Assert;
import com.yyxnb.lib.java.StrUtil;

/**
 * 字符串查找器
 *
 * @author yyx
 */
public class StrFinder extends TextFinder {
	private static final long serialVersionUID = 1L;

	private final CharSequence strToFind;
	private final boolean caseInsensitive;

	/**
	 * 构造
	 *
	 * @param strToFind       被查找的字符串
	 * @param caseInsensitive 是否忽略大小写
	 */
	public StrFinder(CharSequence strToFind, boolean caseInsensitive) {
		Assert.notEmpty(strToFind);
		this.strToFind = strToFind;
		this.caseInsensitive = caseInsensitive;
	}

	@Override
	public int start(int from) {
		Assert.notNull(this.text, "Text to find must be not null!");
		final int subLen = strToFind.length();

		if (from < 0) {
			from = 0;
		}
		int endLimit = getValidEndIndex();
		if (negative) {
			for (int i = from; i > endLimit; i--) {
				if (StrUtil.isSubEquals(text, i, strToFind, 0, subLen, caseInsensitive)) {
					return i;
				}
			}
		} else {
			endLimit = endLimit - subLen + 1;
			for (int i = from; i < endLimit; i++) {
				if (StrUtil.isSubEquals(text, i, strToFind, 0, subLen, caseInsensitive)) {
					return i;
				}
			}
		}

		return INDEX_NOT_FOUND;
	}

	@Override
	public int end(int start) {
		if (start < 0) {
			return -1;
		}
		return start + strToFind.length();
	}
}
