package com.yyxnb.view.proxy.http

/**
 * 代理模式中用于规范代理类和真实类行为的接口
 */
interface IHttpProxy {

    fun loadHttp(options : HttpOptions)

    fun cancel(tag :String)

//    fun get(options : HttpOptions)
//
//    fun post(options : HttpOptions)

//    fun get(url: String, callback: ICallBack)
//    fun post(url: String, params: Map<String, Any>, callback: ICallBack)
}