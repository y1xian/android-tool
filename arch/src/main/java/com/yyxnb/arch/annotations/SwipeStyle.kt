package com.yyxnb.arch.annotations

import android.support.annotation.IntDef
import com.yyxnb.arch.annotations.SwipeStyle.Companion.Edge
import com.yyxnb.arch.annotations.SwipeStyle.Companion.Full
import com.yyxnb.arch.annotations.SwipeStyle.Companion.NONE

@IntDef(Full, Edge, NONE)
@Target(AnnotationTarget.FILE, AnnotationTarget.VALUE_PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
public annotation class SwipeStyle {
    companion object {
        // 全屏
        const val Full = 1
        // 边缘
        const val Edge = 2
        const val NONE = 0
    }
}

