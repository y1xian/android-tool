package com.yyxnb.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * 是否禁止滑动与滑动动画
 */
class NoScrollViewPager : ViewPager {

    private var noScroll = true
    private var noScrollAnim = false

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}

    /**
     * 设置是否能左右滑动
     * @param noScroll true 不能滑动
     */
    fun setNoScroll(noScroll: Boolean) {
        this.noScroll = noScroll
    }

    /**
     * 设置没有滑动动画
     * @param noAnim false 无动画
     */
    fun setScrollAnim(noAnim: Boolean) {
        noScrollAnim = noAnim
    }

    override fun onTouchEvent(arg0: MotionEvent): Boolean {
        return !noScroll && super.onTouchEvent(arg0)
    }

    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
        return !noScroll && super.onInterceptTouchEvent(arg0)
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        super.setCurrentItem(item, smoothScroll)
    }

    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item, noScrollAnim)
    }
}