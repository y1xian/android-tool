package com.yyxnb.tools

import android.os.Handler
import android.os.Looper

/**
 * 短时间内, 只会执行最后一个[Runnable]的[Handler]
 */
class OnceHandler(looper: Looper = Looper.getMainLooper()) : Handler(looper) {

    private var innerRunnable: InnerRunnable? = null

    fun hasCallbacks(): Boolean = innerRunnable != null

    fun once(runnable: Runnable, delayMillis: Long = 0) {
        once(delayMillis, runnable)
    }

    fun once(delayMillis: Long = 0, runnable: Runnable) {

        clear()

        InnerRunnable(runnable).apply {
            innerRunnable = this
            postDelayed(this, delayMillis)
        }
    }

    fun once(delayMillis: Long = 0, run: () -> Unit) {
        once(delayMillis, Runnable(run))
    }

    fun clear() {
        innerRunnable?.let {
            removeCallbacks(it)
        }
        innerRunnable = null
    }

    private inner class InnerRunnable(val raw: Runnable) : Runnable {
        override fun run() {
            raw.run()
            innerRunnable = null
        }
    }
}