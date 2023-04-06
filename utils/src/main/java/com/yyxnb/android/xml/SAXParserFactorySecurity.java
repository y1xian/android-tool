package com.yyxnb.android.xml;

import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

/**
 * 针对基于SAX的XML分析器可能存在的XML注入，对解析器配置了各种参数阻止XML注入的目的。
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/4/6
 */
public class SAXParserFactorySecurity {

	public static SAXParserFactory getInstance()
			throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException, NullPointerException {
		SAXParserFactory factoryImpl = SAXParserFactory.newInstance();

		// 打开、关闭命名空间处理功能。当正在解析文档时为只读属性，未解析文档的状态下为读写。
		factoryImpl.setFeature("http://xml.org/sax/features/namespaces", true);

		// 报告、不报告命名空间前缀。当正在解析文档时为只读属性，未解析文档的状态下为读写。
		// 目前namespace-prefixes 和 namespaces 不能同时为true
		factoryImpl.setFeature("http://xml.org/sax/features/namespace-prefixes", false);

		// 是否打开校验。当关闭校验的时候可以大大节约内存空间并且大大提高解析速度。因此如果使用的XML文档是可靠的，
		// 例如程序生成的，最好关闭校验。当正在解析文档时为只读属性，未解析文档的状态下为读写。
		factoryImpl.setFeature("http://xml.org/sax/features/validation", false);

		// 是否包含外部生成的实体。当正在解析文档时为只读属性，未解析文档的状态下为读写。
		factoryImpl.setFeature("http://xml.org/sax/features/external-general-entities", false);

		// 是否包含外部的参数，包括外部DTD子集。当正在解析文档时为只读属性，未解析文档的状态下为读写。
		factoryImpl.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

		// 是否将所有的名字等字符串内部化，即使用String.intern()方法处理所有的名字字符串，可以节省内存空间。
		factoryImpl.setFeature("http://xml.org/sax/features/string-interning", true);

		return factoryImpl;
	}
}
