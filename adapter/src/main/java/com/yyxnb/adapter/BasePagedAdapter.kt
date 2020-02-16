package com.yyxnb.adapter

import android.support.v7.util.DiffUtil

abstract class BasePagedAdapter<T> constructor(
        protected var mLayoutId: Int, diffCallback: DiffUtil.ItemCallback<T>) : MultiItemTypePagedAdapter<T>(diffCallback) {

    init {
        addItemDelegate(object : ItemDelegate<T> {
            override val layoutId: Int
                get() = mLayoutId

            override fun isThisType(item: T, position: Int): Boolean {
                return true
            }

            override fun bind(holder: BaseViewHolder, t: T, position: Int) {
                this@BasePagedAdapter.bind(holder, t, position)
            }
        })
    }

    protected abstract fun bind(holder: BaseViewHolder, t: T, position: Int)

}