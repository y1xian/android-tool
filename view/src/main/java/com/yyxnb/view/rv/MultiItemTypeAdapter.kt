package com.yyxnb.view.rv

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import com.yyxnb.view.R
import java.lang.ref.WeakReference
import java.util.*


open class MultiItemTypeAdapter<T> : RecyclerView.Adapter<ViewHolder>() {

    private var data: MutableList<T> = ArrayList()
    private val mHeaderViews = SparseArrayCompat<View>()
    private val mFootViews = SparseArrayCompat<View>()
    private val mEmptyViews = SparseArrayCompat<View>()

    lateinit var weakRecyclerView: WeakReference<RecyclerView>
    private var mEmptyLayout: FrameLayout? = null

    /** 是否使用空布局 */
    var isUseEmpty = true

    protected var mItemDelegateManager: ItemDelegateManager<T> = ItemDelegateManager()
    protected var mOnItemClickListener: OnItemClickListener? = null

    val dataCount: Int
        get() = data.size

    val headersCount: Int
        get() = mHeaderViews.size()

    val footersCount: Int
        get() = mFootViews.size()

    val emptyCount: Int
        get() = mEmptyViews.size()

    fun getData() = data

    fun getHeaderItems() = mHeaderViews

    fun getFooterItems() = mFootViews

    fun getEmptyItems() = mEmptyViews

    /**
     * 用于保存需要设置点击事件的 item
     */
    private val childClickViewIds = LinkedHashSet<Int>()

    fun getChildClickViewIds(): LinkedHashSet<Int> {
        return childClickViewIds
    }

    /**
     * 设置需要点击事件的子view
     */
    fun addChildClickViewIds(vararg viewIds: Int) {
        for (viewId in viewIds) {
            childClickViewIds.add(viewId)
        }
    }

    /**
     * 用于保存需要设置长按点击事件的 item
     */
    private val childLongClickViewIds = LinkedHashSet<Int>()

    fun getChildLongClickViewIds(): LinkedHashSet<Int> {
        return childLongClickViewIds
    }

    /**
     * 设置需要长按点击事件的子view
     */
    fun addChildLongClickViewIds(vararg viewIds: Int) {
        for (viewId in viewIds) {
            childLongClickViewIds.add(viewId)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (hasEmptyView()) {
            return mEmptyViews.keyAt(position)
        } else if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position)
        } else if (isFooterViewPos(position)) {
            return mFootViews.keyAt(position - headersCount - dataCount)
        }
        return if (!useItemDelegateManager()) super.getItemViewType(position) else mItemDelegateManager.getItemViewType(data[position - headersCount], position - headersCount)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (mHeaderViews.get(viewType) != null) {
            return ViewHolder.createViewHolder(mHeaderViews.get(viewType)!!)
        } else if (mFootViews.get(viewType) != null) {
            return ViewHolder.createViewHolder(mFootViews.get(viewType)!!)
        } else if (mEmptyViews.get(viewType) != null) {
            return ViewHolder.createViewHolder(mEmptyViews.get(viewType)!!)
        }
        val itemViewDelegate = mItemDelegateManager.getItemViewDelegate(viewType)

