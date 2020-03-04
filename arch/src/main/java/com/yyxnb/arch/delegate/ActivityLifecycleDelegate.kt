package com.yyxnb.arch.delegate

import android.app.Activity
import android.os.Bundle
import com.yyxnb.arch.interfaces.IActivity
import com.yyxnb.arch.interfaces.IActivityLifecycle

/**
 * activity 代理实现类
 */
open class ActivityLifecycleDelegate(private val activity: Activity) : IActivityLifecycle {

    private val iActivity = activity as IActivity

    override fun onCreate(savedInstanceState: Bundle?) {
    }

    override fun onStart() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun onStop() {
    }

    override fun onDestroy() {
    }

    override fun onSaveInstanceState(activity: Activity?, outState: Bundle?) {
    }
}