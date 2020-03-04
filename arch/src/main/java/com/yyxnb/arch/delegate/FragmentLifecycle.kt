package com.yyxnb.arch.delegate

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import com.yyxnb.arch.interfaces.IFragment
import com.yyxnb.arch.interfaces.IFragmentLifecycle

object FragmentLifecycle : FragmentManager.FragmentLifecycleCallbacks() {

    private val cacheDelegate by lazy { HashMap<String, IFragmentLifecycle>() }

    private lateinit var IFragmentLifecycle: IFragmentLifecycle

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        forwardDelegateFunction(fm, f) { IFragmentLifecycle.onAttached(context) }
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        forwardDelegateFunction(fm, f) { IFragmentLifecycle.onCreated(savedInstanceState) }
    }

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        forwardDelegateFunction(fm, f) { IFragmentLifecycle.onViewCreated(v, savedInstanceState) }
    }

    override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        forwardDelegateFunction(fm, f) { IFragmentLifecycle.onActivityCreate(savedInstanceState) }
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        forwardDelegateFunction(fm, f) { IFragmentLifecycle.onStarted() }
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        forwardDelegateFunction(fm, f) { IFragmentLifecycle.onResumed() }
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        forwardDelegateFunction(fm, f) { IFragmentLifecycle.onPaused() }
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        forwardDelegateFunction(fm, f) { IFragmentLifecycle.onStopped() }
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        forwardDelegateFunction(fm, f) { IFragmentLifecycle.onSaveInstanceState(outState) }
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        forwardDelegateFunction(fm, f) { IFragmentLifecycle.onViewDestroyed() }
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        forwardDelegateFunction(fm, f) { IFragmentLifecycle.onDestroyed() }
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        forwardDelegateFunction(fm, f) {
            IFragmentLifecycle.onDetached()
            cacheDelegate.clear()
        }
    }

    private fun forwardDelegateFunction(fm: FragmentManager, f: Fragment, block: () -> Unit) {

        if (f !is IFragment) return

        if (!this::IFragmentLifecycle.isInitialized || !IFragmentLifecycle.isAdded()) {

            val key = f.javaClass.name

            IFragmentLifecycle = cacheDelegate[key] ?: newDelegate(fm, f, key)
        }

        block()
    }

    private fun newDelegate(fm: FragmentManager, f: Fragment, key: String): IFragmentLifecycle {
        return realNewDelegate(fm, f).also { cacheDelegate[key] = it }
    }

    private fun realNewDelegate(fm: FragmentManager, f: Fragment): IFragmentLifecycle {
        return FragmentLifecycleDelegate(fm, f)
    }

}