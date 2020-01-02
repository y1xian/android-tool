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


open class MultiItemTypeAdapter<T> : RecyclerView.Adapter<BaseViewHolder>() {

    private var data: MutableList<T> = ArrayList()
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
    val childClickViewIds = LinkedHashSet<Int>()

    /**
     * 设置需要点击事件的子view
     */
    fun addChildClickViewIds(vararg viewIds: Int) {
        childClickViewIds.addAll(viewIds.asList())
    }

    /**
     * 用于保存需要设置长按点击事件的 item
     */
    val childLongClickViewIds = LinkedHashSet<Int>()

    /**
     * 设置需要长按点击事件的子view
     */
    fun addChildLongClickViewIds(vararg viewIds: Int) {
        childLongClickViewIds.addAll(viewIds.asList())
    }

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
        if (mEmptyViews.get(viewType) != null) {
            return BaseViewHolder.createViewHolder(mEmptyViews.get(viewType)!!)
        } else if (mHeaderViews.get(viewType) != null) {
            return BaseViewHolder.createViewHolder(mHeaderViews.get(viewType)!!)
        } else if (mFootViews.get(viewType) != null) {
            return BaseViewHolder.createViewHolder(mFootViews.get(viewType)!!)
        }
        val itemViewDelegate = mItemDelegateManager.getItemViewDelegate(viewType)

        val layoutId = itemViewDelegate.layoutId
        val holder = BaseViewHolder.createViewHolder(parent.context.applicationContext, parent, layoutId)
        onViewHolderCreated(holder, holder.convertView)
        setListener(parent, holder, viewType)
        return holder
    }

    fun onViewHolderCreated(holderBase: BaseViewHolder, itemView: View) {}

    fun convert(holderBase: BaseViewHolder, t: T) {
        mItemDelegateManager.convert(holderBase, t, holderBase.adapterPosition - headersCount)
    }

    fun setDataItems(list: MutableList<T>?) {

        if (data == list) return

        data = list ?: arrayListOf()
        notifyDataSetChanged()
    }

    fun addDataItem(t: T) {
        data.add(t)
        notifyItemInserted(dataCount + headersCount)
        compatibilityDataSizeChanged(1)
    }

    fun addDataItem(position: Int = 0, t: T) {
        data.add(position, t)
        notifyItemInserted(headersCount + position)
        compatibilityDataSizeChanged(1)
    }

    fun addDataItem(list: List<T>) {
        data.addAll(list)
        notifyItemRangeInserted(headersCount + dataCount, list.size)
        compatibilityDataSizeChanged(list.size)
    }

    open fun updateDataItem(index: Int, t: T) {
        if (data.isNotEmpty()) {
            data[index] = t
            notifyItemChanged(index + headersCount)
        }
    }

    fun removeDataItem(position: Int) {
        if (position >= dataCount) return
        data.removeAt(position)
        val internalPosition = position + headersCount
        notifyItemRemoved(internalPosition)
        compatibilityDataSizeChanged(0)
        notifyItemRangeChanged(internalPosition, dataCount - internalPosition)
    }

    fun replaceData(newData: Collection<T>) {
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

    protected fun setListener(parent: ViewGroup, baseViewHolder: BaseViewHolder, viewType: Int) {

        mOnItemClickListener?.let {
            baseViewHolder.itemView.setOnClickListener { v ->
                var position = baseViewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                position -= headersCount
                mOnItemClickListener!!.onItemClick(v, baseViewHolder, position)
            }
        }

        mOnItemClickListener?.let {
            baseViewHolder.itemView.setOnLongClickListener { v ->
                var position = baseViewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnLongClickListener false
                }
                position -= headersCount
                mOnItemClickListener!!.onItemLongClick(v, baseViewHolder, position)
            }
        }

        mOnItemClickListener?.let {
            for (id in childClickViewIds) {
                baseViewHolder.itemView.findViewById<View>(id)?.let { childView ->
                    if (!childView.isClickable) {
                        childView.isClickable = true
                    }
                    childView.setOnClickListener { v ->
                        var position = baseViewHolder.adapterPosition
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
            for (id in childLongClickViewIds) {
                baseViewHolder.itemView.findViewById<View>(id)?.let { childView ->
                    if (!childView.isLongClickable) {
                        childView.isLongClickable = true
                    }
                    childView.setOnLongClickListener { v ->
                        var position = baseViewHolder.adapterPosition
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

    override fun onBindViewHolder(holderBase: BaseViewHolder, position: Int) {
        if (hasEmptyView() || isHeaderViewPos(position) || isFooterViewPos(position)) {
            return
        }
        convert(holderBase, data[position - headersCount])
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

    override fun onViewAttachedToWindow(holderBase: BaseViewHolder) {
        super.onViewAttachedToWindow(holderBase)
        val position = holderBase.layoutPosition
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            WrapperUtils.setFullSpan(holderBase)
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
        fun onItemClick(view: View, holder: BaseViewHolder, position: Int)

        fun onItemLongClick(view: View, holder: BaseViewHolder, position: Int): Boolean

        fun onItemChildClick(adapter: MultiItemTypeAdapter<*>?, view: View, position: Int)

        fun onItemChildLongClick(adapter: MultiItemTypeAdapter<*>?, view: View, position: Int): Boolean
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = onItemClickListener
    }

    open class SimpleOnItemClickListener : OnItemClickListener {

        override fun onItemClick(view: View, holder: BaseViewHolder, position: Int) {}

        override fun onItemLongClick(view: View, holder: BaseViewHolder, position: Int): Boolean {
            return false
        }

        override fun onItemChildClick(adapter: MultiItemTypeAdapter<*>?, view: View, position: Int) {}

        override fun onItemChildLongClick(adapter: MultiItemTypeAdapter<*>?, view: View, position: Int): Boolean {
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

}