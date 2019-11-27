package com.yyxnb.view.proxy.http

/**
 * 策略或者静态代理模式，开发者只需要关心HttpHelper + HttpOptions
 */
object HttpHelper {

    @Volatile
    private var httpProxy: IHttpProxy? = null
    @Volatile
    private var baseUrl: String? = null

    /**
     * 提供全局替换网络加载框架的接口，若切换其它框架，可以实现一键全局替换
     */
    fun init(http: IHttpProxy): HttpHelper {
        httpProxy = http
        return this
    }

    fun setBaseUrl(url: String) {
        baseUrl = url
    }

    fun get(): HttpOptions {
        return HttpOptions().get()
    }

    fun post(): HttpOptions {
        return HttpOptions().post()
    }

    fun getBaserUrl(): String = baseUrl.toString()

    @Synchronized
    fun loadOptions(options: HttpOptions) {
        if (options.httpProxy != null) {
            options.httpProxy!!.loadHttp(options)
        } else {
            checkNotNull()
            httpProxy!!.loadHttp(options)
        }
    }

    @Synchronized
    fun cancel(tag: String) {
        checkNotNull()
        httpProxy!!.cancel(tag)
    }

    private fun checkNotNull() {
        if (httpProxy == null) {
            throw NullPointerException("you must be set your httpProxy at first!")
        }
    }

}