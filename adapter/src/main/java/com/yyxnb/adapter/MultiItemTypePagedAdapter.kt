package com.yyxnb.adapter

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


open class MultiItemTypePagedAdapter<T> constructor(diffCallback: DiffUtil.ItemCallback<T>) : PagedListAdapter<T, BaseViewHolder>(diffCallback) {

    /**
     * 多类型管理
     */
    protected var mItemDelegateManager: ItemDelegateManager<T> = ItemDelegateManager()
    private var mOnItemClickListener: OnItemClickListener? = null

    private val mHeaders = SparseArray<View>()
    private val mFooters = SparseArray<View>()

    private var BASE_ITEM_TYPE_HEADER = 100000
    private var BASE_ITEM_TYPE_FOOTER = 200000

    fun addHeaderView(view: View) { //判断给View对象是否还没有处在mHeaders数组里面
        if (mHeaders.indexOfValue(view) < 0) {
            mHeaders.put(BASE_ITEM_TYPE_HEADER++, view)
            notifyDataSetChanged()
        }
    }

    fun addFooterView(view: View) { //判断给View对象是否还没有处在mFooters数组里面
        if (mFooters.indexOfValue(view) < 0) {
            mFooters.put(BASE_ITEM_TYPE_FOOTER++, view)
            notifyDataSetChanged()
        }
    }

    // 移除头部
    fun removeHeaderView(view: View) {
        val index = mHeaders.indexOfValue(view)
        if (index < 0) {
            return
        }
        mHeaders.removeAt(index)
        notifyDataSetChanged()
    }

    // 移除底部
    fun removeFooterView(view: View) {
        val index = mFooters.indexOfValue(view)
        if (index < 0) {
            return
        }
        mFooters.removeAt(index)
        notifyDataSetChanged()
    }

    private fun isFooterPosition(position: Int): Boolean {
        return position >= originalItemCount + mHeaders.size()
    }

    private fun isHeaderPosition(position: Int): Boolean {
        return position < mHeaders.size()
    }

    val headerCount: Int
        get() = mHeaders.size()

    val footerCount: Int
        get() = mFooters.size()

    val originalItemCount: Int
        get() = itemCount - headerCount - footerCount

    fun getData() = currentList

    fun getItemData(position: Int): T? {
        return getItem(position)
    }

    override fun getItemViewType(position: Int): Int {
        @Suppress("NAME_SHADOWING") var position = position
        return when {
            isHeaderPosition(position) -> { //返回该position对应的headerview的  viewType
                mHeaders.keyAt(position)
            }
            isFooterPosition(position) -> { //footer类型的，需要计算一下它的position实际大小
                position = position - originalItemCount - headerCount
                mFooters.keyAt(position)
            }
            else -> {
                position -= headerCount
                if (!useItemDelegateManager()) super.getItemViewType(position) else getItemData(position)?.let { mItemDelegateManager.getItemViewType(it, position) }!!
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        when {
            mHeaders.indexOfKey(viewType) >= 0 -> {
                val view = mHeaders[viewType]
                return BaseViewHolder.createViewHolder(view)
            }
            mFooters.indexOfKey(viewType) >= 0 -> {
                val view = mFooters[viewType]
                return BaseViewHolder.createViewHolder(view)
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
        mItemDelegateManager.convert(holder, t, holder.adapterPosition)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        @Suppress("NAME_SHADOWING") var position = position
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            return
        }
        //列表中正常类型的itemView的 position 咱们需要减去添加headerView的个数
        position -= headerCount
        getItemData(position)?.let { convert(holder, it) }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + headerCount + footerCount
    }

    protected fun setListener(holder: BaseViewHolder) {

        mOnItemClickListener?.let {
            holder.itemView.setOnClickListener { v ->
                var position = holder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                position -= headerCount
                it.onItemClick(v, holder, position)
            }
        }

        mOnItemClickListener?.let {
            holder.itemView.setOnLongClickListener { v ->
                var position = holder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnLongClickListener false
                }
                position -= headerCount
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
                        position -= headerCount
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
                        position -= headerCount
                        it.onItemChildLongClick(v, holder, position)
                    }
                }
            }
        }
    }

    fun addItemDelegate(itemViewDelegate: ItemDelegate<T>): MultiItemTypePagedAdapter<T> {
        mItemDelegateManager.addDelegate(itemViewDelegate)
        return this
    }

    fun addItemDelegate(viewType: Int, itemViewDelegate: ItemDelegate<T>): MultiItemTypePagedAdapter<T> {
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
        GlobalScope.launch {
            val oldData = getData()!!
            val dataSource = MutablePageKeyedDataSource<T>()
            dataSource.data.addAll(list)
            val pagedList = dataSource.buildNewPagedList(oldData.config)
            submitList(pagedList)
        }
    }

