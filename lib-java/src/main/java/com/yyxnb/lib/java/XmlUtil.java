package com.yyxnb.lib.java;

import com.yyxnb.lib.java.exceptions.UtilException;
import com.yyxnb.lib.java.io.IoUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * XML工具类
 *
 * <pre>
 * <pre>
 *
 * @author yyx
 * @date 2022/3/19
 */
public class XmlUtil {

	/**
	 * 字符串常量：XML 空格转义 {@code "&nbsp;" -> " "}
	 */
	public static final String NBSP = "&nbsp;";

	/**
	 * 字符串常量：XML And 符转义 {@code "&amp;" -> "&"}
	 */
	public static final String AMP = "&amp;";

	/**
	 * 字符串常量：XML 双引号转义 {@code "&quot;" -> "\""}
	 */
	public static final String QUOTE = "&quot;";

	/**
	 * 字符串常量：XML 单引号转义 {@code "&apos" -> "'"}
	 */
	public static final String APOS = "&apos;";

	/**
	 * 字符串常量：XML 小于号转义 {@code "&lt;" -> "<"}
	 */
	public static final String LT = "&lt;";

	/**
	 * 字符串常量：XML 大于号转义 {@code "&gt;" -> ">"}
	 */
	public static final String GT = "&gt;";

	/**
	 * 在XML中无效的字符 正则
	 */
	public static final String INVALID_REGEX = "[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]";
	/**
	 * 在XML中注释的内容 正则
	 */
	public static final String COMMENT_REGEX = "(?s)<!--.+?-->";
	/**
	 * XML格式化输出默认缩进量
	 */
	public static final int INDENT_DEFAULT = 2;

	/**
	 * 默认的DocumentBuilderFactory实现
	 */
	private static String defaultDocumentBuilderFactory = "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl";

	/**
	 * 是否打开命名空间支持
	 */
	private static boolean namespaceAware = true;
	/**
	 * Sax读取器工厂缓存
	 */
	private static SAXParserFactory factory;

	/**
	 * 禁用默认的DocumentBuilderFactory，禁用后如果有第三方的实现（如oracle的xdb包中的xmlparse），将会自动加载实现。
	 */
	synchronized public static void disableDefaultDocumentBuilderFactory() {
		defaultDocumentBuilderFactory = null;
	}

	/**
	 * 设置是否打开命名空间支持，默认打开
	 *
	 * @param isNamespaceAware 是否命名空间支持
	 */
	synchronized public static void setNamespaceAware(boolean isNamespaceAware) {
		namespaceAware = isNamespaceAware;
	}

	// ----------------------------------------------------------------------- read

	/**
	 * 读取解析XML文件
	 *
	 * @param file XML文件
	 * @return XML文档对象
	 */
	public static Document readXML(File file) {
		Assert.notNull(file, "Xml file is null !");
		if (!file.exists()) {
			throw new UtilException("File [%s] not a exist!", file.getAbsolutePath());
		}
		if (!file.isFile()) {
			throw new UtilException("[%s] not a file!", file.getAbsolutePath());
		}

		try {
			file = file.getCanonicalFile();
		} catch (IOException e) {
			// ignore
		}

		BufferedInputStream in = null;
		try {
			in = IoUtil.toBuffered(IoUtil.toStream(file));
			return readXML(in);
		} finally {
			IoUtil.close(in);
		}
	}

	/**
	 * 读取解析XML文件<br>
	 * 编码在XML中定义
	 *
	 * @param inputStream XML流
	 * @return XML文档对象
	 * @throws UtilException IO异常或转换异常
	 */
	public static Document readXML(InputStream inputStream) throws UtilException {
		return readXML(new InputSource(inputStream));
	}

	/**
	 * 读取解析XML文件<br>
	 * 编码在XML中定义
	 *
	 * @param source {@link InputSource}
	 * @return XML文档对象
	 */
	public static Document readXML(InputSource source) {
		final DocumentBuilder builder = createDocumentBuilder();
		try {
			return builder.parse(source);
		} catch (Exception e) {
			throw new UtilException(e, "Parse XML from stream error!");
		}
	}

	// ----------------------------------------------------------------------- Create

	/**
	 * 创建XML文档<br>
	 * 创建的XML默认是utf8编码，修改编码的过程是在toStr和toFile方法里，即XML在转为文本的时候才定义编码
	 *
	 * @return XML文档
	 */
	public static Document createXml() {
		return createDocumentBuilder().newDocument();
	}

	/**
	 * 创建 DocumentBuilder
	 *
	 * @return DocumentBuilder
	 */
	public static DocumentBuilder createDocumentBuilder() {
		DocumentBuilder builder;
		try {
			builder = createDocumentBuilderFactory().newDocumentBuilder();
		} catch (Exception e) {
			throw new UtilException(e, "Create xml document error!");
		}
		return builder;
	}

	/**
	 * 创建{@link DocumentBuilderFactory}
	 * <p>
	 * 默认使用"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl"<br>
	 * 如果使用第三方实现，请调用{@link #disableDefaultDocumentBuilderFactory()}
	 * </p>
	 *
	 * @return {@link DocumentBuilderFactory}
	 */
	public static DocumentBuilderFactory createDocumentBuilderFactory() {
		final DocumentBuilderFactory factory;
		if (StrUtil.isNotEmpty(defaultDocumentBuilderFactory)) {
			factory = DocumentBuilderFactory.newInstance(defaultDocumentBuilderFactory, null);
		} else {
			factory = DocumentBuilderFactory.newInstance();
		}
		// 默认打开NamespaceAware，getElementsByTagNameNS可以使用命名空间
		factory.setNamespaceAware(namespaceAware);
		return disableXXE(factory);
	}


