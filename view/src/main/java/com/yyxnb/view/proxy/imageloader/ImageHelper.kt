package com.yyxnb.view.proxy.imageloader

import android.net.Uri

import java.io.File

/**
 * 图片加载类
 * 策略或者静态代理模式，开发者只需要关心ImageHelper + ImageOptions
 */
object ImageHelper {

    private var imageProxy: IImageProxy? = null

    /**
     * 提供全局替换图片加载框架的接口，若切换其它框架，可以实现一键全局替换
     */
    fun init(image: IImageProxy) {
        imageProxy = image
    }

    fun load(url: String): ImageOptions {
        return ImageOptions(url)
    }

    fun load(drawable: Int): ImageOptions {
        return ImageOptions(drawable)
    }

    fun load(file: File): ImageOptions {
        return ImageOptions(file)
    }

    fun load(uri: Uri): ImageOptions {
        return ImageOptions(uri)
    }

    /**
     * 优先使用实时设置的图片loader，其次使用全局设置的图片loader
     * @param options
     */
    fun loadOptions(options: ImageOptions) {
        if (options.imageProxy != null) {
            options.imageProxy!!.loadImage(options)
        } else {
            checkNotNull()
            imageProxy!!.loadImage(options)
        }
    }

    fun clearMemoryCache() {
        checkNotNull()
        imageProxy!!.clearMemoryCache()
    }

    fun clearDiskCache() {
        checkNotNull()
        imageProxy!!.clearDiskCache()
    }

    private fun checkNotNull() {
        if (imageProxy == null) {
            throw NullPointerException("you must be set your imageProxy at first!")
        }
    }


}