    /**
     * 添加一条新数据
     */
    fun addDataItem(t: T) {
        addDataItem(getData()?.size!!, t)
    }

    /**
     * 在指定位置添加一条新数据
     */
    fun addDataItem(position: Int, t: T) {
        GlobalScope.launch {
            val oldData = getData()!!
            val dataSource = MutablePageKeyedDataSource<T>()
            if (position == oldData.size) {
                dataSource.data.addAll(oldData)
                dataSource.data.add(t)
            } else {
                oldData.forEachIndexed { i, it ->
                    dataSource.data.add(it)
                    if (i == position) {
                        dataSource.data.add(i, t)
                    }
                }
            }
            val pagedList = dataSource.buildNewPagedList(oldData.config)
            submitList(pagedList)
        }
    }

    /**
     * 添加数据集
     */
    fun addDataItem(list: List<T>) {
        addDataItem(getData()?.size!!, list)
    }

    /**
     * 在指定位置添加数据集
     */
    fun addDataItem(position: Int, list: List<T>) {
        GlobalScope.launch {
            val oldData = getData()!!
            val dataSource = MutablePageKeyedDataSource<T>()
            if (position == oldData.size) {
                dataSource.data.addAll(oldData)
                dataSource.data.addAll(list)
            } else {
                oldData.forEachIndexed { i, it ->
                    dataSource.data.add(it)
                    if (i == position) {
                        dataSource.data.addAll(i, list)
                    }
                }
            }
            val pagedList = dataSource.buildNewPagedList(oldData.config)
            submitList(pagedList)
        }
    }

    /**
     *  改变数据
     */
    open fun updateDataItem(position: Int, t: T) {
        GlobalScope.launch {
            val oldData = currentList!!
            val dataSource = MutablePageKeyedDataSource<T>()
            oldData.forEachIndexed { i, it ->
                if (i != position) {
                    dataSource.data.add(it)
                }
                if (i == position) {
                    dataSource.data.add(i, t)
                }
            }
            val pagedList = dataSource.buildNewPagedList(oldData.config)
            submitList(pagedList)
        }
    }

    /**
     * 删除指定位置的数据
     */
    fun removeDataItem(position: Int) {
        if (position == -1) {
            return
        }
        removeDataItem(getItem(position)!!)
    }

    fun removeDataItem(t: T) {
        GlobalScope.launch {
            val oldData = currentList!!
            val dataSource = MutablePageKeyedDataSource<T>()
            oldData.forEach {
                if (it != t) {
                    dataSource.data.add(it)
                }
            }
            val pagedList = dataSource.buildNewPagedList(oldData.config)
            submitList(pagedList)
        }
    }

    /**
     * 调换位置
     */
    fun changeDataItem(position: Int, t: T) {
        GlobalScope.launch {
            val oldData = currentList!!
            val dataSource = MutablePageKeyedDataSource<T>()
            oldData.forEachIndexed { i, it ->
                if (i == position) {
                    dataSource.data.add(i, t)
                }
                if (it != t) {
                    dataSource.data.add(it)
                }
            }
            val pagedList = dataSource.buildNewPagedList(oldData.config)
            submitList(pagedList)
        }
    }

    fun clearData() {
        GlobalScope.launch {
            val oldData = currentList!!
            val dataSource = MutablePageKeyedDataSource<T>()
            val pagedList = dataSource.buildNewPagedList(oldData.config)
            submitList(pagedList)
        }
    }

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        super.registerAdapterDataObserver(AdapterDataObserverProxy(observer))
    }

    //如果我们先添加了headerView,而后网络数据回来了再更新到列表上
    //由于Paging在计算列表上item的位置时 并不会顾及我们有没有添加headerView，就会出现列表定位的问题
    //实际上 RecyclerView#setAdapter方法，它会给Adapter注册了一个AdapterDataObserver
    //咱么可以代理registerAdapterDataObserver()传递进来的observer。在各个方法的实现中，把headerView的个数算上，再中转出去即可
    private inner class AdapterDataObserverProxy(private val mObserver: RecyclerView.AdapterDataObserver) : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            mObserver.onChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            mObserver.onItemRangeChanged(positionStart + headerCount, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            mObserver.onItemRangeChanged(positionStart + headerCount, itemCount, payload)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            mObserver.onItemRangeInserted(positionStart + headerCount, itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            mObserver.onItemRangeRemoved(positionStart + headerCount, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            mObserver.onItemRangeMoved(fromPosition + headerCount, toPosition + headerCount, itemCount)
        }

    }

}