	// -----------------------------------------------------------------------

	/**
	 * 将XML文档转换为String<br>
	 * 字符编码使用XML文档中的编码，获取不到则使用UTF-8<br>
	 * 默认非格式化输出，若想格式化请使用{@link #format(Document)}
	 *
	 * @param doc XML文档
	 * @return XML字符串
	 */
	public static String toStr(Node doc) {
		return toStr(doc, false);
	}

	/**
	 * 将XML文档转换为String<br>
	 * 字符编码使用XML文档中的编码，获取不到则使用UTF-8
	 *
	 * @param doc      XML文档
	 * @param isPretty 是否格式化输出
	 * @return XML字符串
	 */
	public static String toStr(Node doc, boolean isPretty) {
		return toStr(doc, CharsetUtil.UTF_8, isPretty);
	}

	/**
	 * 将XML文档转换为String<br>
	 * 字符编码使用XML文档中的编码，获取不到则使用UTF-8
	 *
	 * @param doc      XML文档
	 * @param charset  编码
	 * @param isPretty 是否格式化输出
	 * @return XML字符串
	 */
	public static String toStr(Node doc, String charset, boolean isPretty) {
		return toStr(doc, charset, isPretty, false);
	}

	/**
	 * 将XML文档转换为String<br>
	 * 字符编码使用XML文档中的编码，获取不到则使用UTF-8
	 *
	 * @param doc                XML文档
	 * @param charset            编码
	 * @param isPretty           是否格式化输出
	 * @param omitXmlDeclaration 是否输出 xml Declaration
	 * @return XML字符串
	 */
	public static String toStr(Node doc, String charset, boolean isPretty, boolean omitXmlDeclaration) {
		final StringWriter writer = StrUtil.getWriter();
		try {
			write(doc, writer, charset, isPretty ? INDENT_DEFAULT : 0, omitXmlDeclaration);
		} catch (Exception e) {
			throw new UtilException(e, "Trans xml document to string error!");
		}
		return writer.toString();
	}

	/**
	 * 格式化XML输出
	 *
	 * @param doc {@link Document} XML文档
	 * @return 格式化后的XML字符串
	 */
	public static String format(Document doc) {
		return toStr(doc, true);
	}

	/**
	 * 将XML文档写出
	 *
	 * @param node               {@link Node} XML文档节点或文档本身
	 * @param writer             写出的Writer，Writer决定了输出XML的编码
	 * @param charset            编码
	 * @param indent             格式化输出中缩进量，小于1表示不格式化输出
	 * @param omitXmlDeclaration 是否输出 xml Declaration
	 */
	public static void write(Node node, Writer writer, String charset, int indent, boolean omitXmlDeclaration) {
		transform(new DOMSource(node), new StreamResult(writer), charset, indent, omitXmlDeclaration);
	}

	/**
	 * 将XML文档写出<br>
	 * 格式化输出逻辑参考：https://stackoverflow.com/questions/139076/how-to-pretty-print-xml-from-java
	 *
	 * @param source             源
	 * @param result             目标
	 * @param charset            编码
	 * @param indent             格式化输出中缩进量，小于1表示不格式化输出
	 * @param omitXmlDeclaration 是否输出 xml Declaration
	 */
	public static void transform(Source source, Result result, String charset, int indent, boolean omitXmlDeclaration) {
		final TransformerFactory factory = TransformerFactory.newInstance();
		try {
			final Transformer xformer = factory.newTransformer();
			if (indent > 0) {
				xformer.setOutputProperty(OutputKeys.INDENT, "yes");
				//fix issue#1232@Github
				xformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
				xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indent));
			}
			if (StrUtil.isNotBlank(charset)) {
				xformer.setOutputProperty(OutputKeys.ENCODING, charset);
			}
			if (omitXmlDeclaration) {
				xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			}
			xformer.transform(source, result);
		} catch (Exception e) {
			throw new UtilException(e, "Trans xml document to string error!");
		}
	}

	// -----------------------------------------------------------------------

	/**
	 * 关闭XXE，避免漏洞攻击<br>
	 * see: https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Prevention_Cheat_Sheet#JAXP_DocumentBuilderFactory.2C_SAXParserFactory_and_DOM4J
	 *
	 * @param dbf DocumentBuilderFactory
	 * @return DocumentBuilderFactory
	 */
	private static DocumentBuilderFactory disableXXE(DocumentBuilderFactory dbf) {
		String feature;
		try {
			// This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
			// Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
			feature = "http://apache.org/xml/features/disallow-doctype-decl";
			dbf.setFeature(feature, true);
			// If you can't completely disable DTDs, then at least do the following:
			// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
			// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
			// JDK7+ - http://xml.org/sax/features/external-general-entities
			feature = "http://xml.org/sax/features/external-general-entities";
			dbf.setFeature(feature, false);
			// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
			// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
			// JDK7+ - http://xml.org/sax/features/external-parameter-entities
			feature = "http://xml.org/sax/features/external-parameter-entities";
			dbf.setFeature(feature, false);
			// Disable external DTDs as well
			feature = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
			dbf.setFeature(feature, false);
			// and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
			dbf.setXIncludeAware(false);
			dbf.setExpandEntityReferences(false);
		} catch (ParserConfigurationException e) {
			// ignore
		}
		return dbf;
	}
}
