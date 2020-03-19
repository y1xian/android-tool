package com.yyxnb.arch.interfaces

import com.yyxnb.arch.delegate.FragmentDelegate

/**
 *
 */
interface IFragment : IView {

    fun getBaseDelegate(): FragmentDelegate?

    /**
     * 用户可见时候调用
     */
    fun onVisible(){}

    /**
     * 用户不可见时候调用
     */
    fun onInVisible(){}

}