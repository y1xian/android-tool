package com.yyxnb.android.skin.config;

/**
 * SkinConfig
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/3/27
 */
public class SkinConfig {

	/**
	 * sp 文件名称
	 */
	public static final String SKIN_INFO_NAME = "skin_info";

	/**
	 * sp 缓存皮肤名称的 key
	 */
	public static final String SKIN_PATH_NAME = "path_name";

	/**
	 * 属性值对应的类型是color
	 */
	public static final String RES_TYPE_NAME_COLOR = "color";

	/**
	 * 属性值对应的类型是drawable
	 */
	public static final String RES_TYPE_NAME_DRAWABLE = "drawable";

	/**
	 * 属性值对应的类型是mipmap
	 */
	public static final String RES_TYPE_NAME_MIPMAP = "mipmap";

	/**
	 * 当前就是需要修改的皮肤，不需要修改
	 */
	public static final int SKIN_EXCHANGE_NOT_EXCHANGE = -1;

	/**
	 * 皮肤文件不存在
	 */
	public static final int SKIN_EXCHANGE_FILE_NOT_EXISTS = -2;

	/**
	 * 皮肤文件损坏
	 */
	public static final int SKIN_EXCHANGE_FILE_DAMAGE = -3;

	/**
	 * 皮肤文件的签名错误
	 */
	public static final int SKIN_EXCHANGE_SIGNATURE_ERROR = -4;

	/**
	 * 皮肤文件完整有效
	 */
	public static final int SKIN_EXCHANGE_FILE_VALID = 0;

	/**
	 * 换肤成功
	 */
	public static final int SKIN_EXCHANGE_SUCCESS = 1;

}
