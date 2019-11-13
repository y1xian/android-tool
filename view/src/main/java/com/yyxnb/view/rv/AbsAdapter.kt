package com.yyxnb.view.rv

abstract class AbsAdapter<T>(data: MutableList<T>, protected var mLayoutId: Int) : MultiItemTypeAdapter<T>(data) {

    init {
        addItemDelegate(object : ItemDelegate<T> {
            override val layoutId: Int
                get() = mLayoutId

            override fun isThisType(item: T, position: Int): Boolean {
                return true
            }

            override fun bind(holder: ViewHolder, t: T, position: Int) {
                this@AbsAdapter.bind(holder, t, position)
            }
        })
    }

    protected abstract fun bind(holder: ViewHolder, t: T, position: Int)

}