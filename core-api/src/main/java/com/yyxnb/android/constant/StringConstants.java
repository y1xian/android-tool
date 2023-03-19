package com.yyxnb.android.constant;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 字符串常量
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/11/13
 */
public interface StringConstants {

	/**
	 * 字符串常量：{@code "null"} <br>
	 * 注意：{@code "null" != null}
	 */
	String NULL = "null";
	/**
	 * 字符串常量：{@code "undefined"}
	 */
	String UNDEFINED = "undefined";
	/**
	 * 字符串常量：空字符串 {@code ""}
	 */
	String EMPTY = "";
	/**
	 * 字符串常量：空格符 {@code " "}
	 */
	String SPACE = " ";
	/**
	 * 字符串常量：空 JSON {@code "{}"}
	 */
	String EMPTY_JSON = "{}";
	/**
	 * 字符串常量：斜杠 {@code "/"}
	 */
	String SLASH = "/";
	/**
	 * 字符串常量：反斜杠 {@code "\\"}
	 */
	String BACKSLASH = "\\";
	/**
	 * 字符串常量：回车符 {@code "\r"} <br>
	 * 解释：该字符常用于表示 Linux 系统和 MacOS 系统下的文本换行
	 */
	String CR = "\r";
	/**
	 * 字符串常量：换行符 {@code "\n"}
	 */
	String LF = "\n";
	/**
	 * 字符串常量：Windows 换行 {@code "\r\n"} <br>
	 * 解释：该字符串常用于表示 Windows 系统下的文本换行
	 */
	String CRLF = System.lineSeparator();
	/**
	 * 字符串常量：下划线 {@code "_"}
	 */
	String UNDERLINE = "_";
	/**
	 * 字符串常量：减号（连接符） {@code "-"}
	 */
	String DASHED = "-";
	/**
	 * 字符串常量：逗号 {@code ","}
	 */
	String COMMA = ",";
	/**
	 * 字符串常量：小括号（左） <code>"("</code>
	 */
	String PARENTHESES_START = "(";
	/**
	 * 字符串常量：小括号（右） <code>")"</code>
	 */
	String PARENTHESES_END = ")";
	/**
	 * 字符串常量：花括号（左） <code>"{"</code>
	 */
	String BRACE_START = "{";
	/**
	 * 字符串常量：花括号（右） <code>"}"</code>
	 */
	String BRACE_END = "}";
	/**
	 * 字符串常量：中括号（左） {@code "["}
	 */
	String BRACKET_START = "[";
	/**
	 * 字符串常量：中括号（右） {@code "]"}
	 */
	String BRACKET_END = "]";
	/**
	 * 字符串常量：冒号 {@code ":"}
	 */
	String COLON = ":";
	/**
	 * 字符串常量：艾特 {@code "@"}
	 */
	String AT = "@";
	/**
	 * ISO-8859-1
	 */
	String ISO_8859_1 = "ISO-8859-1";
	/**
	 * UTF-8
	 */
	String UTF_8 = "UTF-8";
	/**
	 * GBK
	 */
	String GBK = "GBK";
	/**
	 * ISO-8859-1
	 */
	Charset CHARSET_ISO_8859_1 = StandardCharsets.ISO_8859_1;
	/**
	 * UTF-8
	 */
	Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;
}