package com.yyxnb.view.rv

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


class ViewHolder(val convertView: View) : RecyclerView.ViewHolder(convertView) {

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
        fun createViewHolder(itemView: View): ViewHolder {
            return ViewHolder(itemView)
        }

        fun createViewHolder(context: Context,
                             parent: ViewGroup, layoutId: Int): ViewHolder {
            val itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                    false)
            return ViewHolder(itemView)
        }
    }

    fun <T : View> Int.findView(): T? {
        return itemView.findViewById(this)
    }

    /****以下为辅助方法 */

    fun setText(viewId: Int, text: CharSequence?): ViewHolder {
        getView<TextView>(viewId).text = text
        return this
    }

    fun setText(viewId: Int, text: String?): ViewHolder {
        getView<TextView>(viewId).text = text
        return this
    }

    fun setImageResource(viewId: Int, resId: Int): ViewHolder {
        getView<ImageView>(viewId).setImageResource(resId)
        return this
    }

    fun setImageBitmap(viewId: Int, bitmap: Bitmap?): ViewHolder {
        getView<ImageView>(viewId).setImageBitmap(bitmap)
        return this
    }

    fun setImageDrawable(viewId: Int, drawable: Drawable?): ViewHolder {
        getView<ImageView>(viewId).setImageDrawable(drawable)
        return this
    }

    fun setBackgroundColor(viewId: Int, color: Int): ViewHolder {
        getView<View>(viewId).setBackgroundColor(color)
        return this
    }

    fun setBackgroundRes(viewId: Int, backgroundRes: Int): ViewHolder {
        getView<View>(viewId).setBackgroundResource(backgroundRes)
        return this
    }

    fun setTextColor(viewId: Int, textColor: Int): ViewHolder? {
        getView<TextView>(viewId).setTextColor(textColor)
        return this
    }


    @SuppressLint("NewApi", "ObsoleteSdkInt")
    fun setAlpha(viewId: Int, value: Float): ViewHolder {
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

    fun setVisible(viewId: Int, visible: Boolean): ViewHolder {
        val view: View = getView(viewId)
        view.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    /**
     * 链接
     */
    fun setLinkify(viewId: Int): ViewHolder {
        Linkify.addLinks(getView<TextView>(viewId), Linkify.ALL)
        return this
    }

    /**
     * 横线
     */
    fun setHorizontalLine(viewId: Int): ViewHolder {
        // 中间加横线 ， 添加Paint.ANTI_ALIAS_FLAG是线会变得清晰去掉锯齿
        getView<TextView>(viewId).paint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
        return this
    }

    /**
     * 下滑线
     */
    fun setGlidinglLine(viewId: Int): ViewHolder {
        // 中间加横线 ， 添加Paint.ANTI_ALIAS_FLAG是线会变得清晰去掉锯齿
        getView<TextView>(viewId).paint.flags = Paint.UNDERLINE_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
        return this
    }

    /**
     * 字体
     */
    fun setTypeface(typeface: Typeface?, vararg viewIds: Int): ViewHolder {
        for (viewId in viewIds) {
            val view: TextView = getView(viewId)
            view.typeface = typeface
            view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        }
        return this
    }

    fun setProgress(viewId: Int, progress: Int): ViewHolder {
        getView<ProgressBar>(viewId).progress = progress
        return this
    }

    fun setProgress(viewId: Int, progress: Int, max: Int): ViewHolder {
        val view: ProgressBar = getView(viewId)
        view.max = max
        view.progress = progress
        return this
    }

    fun setMax(viewId: Int, max: Int): ViewHolder {
        getView<ProgressBar>(viewId).max = max
        return this
    }

    fun setRating(viewId: Int, rating: Float): ViewHolder {
        getView<RatingBar>(viewId).rating = rating
        return this
    }

    fun setRating(viewId: Int, rating: Float, max: Int): ViewHolder {
        val view: RatingBar = getView(viewId)
        view.max = max
        view.rating = rating
        return this
    }

    fun setTag(viewId: Int, tag: Any?): ViewHolder {
        getView<View>(viewId).tag = tag
        return this
    }

    fun setTag(viewId: Int, key: Int, tag: Any?): ViewHolder {
        getView<View>(viewId).setTag(key, tag)
        return this
    }

    fun setChecked(viewId: Int, checked: Boolean): ViewHolder {
        val view = getView<View>(viewId) as Checkable
        view.isChecked = checked
        return this
    }

    fun setEnabled(viewId: Int, isEnabled: Boolean): ViewHolder {
        getView<View>(viewId).isEnabled = isEnabled
        return this
    }

    /**
     * 关于事件的
     */
    fun setOnClickListener(viewId: Int,
                           listener: View.OnClickListener?): ViewHolder {
        val view: View = getView(viewId)
        view.setOnClickListener(listener)
        return this
    }

    fun setOnTouchListener(viewId: Int,
                           listener: OnTouchListener?): ViewHolder {
        val view: View = getView(viewId)
        view.setOnTouchListener(listener)
        return this
    }

    fun setOnLongClickListener(viewId: Int,
                               listener: OnLongClickListener?): ViewHolder {
        val view: View = getView(viewId)
        view.setOnLongClickListener(listener)
        return this
    }


}