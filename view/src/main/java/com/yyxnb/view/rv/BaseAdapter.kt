package com.yyxnb.view.rv

abstract class BaseAdapter<T>(
        //布局
        protected var mLayoutId: Int
) : MultiItemTypeAdapter<T>() {

    init {
        addItemDelegate(object : ItemDelegate<T> {
            override val layoutId: Int
                get() = mLayoutId

            override fun isThisType(item: T, position: Int): Boolean {
                return true
            }

            override fun bind(holder: ViewHolder, t: T, position: Int) {
                this@BaseAdapter.bind(holder, t, position)
            }
        })
    }

    protected abstract fun bind(holder: ViewHolder, t: T, position: Int)

}