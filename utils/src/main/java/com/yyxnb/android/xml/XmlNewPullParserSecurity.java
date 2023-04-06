package com.yyxnb.android.xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * XmlNewPullParserSecurity
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/4/6
 */
public class XmlNewPullParserSecurity {

	public static XmlPullParser getInstance() throws XmlPullParserException {

		XmlPullParser pullParser = Xml.newPullParser();
		pullParser.setFeature(XmlPullParser.FEATURE_PROCESS_DOCDECL, false);
		pullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

		return pullParser;
	}
}
