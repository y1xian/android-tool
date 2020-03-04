package com.yyxnb.arch.delegate

import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import com.yyxnb.arch.interfaces.IActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

class ActivityDelegate (private val iActivity: IActivity) : CoroutineScope by MainScope() {


    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {  //判断得到的焦点控件是否包含EditText
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            //得到输入框在屏幕中上下左右的位置
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(event.x > left && event.x < right
                    && event.y > top && event.y < bottom)
        }
        // 如果焦点不是EditText则忽略
        return false
    }
}