package com.yyxnb.arch.base.mvvm

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yyxnb.arch.base.BaseActivity
import com.yyxnb.arch.common.MsgEvent
import com.yyxnb.utils.AppConfig

@Deprecated("用注解@BindViewModel代替")
abstract class BaseActivityVM<VM : BaseViewModel> : BaseActivity() {

    /**
     * ViewModel
     */
    protected lateinit var mViewModel: VM

    override fun initView(savedInstanceState: Bundle?) {
        mViewModel = initViewModel(this, AppConfig.getInstance(this, 0)!!)
        lifecycle.addObserver(mViewModel)
        registerDefUIChange()
        initObservable()
    }

    /**
     * 注册 UI 事件
     */
    private fun registerDefUIChange() {
        mViewModel.defUI.toastEvent.observe(this, Observer {
            AppConfig.toast(it.toString())
        })
        mViewModel.defUI.msgEvent.observe(this, Observer {
            handleEvent(it)
        })
    }

    /**
     * 初始化界面观察者的监听
     * 接收数据结果
     */
    override fun initObservable() {}

    /**
     * 返回消息
     */
    override fun handleEvent(msg: MsgEvent?) {}

    /**
     * 初始化ViewModel
     * create ViewModelProviders
     *
     * @return ViewModel
     */
    private fun initViewModel(activity: AppCompatActivity, modelClass: Class<VM>): VM {
        return ViewModelProviders.of(activity).get(modelClass)
    }

    override fun onDestroy() {
        super.onDestroy()
        //移除LifecycleObserver
        lifecycle.removeObserver(mViewModel)
        this.mViewModel to null
    }

}
