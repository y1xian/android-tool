package com.yyxnb.arch.delegate

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import com.yyxnb.arch.interfaces.IFragment
import com.yyxnb.arch.interfaces.IFragmentLifecycle
import com.yyxnb.arch.utils.FragmentManagerUtils
import com.yyxnb.utils.log.LogUtils

/**
 * fragment 代理实现类
 */
open class FragmentLifecycleDelegate(private val fragmentManager: FragmentManager,
                                     private val fragment: Fragment) : IFragmentLifecycle {

    private val iFragment = fragment as IFragment

    override fun onAttached(context: Context) {
    }

    override fun onCreated(savedInstanceState: Bundle?) {
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
    }

    override fun isAdded(): Boolean = fragment.isAdded

    override fun onActivityCreate(savedInstanceState: Bundle?) {
    }

    override fun onStarted() {
    }

    override fun onResumed() {
    }

    override fun onPaused() {
    }

    override fun onStopped() {
    }

    override fun onSaveInstanceState(outState: Bundle) {
    }

    override fun onViewDestroyed() {
    }

    override fun onDestroyed() {
    }

    override fun onDetached() {

    }
}