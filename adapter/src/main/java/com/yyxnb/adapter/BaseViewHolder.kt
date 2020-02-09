package com.yyxnb.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.util.Linkify
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.*


class BaseViewHolder(val convertView: View) : RecyclerView.ViewHolder(convertView) {

    private val mViews: SparseArray<View> = SparseArray()

    @Suppress("UNCHECKED_CAST")
    fun <T : View> getView(viewId: Int): T {
        val view = getViewOrNull<T>(viewId)
        checkNotNull(view) { "No view found with id $viewId" }
        return view
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : View> getViewOrNull(viewId: Int): T? {
        val view = mViews.get(viewId)
        if (view == null) {
            itemView.findViewById<T>(viewId)?.let {
                mViews.put(viewId, it)
                return it
            }
        }
        return view as? T
    }

    companion object {
        fun createViewHolder(itemView: View): BaseViewHolder {
            return BaseViewHolder(itemView)
        }

        fun createViewHolder(context: Context,
                             parent: ViewGroup, layoutId: Int): BaseViewHolder {
            val itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                    false)
            return BaseViewHolder(itemView)
        }
    }

    fun <T : View> Int.findView(): T? {
        return itemView.findViewById(this)
    }

    /****以下为辅助方法 */

    fun setText(viewId: Int, text: CharSequence?): BaseViewHolder {
        getView<TextView>(viewId).text = text
        return this
    }

    fun setText(viewId: Int, text: String?): BaseViewHolder {
        getView<TextView>(viewId).text = text
        return this
    }

    fun setImageResource(viewId: Int, resId: Int): BaseViewHolder {
        getView<ImageView>(viewId).setImageResource(resId)
        return this
    }

    fun setImageBitmap(viewId: Int, bitmap: Bitmap?): BaseViewHolder {
        getView<ImageView>(viewId).setImageBitmap(bitmap)
        return this
    }

    fun setImageDrawable(viewId: Int, drawable: Drawable?): BaseViewHolder {
        getView<ImageView>(viewId).setImageDrawable(drawable)
        return this
    }

    fun setBackgroundColor(viewId: Int, color: Int): BaseViewHolder {
        getView<View>(viewId).setBackgroundColor(color)
        return this
    }

    fun setBackgroundRes(viewId: Int, backgroundRes: Int): BaseViewHolder {
        getView<View>(viewId).setBackgroundResource(backgroundRes)
        return this
    }

    fun setTextColor(viewId: Int, textColor: Int): BaseViewHolder? {
        getView<TextView>(viewId).setTextColor(textColor)
        return this
    }


    @SuppressLint("NewApi", "ObsoleteSdkInt")
    fun setAlpha(viewId: Int, value: Float): BaseViewHolder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView<View>(viewId).setAlpha(value)
        } else { // Pre-honeycomb hack to set Alpha value
            val alpha = AlphaAnimation(value, value)
            alpha.duration = 0
            alpha.fillAfter = true
            getView<View>(viewId).startAnimation(alpha)
        }
        return this
    }

    fun setVisible(viewId: Int, visible: Boolean): BaseViewHolder {
        val view: View = getView(viewId)
        view.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    /**
     * 链接
     */
    fun setLinkify(viewId: Int): BaseViewHolder {
        Linkify.addLinks(getView<TextView>(viewId), Linkify.ALL)
        return this
    }

    /**
     * 横线
     */
    fun setHorizontalLine(viewId: Int): BaseViewHolder {
        // 中间加横线 ， 添加Paint.ANTI_ALIAS_FLAG是线会变得清晰去掉锯齿
        getView<TextView>(viewId).paint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
        return this
    }

    /**
     * 下滑线
     */
    fun setGlidinglLine(viewId: Int): BaseViewHolder {
        // 中间加横线 ， 添加Paint.ANTI_ALIAS_FLAG是线会变得清晰去掉锯齿
        getView<TextView>(viewId).paint.flags = Paint.UNDERLINE_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
        return this
    }

    /**
     * 字体
     */
    fun setTypeface(typeface: Typeface?, vararg viewIds: Int): BaseViewHolder {
        for (viewId in viewIds) {
            val view: TextView = getView(viewId)
            view.typeface = typeface
            view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        }
        return this
    }

    fun setProgress(viewId: Int, progress: Int): BaseViewHolder {
        getView<ProgressBar>(viewId).progress = progress
        return this
    }

    fun setProgress(viewId: Int, progress: Int, max: Int): BaseViewHolder {
        val view: ProgressBar = getView(viewId)
        view.max = max
        view.progress = progress
        return this
    }

    fun setMax(viewId: Int, max: Int): BaseViewHolder {
        getView<ProgressBar>(viewId).max = max
        return this
    }

    fun setRating(viewId: Int, rating: Float): BaseViewHolder {
        getView<RatingBar>(viewId).rating = rating
        return this
    }

    fun setRating(viewId: Int, rating: Float, max: Int): BaseViewHolder {
        val view: RatingBar = getView(viewId)
        view.max = max
        view.rating = rating
        return this
    }

    fun setTag(viewId: Int, tag: Any?): BaseViewHolder {
        getView<View>(viewId).tag = tag
        return this
    }

    fun setTag(viewId: Int, key: Int, tag: Any?): BaseViewHolder {
        getView<View>(viewId).setTag(key, tag)
        return this
    }

    fun setChecked(viewId: Int, checked: Boolean): BaseViewHolder {
        val view = getView<View>(viewId) as Checkable
        view.isChecked = checked
        return this
    }

    fun setEnabled(viewId: Int, isEnabled: Boolean): BaseViewHolder {
        getView<View>(viewId).isEnabled = isEnabled
        return this
    }

    /**
     * 关于事件的
     */
    fun setOnClickListener(viewId: Int,
                           listener: View.OnClickListener?): BaseViewHolder {
        val view: View = getView(viewId)
        view.setOnClickListener(listener)
        return this
    }

    fun setOnTouchListener(viewId: Int,
                           listener: OnTouchListener?): BaseViewHolder {
        val view: View = getView(viewId)
        view.setOnTouchListener(listener)
        return this
    }

    fun setOnLongClickListener(viewId: Int,
                               listener: OnLongClickListener?): BaseViewHolder {
        val view: View = getView(viewId)
        view.setOnLongClickListener(listener)
        return this
    }


}