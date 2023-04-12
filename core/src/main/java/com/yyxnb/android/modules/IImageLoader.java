package com.yyxnb.android.modules;

import android.content.Context;
import android.widget.ImageView;

/**
 * IImageLoader
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2023/4/12
 */
public interface IImageLoader {

	/**
	 * 图片展示
	 *
	 * @param url       图片地址
	 * @param imageView imageView
	 */
	void displayImage(Object url, ImageView imageView);

	/**
	 * 带占位，错误图
	 *
	 * @param url         图片地址
	 * @param imageView   imageView
	 * @param placeholder 占位图
	 * @param error       错误图
	 */
	void displayImage(Object url, ImageView imageView, int placeholder, int error);

	/**
	 * gif
	 *
	 * @param url       图片地址
	 * @param imageView imageView
	 */
	void displayGif(Object url, ImageView imageView);

	/**
	 * 加载圆形图片
	 *
	 * @param url       图片地址
	 * @param imageView imageView
	 */
	void displayCircleImage(Object url, ImageView imageView);

	/**
	 * 加载圆角图片
	 *
	 * @param url       图片地址
	 * @param imageView imageView
	 * @param radius    圆角大小
	 */
	void displayRoundImage(Object url, ImageView imageView, int radius);

	/**
	 * 加载图片指定大小
	 *
	 * @param url       图片地址
	 * @param imageView imageView
	 * @param width     宽度
	 * @param height    高度
	 */
	void displaySizeImage(Object url, ImageView imageView, int width, int height);

	/**
	 * 预加载图片
	 *
	 * @param context 上下文
	 * @param url     图片地址
	 */
	void preload(Context context, Object url);

}
