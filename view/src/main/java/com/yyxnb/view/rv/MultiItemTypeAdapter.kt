package com.yyxnb.view.rv

import android.support.v4.util.SparseArrayCompat
import android.support.v7.util.DiffUtil
import android.support.v7.util.ListUpdateCallback
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import com.yyxnb.view.R
import java.lang.ref.WeakReference


open class MultiItemTypeAdapter<T> : RecyclerView.Adapter<BaseViewHolder>() {

    private var data: MutableList<T> = arrayListOf()
    private val mHeaderViews = SparseArrayCompat<View>()
    private val mFootViews = SparseArrayCompat<View>()
    private val mEmptyViews = SparseArrayCompat<View>()

    lateinit var weakRecyclerView: WeakReference<RecyclerView>
    private var mEmptyLayout: FrameLayout? = null

    /** 是否使用空布局 */
    var isUseEmpty = true
    var isDefaultEmpty = true

    /**
     * 多类型管理
     */
    protected var mItemDelegateManager: ItemDelegateManager<T> = ItemDelegateManager()
    lateinit var mOnItemClickListener: OnItemClickListener

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

    override fun getItemViewType(position: Int): Int {
        if (hasEmptyView()) {
            return mEmptyViews.keyAt(position)
        }
        isDefaultEmpty = false
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position)
        } else if (isFooterViewPos(position)) {
            return mFootViews.keyAt(position - headersCount - dataCount)
        }

        return if (!useItemDelegateManager()) super.getItemViewType(position) else mItemDelegateManager.getItemViewType(data[position - headersCount], position - headersCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        when {
            mEmptyViews.get(viewType) != null -> {
                return BaseViewHolder.createViewHolder(mEmptyViews.get(viewType)!!)
            }
            mHeaderViews.get(viewType) != null -> {
                return BaseViewHolder.createViewHolder(mHeaderViews.get(viewType)!!)
            }
            mFootViews.get(viewType) != null -> {
                return BaseViewHolder.createViewHolder(mFootViews.get(viewType)!!)
            }
            else -> {
                val itemViewDelegate = mItemDelegateManager.getItemViewDelegate(viewType)
                val layoutId = itemViewDelegate.layoutId
                val holder = BaseViewHolder.createViewHolder(parent.context.applicationContext, parent, layoutId)
                onViewHolderCreated(holder, holder.convertView)
                setListener(holder)
                return holder
            }
        }

    }

    fun onViewHolderCreated(holder: BaseViewHolder, itemView: View) {}

    fun convert(holder: BaseViewHolder, t: T) {
        mItemDelegateManager.convert(holder, t, holder.adapterPosition - headersCount)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (hasEmptyView() || isHeaderViewPos(position) || isFooterViewPos(position)) {
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

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.layoutPosition
        if (hasEmptyView() || isHeaderViewPos(position) || isFooterViewPos(position)) {
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


    protected fun setListener(holder: BaseViewHolder) {

        mOnItemClickListener.let {
            holder.itemView.setOnClickListener { v ->
                var position = holder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                position -= headersCount
                it.onItemClick(v, holder, position)
            }
        }

        mOnItemClickListener.let {
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
        mOnItemClickListener.let {
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
        mOnItemClickListener.let {
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
     * 设置空布局视图，注意：[data]必须为空数组
     */
    fun setEmptyView(emptyView: View) {
        if (data.isEmpty()) {
            mEmptyLayout = FrameLayout(emptyView.context.applicationContext)
            mEmptyLayout?.apply {
                layoutParams = emptyView.layoutParams?.let {
                    return@let ViewGroup.LayoutParams(it.width, it.height)
                } ?: ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                isUseEmpty = true
                removeAllViews()
                addView(emptyView)
                mEmptyViews.clear()
                mEmptyViews.put(BASE_ITEM_TYPE_EMPTY, this)
                notifyItemInserted(0)
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
        if (!isUseEmpty || isDefaultEmpty) {
            return false
        }
        return data.isEmpty()
    }

    companion object {
        private const val BASE_ITEM_TYPE_HEADER = 100000
        private const val BASE_ITEM_TYPE_FOOTER = 200000
        private const val BASE_ITEM_TYPE_EMPTY = 300000
    }

    /**
     * 新数据
     */
    fun setDataItems(list: List<T>?) {
        if (list != null) {
            data.clear()
            data.addAll(list)
            notifyDataSetChanged()
        }
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
        notifyItemRangeChanged(internalPosition, dataCount - internalPosition)
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
            weakRecyclerView.get()?.scrollToPosition(position)
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

    protected fun compatibilityDataSizeChanged(size: Int) {
        if (dataCount == size) {
            notifyDataSetChanged()
        }
    }

    fun clearData() {
        notifyItemRangeRemoved(headersCount, dataCount)
        data.clear()
    }

    fun clearHeader() {
        notifyItemRangeRemoved(0, headersCount)
        mHeaderViews.clear()
    }

    fun clearFooter() {
        notifyItemRangeRemoved(headersCount + dataCount, footersCount)
        mFootViews.clear()
    }

    fun clearAllData() {
        clearData()
        clearHeader()
        clearFooter()
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