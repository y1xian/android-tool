package com.yyxnb.arch.delegate

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.yyxnb.arch.interfaces.IActivity
import com.yyxnb.arch.interfaces.IActivityLifecycle
import com.yyxnb.arch.utils.ActivityManagerUtils

object ActivityLifecycle : Application.ActivityLifecycleCallbacks {

    private val cacheActivityDelegate by lazy { HashMap<String, IActivityLifecycle>() }

    private lateinit var IActivityLifecycle: IActivityLifecycle

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        activity?.let { ActivityManagerUtils.pushActivity(it) }

        forwardDelegateFunction(activity) { IActivityLifecycle.onCreate(savedInstanceState) }

        registerFragmentCallback(activity)
    }

    private fun registerFragmentCallback(activity: Activity?) {

        if (activity !is FragmentActivity) return

        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(FragmentLifecycle, true)
    }

    override fun onActivityStarted(activity: Activity?) {
        forwardDelegateFunction(activity) { IActivityLifecycle.onStart() }
    }

    override fun onActivityResumed(activity: Activity?) {
        forwardDelegateFunction(activity) { IActivityLifecycle.onResume() }
    }

    override fun onActivityPaused(activity: Activity?) {
        forwardDelegateFunction(activity) { IActivityLifecycle.onPause() }
    }

    override fun onActivityStopped(activity: Activity?) {
        forwardDelegateFunction(activity) { IActivityLifecycle.onStop() }
    }

    override fun onActivityDestroyed(activity: Activity?) {
        activity?.let { ActivityManagerUtils.deleteActivity(it) }

        forwardDelegateFunction(activity) {
            IActivityLifecycle.onDestroy()
            cacheActivityDelegate.clear()
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        forwardDelegateFunction(activity) { IActivityLifecycle.onSaveInstanceState(activity, outState) }
    }

    private fun forwardDelegateFunction(activity: Activity?, block: () -> Unit) {

        if (activity !is IActivity) return

        val key = activity.javaClass.name

        IActivityLifecycle = cacheActivityDelegate[key] ?: newDelegate(activity, key)

        block()
    }

    private fun newDelegate(activity: Activity, key: String): IActivityLifecycle {
        return realNewDelegate(activity).also { cacheActivityDelegate[key] = it }
    }

    private fun realNewDelegate(activity: Activity): IActivityLifecycle {
        return ActivityLifecycleDelegate(activity)
    }
}