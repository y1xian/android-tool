package com.yyxnb.widget.fragments


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.yyxnb.arch.base.BaseFragment
import com.yyxnb.adapter.ItemDecoration

import com.yyxnb.widget.R
import com.yyxnb.widget.adapter.StringListAdapter
import com.yyxnb.widget.config.DataConfig
import kotlinx.android.synthetic.main.fragment_behavior.*

/**
 * Behavior
 */
class BehaviorFragment : BaseFragment() {

    private val mAdapter by lazy { StringListAdapter() }

    override fun initLayoutResId(): Int = R.layout.fragment_behavior

    override fun initView(savedInstanceState: Bundle?) {
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        mRecyclerView.setHasFixedSize(true)
        val decoration = ItemDecoration(mContext)
        decoration.setDividerColor(resources.getColor(R.color.red))
        decoration.setPaddingLeft(16)
        mRecyclerView.addItemDecoration(decoration)
        mRecyclerView.adapter = mAdapter

    }

    override fun initViewData() {
        super.initViewData()
        mAdapter.setDataItems(DataConfig.data)
    }

}
