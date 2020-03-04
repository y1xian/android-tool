package com.yyxnb.arch.interfaces

import android.content.Context
import android.os.Bundle
import android.view.View

/**
 * fragment 代理接口
 */
interface IFragmentLifecycle {

    fun onAttached(context: Context)

    fun onCreated(savedInstanceState: Bundle?)

    fun onViewCreated(v: View, savedInstanceState: Bundle?)

    fun onActivityCreate(savedInstanceState: Bundle?)

    fun onStarted()

    fun onResumed()

    fun onPaused()

    fun onStopped()

    fun onSaveInstanceState(outState: Bundle)

    fun onViewDestroyed()

    fun onDestroyed()

    fun onDetached()

    fun isAdded(): Boolean
}