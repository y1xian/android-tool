package com.yyxnb.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent


/**
 * 是否禁止滑动与滑动动画 && 滑动冲突
 */
class NoScrollViewPager : ViewPager {

    private var mLastX = 0
    private var mLastY:Int = 0
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

    @Suppress("ControlFlowWithEmptyBody")
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        //Log.i(TAG, "onInterceptTouchEvent() called with: ev = [" + ev + "]");
        val x = ev.x.toInt()
        val y = ev.y.toInt()
        var intercept = false
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_MOVE -> if (isHorizontalScroll(x, y)) {
                //除了在 第一页的手指向右滑 ， 最后一页的左滑，其他时刻都是父控件需要拦截事件
                intercept = if (isReactFirstPage() && isScrollRight(x)) {
                    //Log.e(TAG, "第一页的手指向右滑]");
                    false
                } else if (isReachLastPage() && isScrollLeft(x)) {
                    //Log.e(TAG, "最后一页的左滑");
                    false
                } else {
                    //Log.e(TAG, "其他情况");
                    true
                }
            } else {
            }
            MotionEvent.ACTION_UP -> {
            }
            else -> {
            }
        }
        mLastX = x
        mLastY = y
        val onInterceptTouchEvent = !noScroll && super.onInterceptTouchEvent(ev)
        return intercept || onInterceptTouchEvent
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        super.setCurrentItem(item, smoothScroll)
    }

    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item, noScrollAnim)
    }

    //是否在水平滑动
    private fun isHorizontalScroll(x: Int, y: Int): Boolean {
        return Math.abs(y - mLastY) < Math.abs(x - mLastX)
    }

    //是否未到达最后一页
    private fun isReachLastPage(): Boolean {
        val adapter = adapter
        return null != adapter && adapter.count - 1 == currentItem
    }

    //是否在第一页
    private fun isReactFirstPage(): Boolean {
        return currentItem == 0
    }

    //是否左滑
    private fun isScrollLeft(x: Int): Boolean {
        return x - mLastX < 0
    }

    private fun isScrollRight(x: Int): Boolean {
        return x - mLastX > 0
    }
}