package com.yyxnb.arch.ext

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus

@Synchronized
fun String.bus(any: Any, delay: Long = 0) {
    LiveEventBus.get(this).postDelay(any, delay)
}

@Suppress("UNCHECKED_CAST", "SYNCHRONIZED_ON_INLINE")
@Synchronized
inline fun <reified T> String.busObserve(lifecycleOwner: LifecycleOwner, crossinline block: (T) -> Unit) {
    LiveEventBus.get(this, T::class.java).observe(lifecycleOwner, Observer { t ->
        t?.let {
            block(it)
        }
    })
}

@Suppress("UNCHECKED_CAST", "SYNCHRONIZED_ON_INLINE")
@Synchronized
inline fun <reified T> String.busObserveSticky(lifecycleOwner: LifecycleOwner, crossinline block: (T) -> Unit) {
    LiveEventBus.get(this, T::class.java).observeSticky(lifecycleOwner, Observer { t ->
        t?.let {
            block(it)
        }
    })
}
