package com.yyxnb.widget.fragments.http

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.yyxnb.arch.base.mvvm.BaseActivityVM
import com.yyxnb.widget.R
import com.yyxnb.widget.vm.NetWorkViewModel

class NetWorkActivity : BaseActivityVM<NetWorkViewModel>() {

    override fun initLayoutResId(): Int = R.layout.activity_net_work

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        mViewModel.reqTeam()
    }

    override fun initObservable() {
        super.initObservable()


        mViewModel.getTestList().observe(this, Observer {
            t ->

        })
    }
}
