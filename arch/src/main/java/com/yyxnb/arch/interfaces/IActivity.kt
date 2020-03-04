package com.yyxnb.arch.interfaces

import android.os.Bundle
import com.yyxnb.arch.annotations.SwipeStyle
import com.yyxnb.arch.delegate.ActivityDelegate
import com.yyxnb.arch.delegate.FragmentDelegate

/**
 *
 */
interface IActivity : IView {

    fun getBaseDelegate(): ActivityDelegate?

    fun initWindows() {}

    fun initArgs(extras: Bundle?): Boolean = true

    fun setSwipeBack(@SwipeStyle mSwipeBack: Int)

}