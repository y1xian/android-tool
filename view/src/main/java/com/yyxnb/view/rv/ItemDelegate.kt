package com.yyxnb.view.rv

interface ItemDelegate<T> {

    val layoutId: Int

    fun isThisType(item: T, position: Int): Boolean

    fun bind(holder: ViewHolder, t: T, position: Int)

}