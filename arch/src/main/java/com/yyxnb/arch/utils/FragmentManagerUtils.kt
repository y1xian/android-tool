package com.yyxnb.arch.utils

import com.yyxnb.arch.base.BaseFragment
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

object FragmentManagerUtils : Serializable {

    private var fragmentStack: Stack<BaseFragment>? = null


    /**
     * 获取当前Fragment个数
     */
    val count: Int
        get() = fragmentStack!!.size

    fun getFragmentStack(): List<BaseFragment> {
        val list = ArrayList<BaseFragment>()
        list.clear()
        if (fragmentStack != null) {
            list.addAll(fragmentStack!!)
        }
        return list
    }

    /**
     * 添加Fragment到堆栈
     */
    fun pushFragment(fragment: BaseFragment) {
        if (fragmentStack == null) {
            fragmentStack = Stack()
        }
        fragmentStack!!.add(fragment)
    }

    /**
     * 获取当前Fragment
     */
    fun currentFragment(): BaseFragment {
        var fragment: BaseFragment? = null
        if (!fragmentStack!!.empty()) {
            fragment = fragmentStack!!.lastElement()
        }
        return fragment!!
    }

    /**
     * 上一个fragment [FragmentManagerUtils.count]} > 1
     */
    fun beforeFragment(): BaseFragment {
        var fragment: BaseFragment? = null
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
    fun killFragment(fragment: BaseFragment?) {
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