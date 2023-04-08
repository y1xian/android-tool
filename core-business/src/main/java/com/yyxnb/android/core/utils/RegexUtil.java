package com.yyxnb.android.core.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RegexUtil
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/13
 */
public class RegexUtil {

	private RegexUtil() {
	}

	public static boolean isMatch(final String regex, final CharSequence input) {
		return input != null && input.length() > 0 && Pattern.matches(regex, input);
	}

	public static List<String> getMatches(final String regex, final CharSequence input) {
		if (input == null) {
			return Collections.emptyList();
		}
		List<String> matches = new ArrayList<>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			matches.add(matcher.group());
		}
		return matches;
	}

	public static String[] getSplits(final String input, final String regex) {
		if (input == null) {
			return new String[0];
		}
		return input.split(regex);
	}

	public static String getReplaceFirst(final String input,
										 final String regex,
										 final String replacement) {
		if (input == null) {
			return "";
		}
		return Pattern.compile(regex).matcher(input).replaceFirst(replacement);
	}

	public static String getReplaceAll(final String input,
									   final String regex,
									   final String replacement) {
		if (input == null) {
			return "";
		}
		return Pattern.compile(regex).matcher(input).replaceAll(replacement);
	}

}
