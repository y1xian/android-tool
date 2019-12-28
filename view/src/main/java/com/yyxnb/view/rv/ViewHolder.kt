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
        var view: View? = mViews.get(viewId)
        if (view == null) {
            view = convertView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T
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


    /****以下为辅助方法 */

    fun setText(viewId: Int, text: CharSequence?): ViewHolder {
        val tv = getView<TextView>(viewId)
        tv.text = text
        return this
    }

    fun setText(viewId: Int, text: String?): ViewHolder? {
        val tv: TextView = getView(viewId)
        tv.text = text
        return this
    }

    fun setImageResource(viewId: Int, resId: Int): ViewHolder {
        val view = getView<ImageView>(viewId)
        view.setImageResource(resId)
        return this
    }

    fun setImageBitmap(viewId: Int, bitmap: Bitmap?): ViewHolder? {
        val view: ImageView = getView(viewId)
        view.setImageBitmap(bitmap)
        return this
    }

    fun setImageDrawable(viewId: Int, drawable: Drawable?): ViewHolder? {
        val view: ImageView = getView(viewId)
        view.setImageDrawable(drawable)
        return this
    }

    fun setBackgroundColor(viewId: Int, color: Int): ViewHolder? {
        val view: View = getView(viewId)
        view.setBackgroundColor(color)
        return this
    }

    fun setBackgroundRes(viewId: Int, backgroundRes: Int): ViewHolder? {
        val view: View = getView(viewId)
        view.setBackgroundResource(backgroundRes)
        return this
    }

    fun setTextColor(viewId: Int, textColor: Int): ViewHolder? {
        val view: TextView = getView(viewId)
        view.setTextColor(textColor)
        return this
    }


    @SuppressLint("NewApi", "ObsoleteSdkInt")
    fun setAlpha(viewId: Int, value: Float): ViewHolder? {
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

    fun setVisible(viewId: Int, visible: Boolean): ViewHolder? {
        val view: View = getView(viewId)
        view.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    fun linkify(viewId: Int): ViewHolder? {
        val view: TextView = getView(viewId)
        Linkify.addLinks(view, Linkify.ALL)
        return this
    }

    fun setTypeface(typeface: Typeface?, vararg viewIds: Int): ViewHolder? {
        for (viewId in viewIds) {
            val view: TextView = getView(viewId)
            view.typeface = typeface
            view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        }
        return this
    }

    fun setProgress(viewId: Int, progress: Int): ViewHolder? {
        val view: ProgressBar = getView(viewId)
        view.setProgress(progress)
        return this
    }

    fun setProgress(viewId: Int, progress: Int, max: Int): ViewHolder? {
        val view: ProgressBar = getView(viewId)
        view.setMax(max)
        view.setProgress(progress)
        return this
    }

    fun setMax(viewId: Int, max: Int): ViewHolder? {
        val view: ProgressBar = getView(viewId)
        view.setMax(max)
        return this
    }

    fun setRating(viewId: Int, rating: Float): ViewHolder? {
        val view: RatingBar = getView(viewId)
        view.rating = rating
        return this
    }

    fun setRating(viewId: Int, rating: Float, max: Int): ViewHolder? {
        val view: RatingBar = getView(viewId)
        view.max = max
        view.rating = rating
        return this
    }

    fun setTag(viewId: Int, tag: Any?): ViewHolder? {
        val view: View = getView(viewId)
        view.tag = tag
        return this
    }

    fun setTag(viewId: Int, key: Int, tag: Any?): ViewHolder? {
        val view: View = getView(viewId)
        view.setTag(key, tag)
        return this
    }

    fun setChecked(viewId: Int, checked: Boolean): ViewHolder? {
        val view = getView<View>(viewId) as Checkable
        view.isChecked = checked
        return this
    }

    /**
     * 关于事件的
     */
    fun setOnClickListener(viewId: Int,
                           listener: View.OnClickListener?): ViewHolder? {
        val view: View = getView(viewId)
        view.setOnClickListener(listener)
        return this
    }

    fun setOnTouchListener(viewId: Int,
                           listener: OnTouchListener?): ViewHolder? {
        val view: View = getView(viewId)
        view.setOnTouchListener(listener)
        return this
    }

    fun setOnLongClickListener(viewId: Int,
                               listener: OnLongClickListener?): ViewHolder? {
        val view: View = getView(viewId)
        view.setOnLongClickListener(listener)
        return this
    }


}