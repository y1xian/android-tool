package com.yyxnb.lib.andorid.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import com.yyxnb.lib.andorid.log.LogUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 服务相关工具类
 *
 * <pre>
 * </pre>
 *
 * @author yyx
 * @date 2022/4/23
 */
public class ServiceUtil {

    /**
     * 获取所有运行的服务
     *
     * @param context 上下文
     * @return 服务名集合
     */
    public static Set<String> getAllRunningService(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos = activityManager.getRunningServices(0x7FFFFFFF);
        Set<String> names = new HashSet<>();
        if (infos == null || infos.size() == 0) {
            return null;
        }
        for (ActivityManager.RunningServiceInfo info : infos) {
            names.add(info.service.getClassName());
        }
        return names;
    }

    /**
     * 判断服务是否运行
     *
     * @param context   上下文
     * @param className 完整包名的服务类名
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isServiceRunning(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos = activityManager.getRunningServices(0x7FFFFFFF);
        if (infos == null || infos.size() == 0) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo info : infos) {
            if (className.equals(info.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 停止服务
     *
     * @param context   上下文
     * @param className 完整包名的服务类名
     * @return {@code true}: 停止成功<br>{@code false}: 停止失败
     */
    public static boolean stopService(Context context, String className) {
        try {
            Intent intent = new Intent(context, Class.forName(className));
            return context.stopService(intent);
        } catch (Exception e) {
            LogUtil.e("停止服务" + e.getMessage());
            return false;
        }
    }


    /**
     * 启动服务
     *
     * @param context 上下文
     * @param intent  意图
     */
    public static void startService(Context context, Intent intent) {
        context.startService(intent);
    }

    /**
     * 绑定服务
     *
     * @param context    上下文
     * @param intent     意图
     * @param connection ServiceConnection代表与服务的连接，它只有两个方法
     * @param flags      0代表不自动创建、BIND_AUTO_CREATE则代表自动创建
     */
    public static void bindService(Context context, Intent intent, ServiceConnection connection, int flags) {
        context.bindService(intent, connection, flags);
    }

    /**
     * 停止服务
     *
     * @param context 上下文
     * @param intent  意图
     */
    public static void stopService(Context context, Intent intent) {
        context.stopService(intent);
    }

    /**
     * 解除绑定
     *
     * @param context    上下文
     * @param connection ServiceConnection
     */
    public static void unBindService(Context context, ServiceConnection connection) {
        context.unbindService(connection);
    }


}
