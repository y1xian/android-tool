package com.yyxnb.arch.livedata

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.yyxnb.arch.base.mvvm.BaseViewModel
import com.yyxnb.arch.interfaces.IActivity
import com.yyxnb.arch.interfaces.IFragment
import com.yyxnb.arch.interfaces.IView
import com.yyxnb.utils.AppConfig
import com.yyxnb.utils.AppConfig.getFiledClazz
import java.io.Serializable
import java.lang.reflect.Field

@Suppress("UNCHECKED_CAST")
object ViewModelFactory : Serializable {

    /**
     *  创建 对应的 ViewModel, 并且 添加 通用 (LiveData) 到 ViewModel中
     */
    fun createViewModel(fragment: Fragment, field: Field): BaseViewModel {
        val viewModelClass = getFiledClazz<BaseViewModel>(field)
        val viewModel = ViewModelProviders.of(fragment).get(viewModelClass)
        initSharedData(fragment as IFragment, viewModel)
        return viewModel
    }

    /**
     *  创建 对应的 ViewModel, 并且 添加 通用 (LiveData) 到 ViewModel中
     */
    fun createViewModel(activity: FragmentActivity, field: Field): BaseViewModel {
        val viewModelClass = getFiledClazz<BaseViewModel>(field)
        val viewModel = ViewModelProviders.of(activity).get(viewModelClass)
        initSharedData(activity as IActivity, viewModel)
        return viewModel
    }

    private fun initSharedData(view: IView, viewModel: BaseViewModel) {
        viewModel.defUI.toastEvent.observe(view as LifecycleOwner, Observer {
            AppConfig.toast(it.toString())
        })
        // 订阅通用 observer
        viewModel.defUI.msgEvent.observe(view as LifecycleOwner, Observer {
            view.handleEvent(it)
        })
    }
}