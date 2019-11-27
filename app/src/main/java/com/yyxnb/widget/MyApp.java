package com.yyxnb.widget;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.yyxnb.http.RetrofitManager;
import com.yyxnb.http.config.OkHttpConfig;
import com.yyxnb.view.proxy.http.HttpHelper;
import com.yyxnb.view.proxy.imageloader.ImageHelper;
import com.yyxnb.widget.utils.GlideImageProxy;
import com.yyxnb.widget.utils.OkHttpProxy;

import okhttp3.OkHttpClient;

import static com.yyxnb.widget.config.BaseAPI.BASE_URL;
import static com.yyxnb.widget.config.BaseAPI.BASE_URL_MOCKY;
import static com.yyxnb.widget.config.BaseAPI.URL_KEY_1;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initRxHttp();

        ImageHelper.INSTANCE.init(new GlideImageProxy());
        HttpHelper.INSTANCE.init(new OkHttpProxy()).setBaseUrl("http://www.mocky.io/");

    }

    private void initRxHttp() {
        RetrofitManager.INSTANCE
                .init(this)
                .setBaseUrl(BASE_URL)
                .setHeaderPriorityEnable(true)
                .putHeaderBaseUrl(URL_KEY_1, BASE_URL_MOCKY)
                .setOkClient(mClient);
    }

    private OkHttpClient mClient = new OkHttpConfig.Builder()
            //全局的请求头信息
//            .setHeaders(new HashMap<>())
            //开启缓存策略(默认false)
            //1、在有网络的时候，先去读缓存，缓存时间到了，再去访问网络获取数据；
            //2、在没有网络的时候，去读缓存中的数据。
            .setCache(true)
            //全局持久话cookie,保存本地每次都会携带在header中（默认false）
            .setSaveCookie(true)
            //可以添加自己的拦截器(比如使用自己熟悉三方的缓存库等等)
//            .setAddInterceptor(new RetryInterceptor.Builder()
//                    .executionCount(1).retryInterval(888)
//                    .build())
            //全局ssl证书认证
            //1、信任所有证书,不安全有风险（默认信任所有证书）
            .setSslSocketFactory()
            //2、使用预埋证书，校验服务端证书（自签名证书）
//            .setSslSocketFactory(cerInputStream)
            //3、使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
            //.setSslSocketFactory(bksInputStream,"123456",cerInputStream)
            //全局超时配置
            .setTimeout(8)
            //全局是否打开请求log日志
            .setLogEnable(true)
            .build();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
    }
}
