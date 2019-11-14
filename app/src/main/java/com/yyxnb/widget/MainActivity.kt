package com.yyxnb.widget

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.yyxnb.arch.base.BaseActivity
import com.yyxnb.view.rv.MultiItemTypeAdapter
import com.yyxnb.widget.adapter.MainListAdapter
import com.yyxnb.widget.config.DataConfig
import com.yyxnb.widget.fragments.NetWorkFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private val mAdapter by lazy { MainListAdapter() }

    override fun initLayoutResId(): Int = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mAdapter

        mAdapter.setDataItems(DataConfig.data)

        mAdapter.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener{
            override fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int) {
                startFragment(NetWorkFragment.newInstance())
            }

            override fun onItemLongClick(view: View, holder: RecyclerView.ViewHolder, position: Int): Boolean {
                return false
            }

        })

    }
}
