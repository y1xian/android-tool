package com.yyxnb.java.io;

/**
 * 数据单位封装
 *
 * <pre>
 * 		BYTES      1B      2^0     1
 * 		KILOBYTES  1KB     2^10    1,024
 * 		MEGABYTES  1MB     2^20    1,048,576
 * 		GIGABYTES  1GB     2^30    1,073,741,824
 * 		TERABYTES  1TB     2^40    1,099,511,627,776
 * </pre>
 *
 * @author yyx
 * @date 2022/3/25
 */
public enum DataUnit {

	/**
	 * Bytes, 后缀表示为： {@code B}.
	 */
	BYTES("B", 1),

	/**
	 * Kilobytes, 后缀表示为： {@code KB}.
	 */
	KILOBYTES("KB", BYTES.getSize() * 1024),

	/**
	 * Megabytes, 后缀表示为： {@code MB}.
	 */
	MEGABYTES("MB", KILOBYTES.getSize() * 1024),

	/**
	 * Gigabytes, 后缀表示为： {@code GB}.
	 */
	GIGABYTES("GB", MEGABYTES.getSize() * 1024),

	/**
	 * Terabytes, 后缀表示为： {@code TB}.
	 */
	TERABYTES("TB", GIGABYTES.getSize() * 1024);

	public static final String[] UNIT_NAMES = new String[]{"B", "KB", "MB", "GB", "TB"};

	private final String suffix;
	private final long size;

	DataUnit(String suffix, long size) {
		this.suffix = suffix;
		this.size = size;
	}

	public String getSuffix() {
		return suffix;
	}

	public long getSize() {
		return size;
	}
}
