package com.yyxnb.what.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.request.target.Target;

/**
 * 图处加载类，外界唯一调用类
 */
public class ImageHelper {

    private static final IImageLoader HELPER = new GlideImageLoader();

    /**
     * 图片展示
     *
     * @param url       图片地址
     * @param imageView
     */
    public static void displayImage(Object url, ImageView imageView) {
        HELPER.displayImage(url, imageView);
    }

    /**
     * 带占位，错误图
     *
     * @param url         图片地址
     * @param imageView
     * @param placeholder 占位图
     * @param error       错误图
     */
    public static void displayImage(Object url, ImageView imageView, int placeholder, int error) {
        HELPER.displayImage(url, imageView, placeholder, error);
    }

    /**
     * gif
     *
     * @param url       图片地址
     * @param imageView
     */
    public static void displayGif(Object url, ImageView imageView) {
        HELPER.displayGif(url, imageView);
    }

    /**
     * 加载圆形图片
     *
     * @param url
     * @param imageView
     */
    public static void displayCircleImage(Object url, ImageView imageView) {
        HELPER.displayCircleImage(url, imageView);
    }

    /**
     * 加载圆角图片
     *
     * @param url
     * @param imageView
     * @param radius    圆角大小
     */
    public static void displayRoundImage(Object url, ImageView imageView, int radius) {
        HELPER.displayRoundImage(url, imageView, radius);
    }

    /**
     * 加载图片指定大小
     *
     * @param context
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    public static void displaySizeImage(Context context, Object url, ImageView imageView, int width, int height) {
        HELPER.displaySizeImage(context, url, imageView, width, height);
    }

    /**
     * 为非view加载图片
     */
    public static void displayImageForTarget(Context context, Target target, Object url) {
        HELPER.displayImageForTarget(context, target, url);
    }

    /**
     * 预加载图片
     *
     * @param context 上下文
     * @param url     地址
     */
    public static void preload(Context context, Object url) {
        HELPER.preload(context, url);
    }

}
