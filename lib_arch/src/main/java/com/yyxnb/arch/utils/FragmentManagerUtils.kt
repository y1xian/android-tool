package com.yyxnb.arch.utils

import android.support.v4.app.Fragment
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

/**
 * 管理所有 fragment
 */
object FragmentManagerUtils : Serializable {

    private var fragmentStack: Stack<Fragment>? = null

    /**
     * 获取当前Fragment个数
     */
    val count: Int
        get() = fragmentStack!!.size

    fun getFragmentStack(): List<Fragment> {
        val list = ArrayList<Fragment>()
        list.clear()
        if (fragmentStack != null) {
            list.addAll(fragmentStack!!)
        }
        return list
    }

    /**
     * 添加Fragment到堆栈
     */
    @Synchronized
    fun pushFragment(fragment: Fragment) {
        if (fragmentStack == null) {
            fragmentStack = Stack()
        }
        fragmentStack!!.add(fragment)
    }

    /**
     * 获取当前Fragment
     */
    fun currentFragment(): Fragment {
        var fragment: Fragment? = null
        if (!fragmentStack!!.empty()) {
            fragment = fragmentStack!!.lastElement()
        }
        return fragment!!
    }

    /**
     * 上一个fragment [FragmentManagerUtils.count]} > 1
     */
    fun beforeFragment(): Fragment {
        var fragment: Fragment? = null
        if (count > 1) {
            fragment = getFragmentStack()[count - 2]
        }
        return fragment!!
    }

    /**
     * 结束当前Fragment
     */
    fun finishFragment() {
        val fragment = fragmentStack!!.lastElement()
        killFragment(fragment)
    }

    /**
     * 结束指定的Fragment
     */
    @Synchronized
    fun killFragment(fragment: Fragment?) {
        var _fragment = fragment
        if (_fragment != null) {
//            _fragment.finish()
            fragmentStack!!.remove(_fragment)
            _fragment = null
        }
    }

    /**
     * 结束所有Fragment
     */
    fun killAllFragment() {
        if (fragmentStack != null) {
            while (!fragmentStack!!.empty()) {
                val fragment = currentFragment()
                killFragment(fragment)
            }
            fragmentStack!!.clear()
        }
    }

    fun onDestroy() {
        fragmentStack = Stack()
    }

}