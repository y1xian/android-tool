package com.yyxnb.view.proxy.imageloader

import android.graphics.Bitmap

/**
 * Created by JohnsonFan on 2018/3/7.
 */

interface BitmapCallBack {

    fun onBitmapLoaded(bitmap: Bitmap)

    fun onBitmapFailed(e: Exception)

    class EmptyCallback : BitmapCallBack {


        override fun onBitmapLoaded(bitmap: Bitmap) {

        }

        override fun onBitmapFailed(e: Exception) {

        }
    }
}