package com.yyxnb.java.io;

import com.yyxnb.java.Assert;
import com.yyxnb.java.CharsetUtil;
import com.yyxnb.java.StrUtil;
import com.yyxnb.java.exceptions.UtilException;
import com.yyxnb.java.io.copy.ReaderWriterCopier;
import com.yyxnb.java.io.copy.StreamCopier;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * IO工具类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/3/25
 */
public class IoUtil {

	/**
	 * 默认缓存大小 8192
	 */
	public static final int DEFAULT_BUFFER_SIZE = 2 << 12;
	/**
	 * 默认中等缓存大小 16384
	 */
	public static final int DEFAULT_MIDDLE_BUFFER_SIZE = 2 << 13;
	/**
	 * 默认大缓存大小 32768
	 */
	public static final int DEFAULT_LARGE_BUFFER_SIZE = 2 << 14;

	/**
	 * 数据流末尾
	 */
	public static final int EOF = -1;

	// ----------------------------------------------------------------------- 关闭流

	/**
	 * 从缓存中刷出数据
	 *
	 * @param flushable {@link Flushable}
	 */
	public static void flush(Flushable flushable) {
		if (null != flushable) {
			try {
				flushable.flush();
			} catch (Exception e) {
				// 静默刷出
			}
		}
	}

	/**
	 * 关闭<br>
	 * 关闭失败不会抛出异常
	 *
	 * @param closeable 被关闭的对象
	 */
	public static void close(Closeable closeable) {
		if (null != closeable) {
			try {
				closeable.close();
			} catch (Exception e) {
				// 静默关闭
			}
		}
	}

	/**
	 * 关闭<br>
	 * 关闭失败不会抛出异常
	 *
	 * @param closeable 被关闭的对象
	 */
	public static void close(Closeable... closeable) {
		for (Closeable c : closeable) {
			close(c);
		}
	}

	/**
	 * 尝试关闭指定对象<br>
	 * 判断对象如果实现了{@link AutoCloseable}，则调用之
	 *
	 * @param obj 可关闭对象
	 */
	public static void closeIfPosible(Object obj) {
		if (obj instanceof AutoCloseable) {
			close((AutoCloseable) obj);
		}
	}

	/**
	 * 关闭<br>
	 * 关闭失败不会抛出异常
	 *
	 * @param closeable 被关闭的对象
	 */
	public static void close(AutoCloseable closeable) {
		if (null != closeable) {
			try {
				closeable.close();
			} catch (Exception e) {
				// 静默关闭
			}
		}
	}

	// ----------------------------------------------------------------------- 转换

	/**
	 * 将输入流转为字节数组
	 *
	 * @param inputStream 输入流
	 * @return 字节数组
	 * @throws IOException IO异常
	 */
	public static byte[] read2Byte(InputStream inputStream) throws IOException {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		close(outSteam, inputStream);
		return outSteam.toByteArray();
	}

	/**
	 * 将输入流转为字符串
	 *
	 * @param inputStream 输入流
	 * @return 字符串
	 * @throws IOException IO异常
	 */
	public static String read2String(InputStream inputStream) throws IOException {
		return new String(read2Byte(inputStream));
	}

	// ----------------------------------------------------------------------- to


	/**
	 * String 转为流
	 *
	 * @param content     内容
	 * @param charsetName 编码
	 * @return 字节流
	 * @deprecated 请使用 {@link #toStream(String, Charset)}
	 */
	@Deprecated
	public static ByteArrayInputStream toStream(String content, String charsetName) {
		return toStream(content, CharsetUtil.charset(charsetName));
	}

	/**
	 * String 转为流
	 *
	 * @param content 内容
	 * @param charset 编码
	 * @return 字节流
	 */
	public static ByteArrayInputStream toStream(String content, Charset charset) {
		if (content == null) {
			return null;
		}
		return toStream(StrUtil.bytes(content, charset));
	}

