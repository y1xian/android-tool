package com.yyxnb.arch.base.mvvm

import android.arch.lifecycle.DefaultLifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper
import com.yyxnb.arch.common.MsgEvent
import com.yyxnb.arch.livedata.SingleLiveEvent
import com.yyxnb.utils.ext.tryCatch
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * 逻辑处理
 *
 * 负责数据处理和View层与Model层的交互。
 * ViewModel通过数据仓库Repository获取数据来源，处理来自View的事件命令，同时更新数据。
 * @author : yyx
 * @date ：2018/6/13
 */
abstract class BaseViewModel : ViewModel(), DefaultLifecycleObserver {

    val defUI: UIChange by lazy { UIChange() }

    open val mScope: CoroutineScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

    /**
     * 所有网络请求都在 viewModelScope 域中启动，当页面销毁时会自动
     * 调用ViewModel的  #onCleared 方法取消所有协程
     */
    fun launchUI(block: suspend CoroutineScope.() -> Unit) {
        mScope.launch {
            tryCatch({
                block()
            }, {
                throw it
            })
        }
    }

    /**
     * 用流的方式进行网络请求
     */
    fun <T> launchFlow(block: suspend () -> T): Flow<T> {
        return flow {
            emit(block())
        }
    }

    /**
     * IO
     */
    suspend fun <T> launchOnIO(block: suspend CoroutineScope.() -> T) {
        withContext(Dispatchers.IO) {
            block
        }
    }

    /**
     * UI事件
     */
    inner class UIChange {
        val toastEvent by lazy { SingleLiveEvent<String>() }
        val msgEvent by lazy { SingleLiveEvent<MsgEvent>() }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        mScope.cancel()
    }
}
