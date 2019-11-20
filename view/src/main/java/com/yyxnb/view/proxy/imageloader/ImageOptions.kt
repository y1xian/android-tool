package com.yyxnb.view.proxy.imageloader

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.annotation.DrawableRes
import android.widget.ImageView

import java.io.File

/**
 * 该类为图片加载框架的通用属性封装，不能耦合任何一方的框架
 */
class ImageOptions {
    var placeholderResId: Int = 0
    var errorResId: Int = 0
    var isCenterCrop: Boolean = false
    var isCenterInside: Boolean = false
    var skipLocalCache: Boolean = false //是否缓存到本地
    var skipNetCache: Boolean = false
    var config: Bitmap.Config = Bitmap.Config.RGB_565
    var targetWidth: Int = 0
    var targetHeight: Int = 0
    var bitmapAngle: Int = 0 //圆角角度
    var degrees: Float = 0.toFloat() //旋转角度.注意:picasso针对三星等本地图片，默认旋转回0度，即正常位置。此时不需要自己rotate
    var placeholder: Drawable? = null
    var targetView: ImageView? = null//targetView展示图片
    var callBack: BitmapCallBack? = null
    var url: String? = null
    var file: File? = null
    var drawableResId: Int = 0
    var uri: Uri? = null
    var imageProxy: IImageProxy? = null//实时切换图片加载库

    constructor(url: String) {
        this.url = url
    }

    constructor(file: File) {
        this.file = file
    }

    constructor(drawableResId: Int) {
        this.drawableResId = drawableResId
    }

    constructor(uri: Uri) {
        this.uri = uri
    }

    fun into(targetView: ImageView) {
        this.targetView = targetView
        ImageHelper.loadOptions(this)
    }

    fun bitmap(callBack: BitmapCallBack) {
        this.callBack = callBack
        ImageHelper.loadOptions(this)
    }

    fun loader(imageImage: IImageProxy): ImageOptions {
        this.imageProxy = imageImage
        return this
    }

    fun placeholder(placeholderResId: Int): ImageOptions {
        this.placeholderResId = placeholderResId
        return this
    }

    fun placeholder(placeholder: Drawable): ImageOptions {
        this.placeholder = placeholder
        return this
    }

    fun error(errorResId: Int): ImageOptions {
        this.errorResId = errorResId
        return this
    }

    fun centerCrop(): ImageOptions {
        isCenterCrop = true
        return this
    }

    fun centerInside(): ImageOptions {
        isCenterInside = true
        return this
    }

    fun config(config: Bitmap.Config): ImageOptions {
        this.config = config
        return this
    }

    fun resize(targetWidth: Int, targetHeight: Int): ImageOptions {
        this.targetWidth = targetWidth
        this.targetHeight = targetHeight
        return this
    }

    /**
     * 圆角
     * @param bitmapAngle   度数
     * @return
     */
    fun angle(bitmapAngle: Int): ImageOptions {
        this.bitmapAngle = bitmapAngle
        return this
    }

    fun skipLocalCache(skipLocalCache: Boolean): ImageOptions {
        this.skipLocalCache = skipLocalCache
        return this
    }

    fun skipNetCache(skipNetCache: Boolean): ImageOptions {
        this.skipNetCache = skipNetCache
        return this
    }

    fun rotate(degrees: Float): ImageOptions {
        this.degrees = degrees
        return this
    }

}