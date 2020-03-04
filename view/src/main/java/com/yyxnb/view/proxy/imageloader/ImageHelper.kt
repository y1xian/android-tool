package com.yyxnb.view.proxy.imageloader

import android.net.Uri

import java.io.File

/**
 * 图片加载类
 * 策略或者静态代理模式，开发者只需要关心ImageHelper + ImageOptions
 */
object ImageHelper {

    @Volatile
    private var image: IImage? = null

    /**
     * 提供全局替换图片加载框架的接口，若切换其它框架，可以实现一键全局替换
     */
    fun init(image: IImage) {
        this.image = image
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
    @Synchronized
    fun loadOptions(options: ImageOptions) {
        if (options.image != null) {
            options.image!!.loadImage(options)
        } else {
            checkNotNull()
            image!!.loadImage(options)
        }
    }

    fun clearMemoryCache() {
        checkNotNull()
        image!!.clearMemoryCache()
    }

    fun clearDiskCache() {
        checkNotNull()
        image!!.clearDiskCache()
    }

    private fun checkNotNull() {
        if (image == null) {
            throw NullPointerException("you must be set your imageProxy at first!")
        }
    }


}