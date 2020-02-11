package com.yyxnb.widget.view

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.widget.TextView

/**
 * 倒计时
 */
class CountdownButton(mContext: Context, attrSet: AttributeSet) : TextView(mContext, attrSet) {

    private val mHandler: Handler = Handler();
    private var mCountTime = 60

    /**
     *   倒计时，并处理点击事件
     */
    fun sendVerifyCode() {
        mHandler.postDelayed(countDown, 0)
    }

    /**
     *  倒计时
     */
    private val countDown = object : Runnable {
        override fun run() {
            this@CountdownButton.text = mCountTime.toString() + "s"
            this@CountdownButton.isEnabled = false

            if (mCountTime > 0) {
                mHandler.postDelayed(this, 1000)
            } else {
                resetCounter()
            }
            mCountTime--
        }
    }

    /**
     *  取消
     */
    fun removeRunable() {
        mHandler.removeCallbacks(countDown)
    }

    //重置按钮状态
    fun resetCounter(vararg text: String) {
        this.isEnabled = true
        if (text.isNotEmpty() && "" != text[0]) {
            this.text = text[0]
        } else {
            this.text = "获取验证码"
        }
        mCountTime = 60
    }
}