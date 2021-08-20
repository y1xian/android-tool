package com.yyxnb.simple;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;

import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshInitializer;
import com.squareup.leakcanary.LeakCanary;
import com.yyxnb.what.log.LogUtils;
import com.yyxnb.simple.helper.WriteLogHelper;
import com.yyxnb.simple.utils.NetworkCallbackImpl;

import me.jessyan.autosize.AutoSizeConfig;

public class MyApp extends Application {

    private final NetworkCallbackImpl networkCallback = new NetworkCallbackImpl();

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtils.init()
                .setShowLog(true)
                .setShowThreadInfo(false)
                .setLogWriteListener(WriteLogHelper::writeLog);

//        registerNetworkCallback();
        initServices();

        initGlobalRefreshHeaderAndFooter();

//        SkinConfig.DEBUG = true;
//        SkinManager.get().init(getApplicationContext());
//        SkinManager.get().loadSkin((String) SPUtils.getParam(SKIN_PATH,""));
//        SkinInflaterFactory.setFactory(LayoutInflater.from(this));

        // 布局
        AutoSizeConfig.getInstance().setCustomFragment(true);
        // 侧滑监听
//        AppConfig.getInstance().getApp().registerActivityLifecycleCallbacks(ParallaxHelper.getInstance());

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    /**
     * 初始化服务
     */
    private void initServices() {

    }


    //static 代码段可以防止内存泄露
    static {
        //设置全局默认配置（优先级最低，会被其他设置覆盖）
        SmartRefreshLayout.setDefaultRefreshInitializer(new DefaultRefreshInitializer() {
            @Override
            public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
                //开始设置全局的基本参数（可以被下面的DefaultRefreshHeaderCreator覆盖）
                layout.setReboundDuration(1000);
                layout.setFooterHeight(100);
                //是否启用越界回弹
                layout.setEnableOverScrollBounce(true);
                //是否启用越界拖动（仿苹果效果）1.0.4
                layout.setEnableOverScrollDrag(true);
                //是否在加载的时候禁止列表的操作
                layout.setDisableContentWhenLoading(false);
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
            }
        });


    }

    /**
     * 初始化SmartRefreshLayout
     */
    private void initGlobalRefreshHeaderAndFooter() {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.black);//全局设置主题颜色
            return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
//                return new MaterialHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context).setDrawableSize(20);
        });
    }

    /**
     * 注册网络监听
     */
    private void registerNetworkCallback() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else {
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            connectivityManager.registerNetworkCallback(request, networkCallback);
        }
    }

    /**
     * 注销网络监听
     */
    private void unregisterNetworkCallback() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
    }

    @Override
    public void onTerminate() {
        unregisterNetworkCallback();
        super.onTerminate();
    }
}
