package com.yyxnb.view.rv

import android.annotation.SuppressLint
import android.support.v7.util.DiffUtil

abstract class BaseAdapter<T>(
        //布局
        protected var mLayoutId: Int,
        //作用是判断两个item的数据是否相等

        diffCallback: DiffUtil.ItemCallback<T> /*= object : DiffUtil.ItemCallback<T>() {

            *//**
             * Called by the DiffUtil to decide whether two object represent the same Item.
             * 在DiffUtil检测两个对象是否表示相同的Item时被调用，True代表两个对象对应相同的Item
             *//*
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                return oldItem == newItem
            }

            *//**
             * Called by the DiffUtil when it wants to check whether two items have the same data.
             * 在DiffUtil想去检测两个Items是否有一样的数据时调用，，True表示一致，False表示不一致
             *//*
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                return oldItem == newItem
            }

        }*/) : MultiItemTypeAdapter<T>(diffCallback) {

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