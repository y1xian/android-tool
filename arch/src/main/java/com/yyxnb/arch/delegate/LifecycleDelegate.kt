package com.yyxnb.arch.delegate

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import com.yyxnb.arch.delegate.DeferredLifecycleDelegate
import com.yyxnb.arch.delegate.ImmediateLifecycleDelegate

/**
 * Lifecycle 代理
 */
class LifecycleDelegate(lifecycleOwner: LifecycleOwner) : LifecycleObserver {

    private val immediateLifecycleDelegate: ImmediateLifecycleDelegate = ImmediateLifecycleDelegate(lifecycleOwner)
    private val deferredLifecycleDelegate: DeferredLifecycleDelegate = DeferredLifecycleDelegate(lifecycleOwner)

    @JvmOverloads
    fun scheduleTaskAtStarted(runnable: Runnable, interval: Long = 1L) {
        if (interval >= 1L) {
            deferredLifecycleDelegate.scheduleTaskAtStarted(runnable, interval)
        } else {
            immediateLifecycleDelegate.scheduleTaskAtStarted(runnable)
        }
    }

}