        val layoutId = itemViewDelegate.layoutId
        val holder = ViewHolder.createViewHolder(parent.context, parent, layoutId)
        onViewHolderCreated(holder, holder.convertView)
        setListener(parent, holder, viewType)
        return holder
    }

    fun onViewHolderCreated(holder: ViewHolder, itemView: View) {}

    fun convert(holder: ViewHolder, t: T) {
        mItemDelegateManager.convert(holder, t, holder.adapterPosition - headersCount)
    }

    protected fun isEnabled(viewType: Int): Boolean {
        return true
    }

    fun setDataItems(list: List<T>?) {
        if (list != null) {
            data.clear()
            data.addAll(list)
            notifyDataSetChanged()
        }
    }

    fun addDataItem(t: T) {
        data.add(t)
        notifyItemInserted(data.size + headersCount)
    }

    fun addDataItem(position: Int = 0, t: T) {
        data.add(position, t)
        notifyItemInserted(headersCount + position)
    }

    fun addDataItem(position: Int = data.size, list: List<T>?) {
        if (list != null) {
            data.addAll(list)
            notifyItemRangeInserted(headersCount + position, list.size)
        }
    }

    fun addDataItem(list: List<T>?) {
        if (list != null) {
            data.addAll(list)
            notifyItemRangeInserted(data.size - list.size + headersCount, list.size)
        }
    }

    /**
     * 改变数据
     */
    open fun updateDataItem(index: Int, t: T) {
        if (data.isNotEmpty()) {
            data[index] = t
            notifyItemChanged(index + headersCount)
        }
    }


    @JvmOverloads
    fun removeDataItem(position: Int, itemCount: Int = 1) {
        for (i in 0 until itemCount) {
            data.removeAt(position)
        }
        notifyItemRangeRemoved(headersCount + position, itemCount)
    }

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }

    fun clearHeader() {
        mHeaderViews.clear()
        notifyDataSetChanged()
    }

    fun clearFooter() {
        mFootViews.clear()
        notifyDataSetChanged()
    }

    fun clearAllData() {
        clearData()
        clearHeader()
        clearFooter()
        notifyDataSetChanged()
    }

    protected fun setListener(parent: ViewGroup, viewHolder: ViewHolder, viewType: Int) {
        if (!isEnabled(viewType)) return

        mOnItemClickListener?.let {
            viewHolder.convertView.setOnClickListener { v ->
                var position = viewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                position -= headersCount
                mOnItemClickListener!!.onItemClick(v, viewHolder, position)
            }
        }

        mOnItemClickListener?.let {
            viewHolder.convertView.setOnLongClickListener { v ->
                var position = viewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnLongClickListener false
                }
                position -= headersCount
                mOnItemClickListener!!.onItemLongClick(v, viewHolder, position)
            }
        }

        mOnItemClickListener?.let {
            for (id in getChildClickViewIds()) {
                viewHolder.convertView.findViewById<View>(id)?.let { childView ->
                    if (!childView.isClickable) {
                        childView.isClickable = true
                    }
                    childView.setOnClickListener { v ->
                        var position = viewHolder.adapterPosition
                        if (position == RecyclerView.NO_POSITION) {
                            return@setOnClickListener
                        }
                        position -= headersCount
                        mOnItemClickListener!!.onItemChildClick(this, v, position)
                    }
                }
            }
        }

        mOnItemClickListener?.let {
            for (id in getChildLongClickViewIds()) {
                viewHolder.convertView.findViewById<View>(id)?.let { childView ->
                    if (!childView.isLongClickable) {
                        childView.isLongClickable = true
                    }
                    childView.setOnLongClickListener { v ->
                        var position = viewHolder.adapterPosition - headersCount
                        if (position == RecyclerView.NO_POSITION) {
                            return@setOnLongClickListener false
                        }
                        position -= headersCount
                        mOnItemClickListener!!.onItemChildLongClick(this, v, position)
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (isHeaderViewPos(position)) {
            return
        }
        if (isFooterViewPos(position)) {
            return
        }
        convert(holder, data[position - headersCount])
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        weakRecyclerView = WeakReference(recyclerView)
        setEmptyView(R.layout._loading_layout_empty)
        WrapperUtils.onAttachedToRecyclerView(
                recyclerView
        ) { layoutManager, oldLookup, position ->
            val viewType = getItemViewType(position)
            when {
                mHeaderViews.get(viewType) != null -> layoutManager.spanCount
                mFootViews.get(viewType) != null -> layoutManager.spanCount
                mEmptyViews.get(viewType) != null -> layoutManager.spanCount
                else -> oldLookup.getSpanSize(position)
            }
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.layoutPosition
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            WrapperUtils.setFullSpan(holder)
        }
    }

    override fun getItemCount(): Int {
        val itemCount = data.size
        if (hasEmptyView()) {
            return emptyCount
        }
        return headersCount + footersCount + itemCount
    }

    private fun isHeaderViewPos(position: Int): Boolean {
        return position < headersCount
    }

    private fun isFooterViewPos(position: Int): Boolean {
        return position >= headersCount + dataCount
    }

    fun addHeaderView(view: View) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view)
        notifyItemInserted(headersCount)
    }

    fun addFootView(view: View) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view)
        notifyItemInserted(headersCount + dataCount + footersCount)
    }

    fun addItemDelegate(itemViewDelegate: ItemDelegate<T>): MultiItemTypeAdapter<T> {
        mItemDelegateManager.addDelegate(itemViewDelegate)
        return this
    }

    fun addItemDelegate(viewType: Int, itemViewDelegate: ItemDelegate<T>): MultiItemTypeAdapter<T> {
        mItemDelegateManager.addDelegate(viewType, itemViewDelegate)
        return this
    }

    protected fun useItemDelegateManager(): Boolean {
        return mItemDelegateManager.itemViewDelegateCount > 0
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int)

        fun onItemLongClick(view: View, holder: RecyclerView.ViewHolder, position: Int): Boolean

        fun onItemChildClick(adapter: MultiItemTypeAdapter<*>?, view: View?, position: Int)

        fun onItemChildLongClick(adapter: MultiItemTypeAdapter<*>?, view: View?, position: Int): Boolean
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = onItemClickListener
    }

    open class SimpleOnItemClickListener : OnItemClickListener {

        override fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int) {}

        override fun onItemLongClick(view: View, holder: RecyclerView.ViewHolder, position: Int): Boolean {
            return false
        }

        override fun onItemChildClick(adapter: MultiItemTypeAdapter<*>?, view: View?, position: Int) {}

        override fun onItemChildLongClick(adapter: MultiItemTypeAdapter<*>?, view: View?, position: Int): Boolean {
            return false
        }
    }

    /**
     * 设置空布局视图，注意：[data]必须为空数组
     */
    fun setEmptyView(emptyView: View) {
        if (data.isEmpty()) {
            mEmptyLayout = FrameLayout(emptyView.context)
            mEmptyLayout?.apply {
                layoutParams = emptyView.layoutParams?.let {
                    return@let ViewGroup.LayoutParams(it.width, it.height)
                } ?: ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                isUseEmpty = true
                removeAllViews()
                addView(emptyView)
                mEmptyViews.put(BASE_ITEM_TYPE_EMPTY, this)
            }
        }
    }

    fun setEmptyView(layoutResId: Int) {
        weakRecyclerView.get()?.let {
            val view = LayoutInflater.from(it.context).inflate(layoutResId, it, false)
            setEmptyView(view)
        }
    }

    fun removeEmptyView() {
        isUseEmpty = false
        mEmptyViews.clear()
    }

    fun hasEmptyView(): Boolean {
        if (emptyCount == 0 || headersCount != 0 || footersCount != 0) {
            return false
        }
        if (!isUseEmpty) {
            return false
        }
        return data.isEmpty()
    }

    companion object {
        private const val BASE_ITEM_TYPE_HEADER = 100000
        private const val BASE_ITEM_TYPE_FOOTER = 200000
        private const val BASE_ITEM_TYPE_EMPTY = 300000
    }

}