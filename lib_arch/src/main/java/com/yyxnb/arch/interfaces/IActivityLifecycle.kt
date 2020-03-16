package com.yyxnb.arch.interfaces

import android.app.Activity
import android.os.Bundle

/**
 * activity 代理接口
 */
interface IActivityLifecycle {

    fun onCreate(savedInstanceState: Bundle?)

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onSaveInstanceState(activity: Activity?, outState: Bundle?)

    fun onDestroy()
}