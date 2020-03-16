package com.yyxnb.adapter.ext

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.*
import android.view.View
import com.yyxnb.adapter.*
import com.yyxnb.adapter.rv.BaseRecyclerView

@JvmOverloads
fun RecyclerView.wrapLinear(
        context: Context,
        decoration: ItemDecoration = ItemDecoration(context)
): RecyclerView {
    apply {
        layoutManager = LinearLayoutManager(context)
        setHasFixedSize(true)
        addItemDecoration(decoration)
    }
    return this
}

fun RecyclerView.wrapLinear(
        context: Context,
        decoration: ItemDecoration.() -> ItemDecoration
): RecyclerView {
    apply {
        layoutManager = LinearLayoutManager(context)
        setHasFixedSize(true)
        addItemDecoration(ItemDecoration(context).let(decoration))
    }
    return this
}

@JvmOverloads
fun RecyclerView.wrapGrid(
        context: Context,
        spanCount: Int = 2,
        decoration: ItemDecoration = ItemDecoration(context)
): RecyclerView {
    apply {
        layoutManager = GridLayoutManager(context, spanCount)
        setHasFixedSize(true)
        addItemDecoration(decoration)
    }
    return this
}

fun RecyclerView.wrapGrid(
        context: Context,
        spanCount: Int = 2,
        decoration: ItemDecoration.() -> ItemDecoration
): RecyclerView {
    apply {
        layoutManager = GridLayoutManager(context, spanCount)
        setHasFixedSize(true)
        addItemDecoration(ItemDecoration(context).let(decoration))
    }
    return this
}

/**
 * 处理上拉下拉状态
 *  true  第一页（为了确认是否下拉或第一次赋值）
 */
@JvmOverloads
fun BaseRecyclerView.wrapData(
        page: Int,
        data: List<Any>,
        pageSize: Int = 10
) {
    val size = data.size
    when (size) {
        in 1..pageSize -> {
            if (page == 1) {
                //完成刷新
                isRefreshing = false
            } else if (size == pageSize) {
                //完成加载,还有更多数据
                loadMoreComplete()
            }
            //没有更多数据
            if (size < pageSize) {
                loadMoreEnd()
            }
        }
        0 -> {
            //没有更多数据
            if (page == 1) {
                //完成刷新并标记
                isRefreshing = false
            }
            loadMoreEnd()
        }
    }
    if (page == 1) {
        setDataItems(data)
    } else {
        addDataItem(data)
    }
}

fun RecyclerView.divider(color: Int = Color.parseColor("#DEDEDE"), size: Int = 1): RecyclerView {
    val decoration = ItemDecoration(context).apply {
        setDividerColor(color)
        setDividerHeight(size)
    }
    addItemDecoration(decoration)
    return this
}

fun RecyclerView.vertical(spanCount: Int = 0, isStaggered: Boolean = false): RecyclerView {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    if (spanCount != 0) {
        layoutManager = GridLayoutManager(context, spanCount)
    }
    if (isStaggered) {
        layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
    }
    return this
}

fun RecyclerView.horizontal(spanCount: Int = 0, isStaggered: Boolean = false): RecyclerView {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    if (spanCount != 0) {
        layoutManager = GridLayoutManager(context, spanCount, GridLayoutManager.HORIZONTAL, false)
    }
    if (isStaggered) {
        layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.HORIZONTAL)
    }
    return this
}


inline val RecyclerView.data
    get() = (adapter as BaseAdapter<*>).getData()

inline val RecyclerView.orientation
    get() = if (layoutManager == null) -1 else layoutManager.run {
        when (this) {
            is LinearLayoutManager -> orientation
            is GridLayoutManager -> orientation
            is StaggeredGridLayoutManager -> orientation
            else -> -1
        }
    }


fun <T> RecyclerView.bindData(layoutId: Int, bindFn: (holderBase: BaseViewHolder, t: T, position: Int) -> Unit): RecyclerView {
    adapter = object : BaseAdapter<T>(layoutId) {
        override fun bind(holder: BaseViewHolder, t: T, position: Int) {
            bindFn(holder, t, position)
        }
    }
    return this
}

fun RecyclerView.addHeader(headerView: View): RecyclerView {
    adapter?.apply {
        (this as BaseAdapter<*>).addHeaderView(headerView)
    }
    return this
}

fun RecyclerView.addFooter(footerView: View): RecyclerView {
    adapter?.apply {
        (this as BaseAdapter<*>).addFooterView(footerView)
    }
    return this
}

fun <T> RecyclerView.multiTypes(itemDelegates: List<ItemDelegate<T>>): RecyclerView {
    adapter = MultiItemTypeAdapter<T>().apply {
        itemDelegates.forEach { addItemDelegate(it) }
    }
    return this
}
