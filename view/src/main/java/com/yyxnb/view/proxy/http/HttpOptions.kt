package com.yyxnb.view.proxy.http


class HttpOptions {
    var url: String = HttpHelper.getBaserUrl()
    var tag: String ?= null
    var type: HttpType = HttpType.POST
    var params: Map<String, Any>? = null
    var httpProxy: IHttpProxy? = null
    var callBack: ICallBack? = null


    constructor(url: String) {
        this.url = url
    }

    fun get(): HttpOptions {
        this.type = HttpType.GET
        return this
    }

    fun post(): HttpOptions {
        this.type = HttpType.POST
        return this
    }

    fun params(params: Map<String, Any>): HttpOptions {
        this.params = params
        return this
    }

    fun cancel(tag :String): HttpOptions {
        this.tag = tag
        return this
    }


    fun execute(callback: ICallBack) {
        this.callBack = callback
        HttpHelper.loadOptions(this)
    }
}