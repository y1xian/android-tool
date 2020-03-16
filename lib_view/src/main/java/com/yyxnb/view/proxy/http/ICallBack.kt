package com.yyxnb.view.proxy.http

/**
 *  定义网络请求基类接口回调
 */
interface ICallBack {
    fun onSuccess(result: String)
    fun onFailure(result: String)
}