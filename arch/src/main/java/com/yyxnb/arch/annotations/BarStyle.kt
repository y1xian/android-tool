package com.yyxnb.arch.annotations

import android.support.annotation.IntDef
import com.yyxnb.arch.annotations.BarStyle.Companion.DarkContent
import com.yyxnb.arch.annotations.BarStyle.Companion.LightContent
import com.yyxnb.arch.annotations.BarStyle.Companion.NONE

@IntDef(DarkContent, LightContent, NONE)
@Target(AnnotationTarget.FILE, AnnotationTarget.VALUE_PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class BarStyle {
    companion object {
        // 深色
        const val DarkContent = 1
        // 浅色
        const val LightContent = 2
        const val NONE = 0
    }
}
