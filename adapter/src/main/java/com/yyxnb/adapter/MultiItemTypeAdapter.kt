package com.yyxnb.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.util.ListUpdateCallback
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.yyxnb.adapter.rv.BaseRecyclerView
import com.yyxnb.adapter.rv.BaseState
import java.lang.ref.WeakReference


open class MultiItemTypeAdapter<T> : RecyclerView.Adapter<BaseViewHolder>() {

    private var data: MutableList<T> = arrayListOf()

    private var weakRecyclerView: WeakReference<BaseRecyclerView>? = null
    private var mEmptyLayout: FrameLayout? = null

    /** 是否使用空布局 */
    var isDefaultEmpty = true

    /**
     * 多类型管理
     */
    protected var mItemDelegateManager: ItemDelegateManager<T> = ItemDelegateManager()
    private var mOnItemClickListener: OnItemClickListener? = null

    val dataCount: Int
        get() = data.size

    val headersCount: Int
        get() = getCustomTopItemViewCount

    val footersCount: Int
        get() = weakRecyclerView?.get()?.footerViewSize ?: 0

    /**
     * list列表数据头部的view数量：RefreshView + HeaderView + EmptyView
     */
    val getCustomTopItemViewCount: Int
        get() = weakRecyclerView?.get()?.customTopItemViewCount ?: 0

    fun getData() = data

    fun getItemData(position: Int): T {
        return getData()[position]
    }

    fun setRecyclerView(recyclerView: BaseRecyclerView) {
        weakRecyclerView = WeakReference(recyclerView)
    }

    override fun getItemViewType(position: Int): Int {
        return if (!useItemDelegateManager()) super.getItemViewType(position) else mItemDelegateManager.getItemViewType(data[position], position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemViewDelegate = mItemDelegateManager.getItemViewDelegate(viewType)
        val layoutId = itemViewDelegate.layoutId
        val holder = BaseViewHolder.createViewHolder(parent.context.applicationContext, parent, layoutId)
        onViewHolderCreated(holder, holder.convertView)
        setListener(holder)
        return holder
    }

    fun onViewHolderCreated(holder: BaseViewHolder, itemView: View) {}

    fun convert(holder: BaseViewHolder, t: T) {
        mItemDelegateManager.convert(holder, t, holder.adapterPosition)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        convert(holder, data[position])
    }

    override fun getItemCount(): Int {
        return dataCount
    }

    protected fun setListener(holder: BaseViewHolder) {

        mOnItemClickListener?.let {
            holder.itemView.setOnClickListener { v ->
                var position = holder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                position -= headersCount
                it.onItemClick(v, holder, position)
            }
        }

        mOnItemClickListener?.let {
            holder.itemView.setOnLongClickListener { v ->
                var position = holder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnLongClickListener false
                }
                position -= headersCount
                it.onItemLongClick(v, holder, position)
            }
        }

    }

    /**
     * 设置需要点击事件的子view
     */
    fun addChildClickViewIds(holder: BaseViewHolder, vararg viewIds: Int) {
        mOnItemClickListener?.let {
            for (id in viewIds) {
                holder.itemView.findViewById<View>(id)?.let { childView ->
                    if (!childView.isClickable) {
                        childView.isClickable = true
                    }
                    childView.setOnClickListener { v ->
                        var position = holder.adapterPosition
                        if (position == RecyclerView.NO_POSITION) {
                            return@setOnClickListener
                        }
                        position -= headersCount
                        it.onItemChildClick(v, holder, position)
                    }
                }
            }
        }

    }

    /**
     * 设置需要长按点击事件的子view
     */
    fun addChildLongClickViewIds(holder: BaseViewHolder, vararg viewIds: Int) {
        mOnItemClickListener?.let {
            for (id in viewIds) {
                holder.itemView.findViewById<View>(id)?.let { childView ->
                    if (!childView.isLongClickable) {
                        childView.isLongClickable = true
                    }
                    childView.setOnLongClickListener { v ->
                        var position = holder.adapterPosition
                        if (position == RecyclerView.NO_POSITION) {
                            return@setOnLongClickListener false
                        }
                        position -= headersCount
                        it.onItemChildLongClick(v, holder, position)
                    }
                }
            }
        }
    }

    private fun isHeaderViewPos(position: Int): Boolean {
        return position < headersCount
    }

    private fun isFooterViewPos(position: Int): Boolean {
        return position >= headersCount + dataCount
    }

    fun addHeaderView(view: View) {
        weakRecyclerView?.get()?.apply {
            addHeaderView(view)
        }
    }

    fun addFooterView(view: View) {
        weakRecyclerView?.get()?.apply {
            addFooterView(view)
        }
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
        fun onItemClick(view: View, holder: BaseViewHolder, position: Int)

        fun onItemLongClick(view: View, holder: BaseViewHolder, position: Int): Boolean

        fun onItemChildClick(view: View, holder: BaseViewHolder, position: Int)

        fun onItemChildLongClick(view: View, holder: BaseViewHolder, position: Int): Boolean
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = onItemClickListener
    }

