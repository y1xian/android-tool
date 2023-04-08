package com.yyxnb.android.core.xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * 针对基于DOM的pull分析器可能存在的XML注入，对解析器配置了各种参数阻止XML注入的目的。
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/4/6
 */
public class XmlPullParserFactorySecurity {

	public static XmlPullParserFactory getInstance() throws XmlPullParserException {
		XmlPullParserFactory factoryImpl = XmlPullParserFactory.newInstance();

		// 解析器处理命名空间xlmns
		factoryImpl.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);

		// 命名空间的属性是否通过对属性的访问方式暴露出来
		factoryImpl.setFeature(XmlPullParser.FEATURE_REPORT_NAMESPACE_ATTRIBUTES, false);

		// 支持DTD, XML1.0规范中定义的解析
		factoryImpl.setFeature(XmlPullParser.FEATURE_PROCESS_DOCDECL, false);

		// 是否支持在XML 1.0规范中定义的所有验证错误都将上报
		factoryImpl.setFeature(XmlPullParser.FEATURE_VALIDATION, false);

		return factoryImpl;
	}
}
