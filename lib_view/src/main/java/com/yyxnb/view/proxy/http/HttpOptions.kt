package com.yyxnb.view.proxy.http

import java.io.File


class HttpOptions {
    var url: String = ""
    var file: File? = null
    var tag: String = "tag"
    var connectTimeout: Int = 10
    var readTimeout: Int = 10
    var writeTimeout: Int = 10
    var cache: Boolean = true
    var type: HttpType = HttpType.POST
    var headers: Map<String, Any> = linkedMapOf()
    var params: Map<String, Any> = linkedMapOf()
    var http: IHttp? = null
    var callBack: ICallBack? = null

    fun url(url: String): HttpOptions {
        if (url.startsWith("http") || url.startsWith("https")){
            this.url = url
        }else{
            this.url = HttpHelper.getBaserUrl() + url
        }

        return this
    }

    fun get(): HttpOptions {
        this.type = HttpType.GET
        return this
    }

    fun post(): HttpOptions {
        this.type = HttpType.POST
        return this
    }

    fun headers(headers: Map<String, Any>): HttpOptions {
        this.headers = headers
        return this
    }

    fun params(params: Map<String, Any>): HttpOptions {
        this.params = params
        return this
    }

    fun cache(cache: Boolean): HttpOptions {
        this.cache = cache
        return this
    }

    fun cancel(tag: String): HttpOptions {
        this.tag = tag
        return this
    }

    fun connectTimeout(connectTimeout: Int): HttpOptions {
        this.connectTimeout = connectTimeout
        return this
    }

    fun readTimeout(readTimeout: Int): HttpOptions {
        this.readTimeout = readTimeout
        return this
    }

    fun writeTimeout(writeTimeout: Int): HttpOptions {
        this.writeTimeout = writeTimeout
        return this
    }

    fun execute(callback: ICallBack) {
        this.callBack = callback
        HttpHelper.loadOptions(this)
    }
}