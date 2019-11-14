package com.yyxnb.widget

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.yyxnb.arch.base.BaseActivity
import com.yyxnb.widget.adapter.MainListAdapter
import com.yyxnb.widget.config.DataConfig
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val mAdapter by lazy { MainListAdapter() }

    override fun initLayoutResId(): Int = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mAdapter

        mAdapter.setDataItems(DataConfig.data)

    }
}
