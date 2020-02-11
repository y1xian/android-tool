package com.yyxnb.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.os.Build
import android.support.annotation.CheckResult
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.yyxnb.utils.AppConfig.context
import java.io.Serializable

/**
 * 在系统的Toast基础上封装
 */
object ToastUtils : Serializable {
    @ColorInt
    private val DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF")
    @ColorInt
    private val ERROR_COLOR = Color.parseColor("#FD4C5B")
    @ColorInt
    private val INFO_COLOR = Color.parseColor("#3F51B5")
    @ColorInt
    private val SUCCESS_COLOR = Color.parseColor("#388E3C")
    @ColorInt
    private val WARNING_COLOR = Color.parseColor("#FFA900")
    private const val TOAST_TYPEFACE = "sans-serif-condensed"
    private var currentToast: Toast? = null
    private val mContext = context
    //*******************************************普通 使用ApplicationContext 方法*********************
    /**
     * Toast 替代方法 ：立即显示无需等待
     */
    private var mToast: Toast? = null
    private var mExitTime: Long = 0

    fun normal(message: String) {
        normal(mContext, message, Toast.LENGTH_SHORT, null, false)!!.show()
    }

    fun normal(message: String, icon: Drawable?) {
        normal(mContext, message, Toast.LENGTH_SHORT, icon, true)!!.show()
    }

    fun normal(message: String, duration: Int) {
        normal(mContext, message, duration, null, false)!!.show()
    }

    fun normal(message: String, duration: Int, icon: Drawable?) {
        normal(mContext, message, duration, icon, true)!!.show()
    }

    fun normal(message: String, duration: Int, icon: Drawable?, withIcon: Boolean): Toast? {
        return custom(mContext, message, icon, DEFAULT_TEXT_COLOR, duration, withIcon)
    }

    @CheckResult
    fun normal(context: Context = mContext, message: String, duration: Int = Toast.LENGTH_SHORT, icon: Drawable? = null, withIcon: Boolean = false): Toast? {
        return custom(context, message, icon, DEFAULT_TEXT_COLOR, duration, withIcon)
    }

    @CheckResult
    fun custom(context: Context, message: String, icon: Drawable?, @ColorInt textColor: Int, duration: Int, withIcon: Boolean): Toast? {
        return custom(context, message, icon, textColor, -1, duration, withIcon, false)
    }

    //*******************************************内需方法********************************************
    @CheckResult
    fun custom(context: Context, message: String, @DrawableRes iconRes: Int, @ColorInt textColor: Int, @ColorInt tintColor: Int, duration: Int, withIcon: Boolean, shouldTint: Boolean): Toast? {
        return custom(context, message, getDrawable(context, iconRes), textColor, tintColor, duration, withIcon, shouldTint)
    }

    @CheckResult
    fun custom(context: Context, message: String, icon: Drawable?, @ColorInt textColor: Int, @ColorInt tintColor: Int, duration: Int, withIcon: Boolean, shouldTint: Boolean): Toast? {
        if (currentToast == null) {
            currentToast = Toast(context)
        } else {
            currentToast!!.cancel()
            currentToast = null
            currentToast = Toast(context)
        }
        val toastLayout = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.toast_layout, null)
        val toastIcon = toastLayout.findViewById<ImageView>(R.id.toast_icon)
        val toastTextView = toastLayout.findViewById<TextView>(R.id.toast_text)
        val drawableFrame: Drawable = if (shouldTint) {
            tint9PatchDrawableFrame(context, tintColor)
        } else {
            getDrawable(context, R.drawable.toast_frame)
        }
        setBackground(toastLayout, drawableFrame)
        if (withIcon) {
            requireNotNull(icon) { "Avoid passing 'icon' as null if 'withIcon' is set to true" }
            setBackground(toastIcon, icon)
        } else {
            toastIcon.visibility = View.GONE
        }
        toastTextView.setTextColor(textColor)
        toastTextView.text = message
        toastTextView.typeface = Typeface.create(TOAST_TYPEFACE, Typeface.NORMAL)
        currentToast!!.view = toastLayout
        currentToast!!.duration = duration
        //        currentToast.setGravity(Gravity.CENTER, 0, 0);
        return currentToast
    }

    fun tint9PatchDrawableFrame(context: Context, @ColorInt tintColor: Int): Drawable {
        val toastDrawable = getDrawable(context, R.drawable.toast_frame) as NinePatchDrawable
        toastDrawable.colorFilter = PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
        return toastDrawable
    }

    //===========================================内需方法============================================
//******************************************系统 Toast 替代方法***************************************
    @SuppressLint("ObsoleteSdkInt")
    fun setBackground(view: View, drawable: Drawable?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.background = drawable
        } else {
            view.setBackgroundDrawable(drawable)
        }
    }

    fun getDrawable(context: Context, @DrawableRes id: Int): Drawable {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            context.getDrawable(id)
        } else {
            context.resources.getDrawable(id)
        }
    }

    /**
     * 封装了Toast的方法 :需要等待
     *
     * @param context Context
     * @param str     要显示的字符串
     * @param isLong  Toast.LENGTH_LONG / Toast.LENGTH_SHORT
     */
    fun showToast(context: Context?, str: String, isLong: Boolean) {
        if (isLong) {
            Toast.makeText(context, str, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 封装了Toast的方法 :需要等待
     */
    fun showToastShort(str: String) {
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show()
    }

    /**
     * 封装了Toast的方法 :需要等待
     */
    fun showToastShort(resId: Int) {
        Toast.makeText(mContext, mContext.getString(resId), Toast.LENGTH_SHORT).show()
    }

    /**
     * 封装了Toast的方法 :需要等待
     */
    fun showToastLong(str: String) {
        Toast.makeText(mContext, str, Toast.LENGTH_LONG).show()
    }

    /**
     * 封装了Toast的方法 :需要等待
     */
    fun showToastLong(resId: Int) {
        Toast.makeText(mContext, mContext.getString(resId), Toast.LENGTH_LONG).show()
    }

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param msg 显示内容
     */
    @SuppressLint("ShowToast")
    fun showToast(msg: String) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG)
        } else {
            mToast!!.setText(msg)
        }
        mToast!!.show()
    }

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param resId String资源ID
     */
    @SuppressLint("ShowToast")
    fun showToast(resId: Int) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, mContext.getString(resId), Toast.LENGTH_LONG)
        } else {
            mToast!!.setText(mContext.getString(resId))
        }
        mToast!!.show()
    }

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param context  实体
     * @param resId    String资源ID
     * @param duration 显示时长
     */
    fun showToast(context: Context, resId: Int, duration: Int) {
        showToast(context, context.getString(resId), duration)
    }
    //===========================================Toast 替代方法======================================
    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param context  实体
     * @param msg      要显示的字符串
     * @param duration 显示时长
     */
    @SuppressLint("ShowToast")
    fun showToast(context: Context, msg: String, duration: Int) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, duration)
        } else {
            mToast!!.setText(msg)
        }
        mToast!!.show()
    }

    fun doubleClickExit(): Boolean {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            normal("再按一次退出")
            mExitTime = System.currentTimeMillis()
            return false
        }
        return true
    }
}