	/**
	 * String 转为UTF-8编码的字节流流
	 *
	 * @param content 内容
	 * @return 字节流
	 */
	public static ByteArrayInputStream toUtf8Stream(String content) {
		return toStream(content, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 文件转为{@link FileInputStream}
	 *
	 * @param file 文件
	 * @return {@link FileInputStream}
	 */
	public static FileInputStream toStream(File file) {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * byte[] 转为{@link ByteArrayInputStream}
	 *
	 * @param content 内容bytes
	 * @return 字节流
	 */
	public static ByteArrayInputStream toStream(byte[] content) {
		if (content == null) {
			return null;
		}
		return new ByteArrayInputStream(content);
	}

	/**
	 * {@link ByteArrayOutputStream}转为{@link ByteArrayInputStream}
	 *
	 * @param out {@link ByteArrayOutputStream}
	 * @return 字节流
	 */
	public static ByteArrayInputStream toStream(ByteArrayOutputStream out) {
		if (out == null) {
			return null;
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

	/**
	 * {@link ByteArrayOutputStream} 转换为String
	 *
	 * @param out     {@link ByteArrayOutputStream}
	 * @param charset 编码
	 * @return 字符串
	 */
	public static String toStr(ByteArrayOutputStream out, Charset charset) {
		try {
			return out.toString(charset.name());
		} catch (UnsupportedEncodingException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 转换为{@link BufferedInputStream}
	 *
	 * @param in {@link InputStream}
	 * @return {@link BufferedInputStream}
	 */
	public static BufferedInputStream toBuffered(InputStream in) {
		Assert.notNull(in, "InputStream must be not null!");
		return (in instanceof BufferedInputStream) ? (BufferedInputStream) in : new BufferedInputStream(in);
	}

	/**
	 * 转换为{@link BufferedInputStream}
	 *
	 * @param in         {@link InputStream}
	 * @param bufferSize buffer size
	 * @return {@link BufferedInputStream}
	 */
	public static BufferedInputStream toBuffered(InputStream in, int bufferSize) {
		Assert.notNull(in, "InputStream must be not null!");
		return (in instanceof BufferedInputStream) ? (BufferedInputStream) in : new BufferedInputStream(in, bufferSize);
	}

	/**
	 * 转换为{@link BufferedOutputStream}
	 *
	 * @param out {@link OutputStream}
	 * @return {@link BufferedOutputStream}
	 */
	public static BufferedOutputStream toBuffered(OutputStream out) {
		Assert.notNull(out, "OutputStream must be not null!");
		return (out instanceof BufferedOutputStream) ? (BufferedOutputStream) out : new BufferedOutputStream(out);
	}

	/**
	 * 转换为{@link BufferedOutputStream}
	 *
	 * @param out        {@link OutputStream}
	 * @param bufferSize buffer size
	 * @return {@link BufferedOutputStream}
	 */
	public static BufferedOutputStream toBuffered(OutputStream out, int bufferSize) {
		Assert.notNull(out, "OutputStream must be not null!");
		return (out instanceof BufferedOutputStream) ? (BufferedOutputStream) out : new BufferedOutputStream(out, bufferSize);
	}

	/**
	 * 转换为{@link BufferedReader}
	 *
	 * @param reader {@link Reader}
	 * @return {@link BufferedReader}
	 */
	public static BufferedReader toBuffered(Reader reader) {
		Assert.notNull(reader, "Reader must be not null!");
		return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);
	}

	/**
	 * 转换为{@link BufferedReader}
	 *
	 * @param reader     {@link Reader}
	 * @param bufferSize buffer size
	 * @return {@link BufferedReader}
	 */
	public static BufferedReader toBuffered(Reader reader, int bufferSize) {
		Assert.notNull(reader, "Reader must be not null!");
		return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader, bufferSize);
	}

	/**
	 * 转换为{@link BufferedWriter}
	 *
	 * @param writer {@link Writer}
	 * @return {@link BufferedWriter}
	 */
	public static BufferedWriter toBuffered(Writer writer) {
		Assert.notNull(writer, "Writer must be not null!");
		return (writer instanceof BufferedWriter) ? (BufferedWriter) writer : new BufferedWriter(writer);
	}

	/**
	 * 转换为{@link BufferedWriter}
	 *
	 * @param writer     {@link Writer}
	 * @param bufferSize buffer size
	 * @return {@link BufferedWriter}
	 */
	public static BufferedWriter toBuffered(Writer writer, int bufferSize) {
		Assert.notNull(writer, "Writer must be not null!");
		return (writer instanceof BufferedWriter) ? (BufferedWriter) writer : new BufferedWriter(writer, bufferSize);
	}

	// ----------------------------------------------------------------------- read

	/**
	 * 从流中读取UTF8编码的内容
	 *
	 * @param in 输入流
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String readUtf8(InputStream in) throws IORuntimeException {
		return read(in, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 从流中读取内容，读取完成后关闭流
	 *
	 * @param in          输入流
	 * @param charsetName 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 * @deprecated 请使用 {@link #read(InputStream, Charset)}
	 */
	@Deprecated
	public static String read(InputStream in, String charsetName) throws IORuntimeException {
		final FastByteArrayOutputStream out = read(in);
		return StrUtil.isBlank(charsetName) ? out.toString() : out.toString(charsetName);
	}

	/**
	 * 从流中读取内容，读到输出流中，读取完毕后关闭流
	 *
	 * @param in 输入流
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 */
	public static FastByteArrayOutputStream read(InputStream in) throws IORuntimeException {
		return read(in, true);
	}

	/**
	 * 从Reader中读取String，读取完毕后关闭Reader
	 *
	 * @param reader Reader
	 * @return String
	 * @throws IORuntimeException IO异常
	 */
	public static String read(Reader reader) throws IORuntimeException {
		return read(reader, true);
	}

	/**
	 * 从{@link Reader}中读取String
	 *
	 * @param reader  {@link Reader}
	 * @param isClose 是否关闭{@link Reader}
	 * @return String
	 * @throws IORuntimeException IO异常
	 */
	public static String read(Reader reader, boolean isClose) throws IORuntimeException {
		final StringBuilder builder = StrUtil.builder();
		final CharBuffer buffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
		try {
			while (-1 != reader.read(buffer)) {
				builder.append(buffer.flip());
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (isClose) {
				IoUtil.close(reader);
			}
		}
		return builder.toString();
	}

	/**
	 * 从流中读取内容，读取完毕后关闭流
	 *
	 * @param in      输入流，读取完毕后并不关闭流
	 * @param charset 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String read(InputStream in, Charset charset) throws IORuntimeException {
		return StrUtil.str(readBytes(in), charset);
	}

	/**
	 * 从流中读取bytes，读取完毕后关闭流
	 *
	 * @param in {@link InputStream}
	 * @return bytes
	 * @throws IORuntimeException IO异常
	 */
	public static byte[] readBytes(InputStream in) throws IORuntimeException {
		return readBytes(in, true);
	}

	/**
	 * 从流中读取bytes
	 *
	 * @param in      {@link InputStream}
	 * @param isClose 是否关闭输入流
	 * @return bytes
	 * @throws IORuntimeException IO异常
	 */
	public static byte[] readBytes(InputStream in, boolean isClose) throws IORuntimeException {
		if (in instanceof FileInputStream) {
			// 文件流的长度是可预见的，此时直接读取效率更高
			final byte[] result;
			try {
				final int available = in.available();
				result = new byte[available];
				final int readLength = in.read(result);
				if (readLength != available) {
					throw new IOException(StrUtil.format("File length is [%s] but read [%s]!", available, readLength));
				}
			} catch (IOException e) {
				throw new IORuntimeException(e);
			} finally {
				if (isClose) {
					close(in);
				}
			}
			return result;
		}

		// 未知bytes总量的流
		return read(in, isClose).toByteArray();
	}

	/**
	 * 从流中读取内容，读到输出流中，读取完毕后可选是否关闭流
	 *
	 * @param in      输入流
	 * @param isClose 读取完毕后是否关闭流
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 */
	public static FastByteArrayOutputStream read(InputStream in, boolean isClose) throws IORuntimeException {
		final FastByteArrayOutputStream out;
		if (in instanceof FileInputStream) {
			// 文件流的长度是可预见的，此时直接读取效率更高
			try {
				out = new FastByteArrayOutputStream(in.available());
			} catch (IOException e) {
				throw new IORuntimeException(e);
			}
		} else {
			out = new FastByteArrayOutputStream();
		}
		try {
			copy(in, out);
		} finally {
			if (isClose) {
				close(in);
			}
		}
		return out;
	}

	/**
	 * 从流中读取对象，即对象的反序列化
	 *
	 * <p>
	 * 注意！！！ 此方法不会检查反序列化安全，可能存在反序列化漏洞风险！！！
	 * </p>
	 *
	 * @param <T> 读取对象的类型
	 * @param in  输入流
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 * @throws UtilException      ClassNotFoundException包装
	 */
	public static <T> T readObj(InputStream in) throws IORuntimeException, UtilException {
		return readObj(in, null);
	}

	/**
	 * 从流中读取对象，即对象的反序列化，读取后不关闭流
	 *
	 * <p>
	 * 注意！！！ 此方法不会检查反序列化安全，可能存在反序列化漏洞风险！！！
	 * </p>
	 *
	 * @param <T>   读取对象的类型
	 * @param in    输入流
	 * @param clazz 读取对象类型
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 * @throws UtilException      ClassNotFoundException包装
	 */
	public static <T> T readObj(InputStream in, Class<T> clazz) throws IORuntimeException, UtilException {
		try {
			return readObj((in instanceof ValidateObjectInputStream) ?
							(ValidateObjectInputStream) in : new ValidateObjectInputStream(in),
					clazz);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 从流中读取对象，即对象的反序列化，读取后不关闭流
	 *
	 * <p>
	 * 此方法使用了{@link ValidateObjectInputStream}中的黑白名单方式过滤类，用于避免反序列化漏洞<br>
	 * 通过构造{@link ValidateObjectInputStream}，调用{@link ValidateObjectInputStream#accept(Class[])}
	 * 或者{@link ValidateObjectInputStream#refuse(Class[])}方法添加可以被序列化的类或者禁止序列化的类。
	 * </p>
	 *
	 * @param <T>   读取对象的类型
	 * @param in    输入流，使用{@link ValidateObjectInputStream}中的黑白名单方式过滤类，用于避免反序列化漏洞
	 * @param clazz 读取对象类型
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 * @throws UtilException      ClassNotFoundException包装
	 */
	public static <T> T readObj(ValidateObjectInputStream in, Class<T> clazz) throws IORuntimeException, UtilException {
		if (in == null) {
			throw new IllegalArgumentException("The InputStream must not be null");
		}
		if (null != clazz) {
			in.accept(clazz);
		}
		try {
			//noinspection unchecked
			return (T) in.readObject();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new UtilException(e);
		}
	}

	// ----------------------------------------------------------------------- write

	/**
	 * 将byte[]写到流中
	 *
	 * @param out        输出流
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param content    写入的内容
	 * @throws IORuntimeException IO异常
	 */
	public static void write(OutputStream out, boolean isCloseOut, byte[] content) throws IORuntimeException {
		try {
			out.write(content);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (isCloseOut) {
				close(out);
			}
		}
	}

	/**
	 * 将多部分内容写到流中
	 *
	 * @param out        输出流
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param obj        写入的对象内容
	 * @throws IORuntimeException IO异常
	 */
	public static void writeObj(OutputStream out, boolean isCloseOut, Serializable obj) throws IORuntimeException {
		writeObjects(out, isCloseOut, obj);
	}

	/**
	 * 将多部分内容写到流中
	 *
	 * @param out        输出流
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param contents   写入的内容
	 * @throws IORuntimeException IO异常
	 */
	public static void writeObjects(OutputStream out, boolean isCloseOut, Serializable... contents) throws IORuntimeException {
		ObjectOutputStream osw = null;
		try {
			osw = out instanceof ObjectOutputStream ? (ObjectOutputStream) out : new ObjectOutputStream(out);
			for (Object content : contents) {
				if (content != null) {
					osw.writeObject(content);
				}
			}
			osw.flush();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (isCloseOut) {
				close(osw);
			}
		}
	}

	// ----------------------------------------------------------------------- copy


	/**
	 * 将Reader中的内容复制到Writer中 使用默认缓存大小，拷贝后不关闭Reader
	 *
	 * @param reader Reader
	 * @param writer Writer
	 * @return 拷贝的字节数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(Reader reader, Writer writer) throws IORuntimeException {
		return copy(reader, writer, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 将Reader中的内容复制到Writer中，拷贝后不关闭Reader
	 *
	 * @param reader     Reader
	 * @param writer     Writer
	 * @param bufferSize 缓存大小
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(Reader reader, Writer writer, int bufferSize) throws IORuntimeException {
		return copy(reader, writer, bufferSize, null);
	}

	/**
	 * 将Reader中的内容复制到Writer中，拷贝后不关闭Reader
	 *
	 * @param reader         Reader
	 * @param writer         Writer
	 * @param bufferSize     缓存大小
	 * @param streamProgress 进度处理器
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(Reader reader, Writer writer, int bufferSize, StreamProgress streamProgress) throws IORuntimeException {
		return copy(reader, writer, bufferSize, -1, streamProgress);
	}

	/**
	 * 将Reader中的内容复制到Writer中，拷贝后不关闭Reader
	 *
	 * @param reader         Reader
	 * @param writer         Writer
	 * @param bufferSize     缓存大小
	 * @param count          最大长度
	 * @param streamProgress 进度处理器
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(Reader reader, Writer writer, int bufferSize, long count, StreamProgress streamProgress) throws IORuntimeException {
		return new ReaderWriterCopier(bufferSize, count, streamProgress).copy(reader, writer);
	}

	/**
	 * 拷贝流，使用默认Buffer大小，拷贝后不关闭流
	 *
	 * @param in  输入流
	 * @param out 输出流
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(InputStream in, OutputStream out) throws IORuntimeException {
		return copy(in, out, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 拷贝流，拷贝后不关闭流
	 *
	 * @param in         输入流
	 * @param out        输出流
	 * @param bufferSize 缓存大小
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(InputStream in, OutputStream out, int bufferSize) throws IORuntimeException {
		return copy(in, out, bufferSize, null);
	}

	/**
	 * 拷贝流，拷贝后不关闭流
	 *
	 * @param in             输入流
	 * @param out            输出流
	 * @param bufferSize     缓存大小
	 * @param streamProgress 进度条
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(InputStream in, OutputStream out, int bufferSize, StreamProgress streamProgress) throws IORuntimeException {
		return copy(in, out, bufferSize, -1, streamProgress);
	}

	/**
	 * 拷贝流，拷贝后不关闭流
	 *
	 * @param in             输入流
	 * @param out            输出流
	 * @param bufferSize     缓存大小
	 * @param count          总拷贝长度
	 * @param streamProgress 进度条
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(InputStream in, OutputStream out, int bufferSize, long count, StreamProgress streamProgress) throws IORuntimeException {
		return new StreamCopier(bufferSize, count, streamProgress).copy(in, out);
	}

	/**
	 * 拷贝文件流，使用NIO
	 *
	 * @param in  输入
	 * @param out 输出
	 * @return 拷贝的字节数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(FileInputStream in, FileOutputStream out) throws IORuntimeException {
		Assert.notNull(in, "FileInputStream is null!");
		Assert.notNull(out, "FileOutputStream is null!");

		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try {
			inChannel = in.getChannel();
			outChannel = out.getChannel();
			return copy(inChannel, outChannel);
		} finally {
			close(outChannel);
			close(inChannel);
		}
	}

	/**
	 * 拷贝文件Channel，使用NIO，拷贝后不会关闭channel
	 *
	 * @param inChannel  {@link FileChannel}
	 * @param outChannel {@link FileChannel}
	 * @return 拷贝的字节数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(FileChannel inChannel, FileChannel outChannel) throws IORuntimeException {
		Assert.notNull(inChannel, "In channel is null!");
		Assert.notNull(outChannel, "Out channel is null!");

		try {
			return copySafely(inChannel, outChannel);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 文件拷贝实现
	 *
	 * <pre>
	 * FileChannel#transferTo 或 FileChannel#transferFrom 的实现是平台相关的，需要确保低版本平台的兼容性
	 * 例如 android 7以下平台在使用 ZipInputStream 解压文件的过程中，
	 * 通过 FileChannel#transferFrom 传输到文件时，其返回值可能小于 totalBytes，不处理将导致文件内容缺失
	 *
	 * // 错误写法，dstChannel.transferFrom 返回值小于 zipEntry.getSize()，导致解压后文件内容缺失
	 * try (InputStream srcStream = zipFile.getInputStream(zipEntry);
	 * 		ReadableByteChannel srcChannel = Channels.newChannel(srcStream);
	 * 		FileOutputStream fos = new FileOutputStream(saveFile);
	 * 		FileChannel dstChannel = fos.getChannel()) {
	 * 		dstChannel.transferFrom(srcChannel, 0, zipEntry.getSize());
	 *  }
	 * </pre>
	 *
	 * @param inChannel  输入通道
	 * @param outChannel 输出通道
	 * @return 输入通道的字节数
	 * @throws IOException 发生IO错误
	 * @link http://androidxref.com/6.0.1_r10/xref/libcore/luni/src/main/java/java/nio/FileChannelImpl.java
	 * @link http://androidxref.com/7.0.0_r1/xref/libcore/ojluni/src/main/java/sun/nio/ch/FileChannelImpl.java
	 * @link http://androidxref.com/7.0.0_r1/xref/libcore/ojluni/src/main/native/FileChannelImpl.c
	 */
	private static long copySafely(FileChannel inChannel, FileChannel outChannel) throws IOException {
		final long totalBytes = inChannel.size();
		for (long pos = 0, remaining = totalBytes; remaining > 0; ) { // 确保文件内容不会缺失
			final long writeBytes = inChannel.transferTo(pos, remaining, outChannel); // 实际传输的字节数
			pos += writeBytes;
			remaining -= writeBytes;
		}
		return totalBytes;
	}

	// ----------------------------------------------------------------------- get

	/**
	 * 获得一个文件读取器，默认使用UTF-8编码
	 *
	 * @param in 输入流
	 * @return BufferedReader对象
	 */
	public static BufferedReader getUtf8Reader(InputStream in) {
		return getReader(in, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 获得一个Reader
	 *
	 * @param in      输入流
	 * @param charset 字符集
	 * @return BufferedReader对象
	 */
	public static BufferedReader getReader(InputStream in, Charset charset) {
		if (null == in) {
			return null;
		}

		InputStreamReader reader;
		if (null == charset) {
			reader = new InputStreamReader(in);
		} else {
			reader = new InputStreamReader(in, charset);
		}
		return new BufferedReader(reader);
	}

	/**
	 * 获得{@link BufferedReader}<br>
	 * 如果是{@link BufferedReader}强转返回，否则新建。如果提供的Reader为null返回null
	 *
	 * @param reader 普通Reader，如果为null返回null
	 * @return {@link BufferedReader} or null
	 */
	public static BufferedReader getReader(Reader reader) {
		if (null == reader) {
			return null;
		}
		return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);
	}

	/**
	 * 获得{@link PushbackReader}<br>
	 * 如果是{@link PushbackReader}强转返回，否则新建
	 *
	 * @param reader       普通Reader
	 * @param pushBackSize 推后的byte数
	 * @return {@link PushbackReader}
	 */
	public static PushbackReader getPushBackReader(Reader reader, int pushBackSize) {
		return (reader instanceof PushbackReader) ? (PushbackReader) reader : new PushbackReader(reader, pushBackSize);
	}

	/**
	 * 获得一个Writer，默认编码UTF-8
	 *
	 * @param out 输入流
	 * @return OutputStreamWriter对象
	 */
	public static OutputStreamWriter getUtf8Writer(OutputStream out) {
		return getWriter(out, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 获得一个Writer
	 *
	 * @param out         输入流
	 * @param charsetName 字符集
	 * @return OutputStreamWriter对象
	 * @deprecated 请使用 {@link #getWriter(OutputStream, Charset)}
	 */
	@Deprecated
	public static OutputStreamWriter getWriter(OutputStream out, String charsetName) {
		return getWriter(out, Charset.forName(charsetName));
	}

	/**
	 * 获得一个Writer
	 *
	 * @param out     输入流
	 * @param charset 字符集
	 * @return OutputStreamWriter对象
	 */
	public static OutputStreamWriter getWriter(OutputStream out, Charset charset) {
		if (null == out) {
			return null;
		}

		if (null == charset) {
			return new OutputStreamWriter(out);
		} else {
			return new OutputStreamWriter(out, charset);
		}
	}
}
