package com.yyxnb.view.proxy.imageloader

import android.net.Uri

import java.io.File

/**
 * 图片加载类
 * 策略或者静态代理模式，开发者只需要关心ImageLoader + LoaderOptions
 */
object ImageHelper {

    private var sLoader: ILoaderProxy? = null

    /**
     * 提供全局替换图片加载框架的接口，若切换其它框架，可以实现一键全局替换
     */
    fun setGlobalImageLoader(loader: ILoaderProxy) {
        sLoader = loader
    }

    fun load(url: String): LoaderOptions {
        return LoaderOptions(url)
    }

    fun load(drawable: Int): LoaderOptions {
        return LoaderOptions(drawable)
    }

    fun load(file: File): LoaderOptions {
        return LoaderOptions(file)
    }

    fun load(uri: Uri): LoaderOptions {
        return LoaderOptions(uri)
    }

    /**
     * 优先使用实时设置的图片loader，其次使用全局设置的图片loader
     * @param options
     */
    fun loadOptions(options: LoaderOptions) {
        if (options.loader != null) {
            options.loader!!.loadImage(options)
        } else {
            checkNotNull()
            sLoader!!.loadImage(options)
        }
    }

    fun clearMemoryCache() {
        checkNotNull()
        sLoader!!.clearMemoryCache()
    }

    fun clearDiskCache() {
        checkNotNull()
        sLoader!!.clearDiskCache()
    }

    private fun checkNotNull() {
        if (sLoader == null) {
            throw NullPointerException("you must be set your imageLoader at first!")
        }
    }


}