    open class SimpleOnItemClickListener : OnItemClickListener {

        override fun onItemClick(view: View, holder: BaseViewHolder, position: Int) {}

        override fun onItemLongClick(view: View, holder: BaseViewHolder, position: Int): Boolean {
            return false
        }

        override fun onItemChildClick(view: View, holder: BaseViewHolder, position: Int) {}

        override fun onItemChildLongClick(view: View, holder: BaseViewHolder, position: Int): Boolean {
            return false
        }
    }

    /**
     * 新数据
     */
    fun setDataItems(list: List<T> = arrayListOf()) {
        data.clear()
        data.addAll(list)
        compatibilityDataSizeChanged(list.size)
    }

    /**
     * 添加一条新数据
     */
    fun addDataItem(t: T) {
        data.add(t)
        notifyItemInserted(dataCount + headersCount)
        compatibilityDataSizeChanged(1)
    }

    /**
     * 在指定位置添加一条新数据
     */
    fun addDataItem(position: Int, t: T) {
        data.add(position, t)
        notifyItemInserted(headersCount + position)
        compatibilityDataSizeChanged(1)
    }

    /**
     * 添加数据集
     */
    fun addDataItem(list: List<T>) {
        data.addAll(list)
        notifyItemRangeInserted(dataCount + headersCount, list.size)
        compatibilityDataSizeChanged(list.size)
    }

    /**
     * 在指定位置添加数据集
     */
    fun addDataItem(position: Int, list: List<T>) {
        data.addAll(position, list)
        notifyItemRangeInserted(headersCount + position, list.size)
        compatibilityDataSizeChanged(list.size)
    }

    /**
     *  改变数据
     */
    open fun updateDataItem(index: Int, t: T) {
        if (data.isNotEmpty()) {
            data[index] = t
            notifyItemChanged(index + headersCount)
        }
    }

    /**
     * 删除指定位置的数据
     */
    fun removeDataItem(position: Int) {
        if (position >= dataCount) return
        data.removeAt(position)
        val internalPosition = position + headersCount
        notifyItemRemoved(internalPosition)
        compatibilityDataSizeChanged(0)
        if (position != dataCount) {
            notifyItemRangeChanged(internalPosition, dataCount - internalPosition)
        }
    }

    fun removeDataItem(t: T) {
        val index = data.indexOf(t)
        if (index == -1) {
            return
        }
        removeDataItem(index)
    }

    /**
     * 调换位置
     */
    @JvmOverloads
    fun changeDataItem(position: Int, t: T, isScroll: Boolean = false) {
        removeDataItem(t)
        addDataItem(position, t)
        if (isScroll) {
            weakRecyclerView?.get()?.scrollToPosition(position)
        }
    }

    /**
     * 不是同一个引用才清空列表
     */
    fun replaceData(newData: List<T>) {
        if (newData != data) {
            data.clear()
            data.addAll(newData)
        }
        notifyDataSetChanged()
    }

    private fun compatibilityDataSizeChanged(size: Int) {
        isDefaultEmpty = false
        weakRecyclerView?.get()?.apply {
            if (dataCount > 0) {
                setStateType(BaseState.SUCCESS)
            }
        }
        if (dataCount == size) {
            notifyDataSetChanged()
        }
    }

    fun clearData() {
        notifyItemRangeRemoved(headersCount, dataCount)
        data.clear()
    }

    fun clearHeader() {
        weakRecyclerView?.get()?.apply {
            removeAllHeaderView()
        }
    }

    fun clearFooter() {
        weakRecyclerView?.get()?.apply {
            removeAllFooterView()
        }
    }

    /**
     *  DiffUtil
     */
    fun diffUtilData(newData: List<T>, detectMoves: Boolean = true) {
        //第一个参数是DiffUtil.Callback对象，
        //第二个参数代表是否检测Item的移动，改为false算法效率更高，按需设置，我们这里是true。
        val result = DiffUtil.calculateDiff(DiffCallBack(data, newData), detectMoves)
        // 这里的getData即表示获取整个列表的数据，自行实现即可
        data.clear()
        data.addAll(newData)
//        result.dispatchUpdatesTo(this)
        result.dispatchUpdatesTo(object : ListUpdateCallback {
            override fun onChanged(position: Int, count: Int, payload: Any?) {
                notifyItemRangeChanged(headersCount + position, count, payload)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                notifyItemMoved(headersCount + fromPosition, headersCount + toPosition)
            }

            override fun onInserted(position: Int, count: Int) {
                notifyItemRangeInserted(headersCount + position, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                notifyItemRangeRemoved(headersCount + position, count)
            }

        })
    }

}