package com.yyxnb.android.core.xml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * 针对基于DOM的XML分析器可能存在的XML注入，对解析器配置了各种参数阻止XML注入的目的。
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/4/6
 */
public class DocumentBuilderFactorySecurity {

	public static DocumentBuilderFactory getInstance() throws ParserConfigurationException, NullPointerException {
		DocumentBuilderFactory factoryImpl = DocumentBuilderFactory.newInstance();

		// 打开、关闭命名空间处理功能。当正在解析文档时为只读属性，未解析文档的状态下为读写。
		factoryImpl.setFeature("http://xml.org/sax/features/namespaces", true);

		// 是否打开校验。当关闭校验的时候可以大大节约内存空间并且大大提高解析速度。因此如果使用的XML文档是可靠的，
		// 例如程序生成的，最好关闭校验。当正在解析文档时为只读属性，未解析文档的状态下为读写。
		factoryImpl.setFeature("http://xml.org/sax/features/validation", false);

		// 设置为false,关闭扩展实体引用
		factoryImpl.setExpandEntityReferences(false);

		return factoryImpl;
	}

}
