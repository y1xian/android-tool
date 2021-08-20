package com.yyxnb.what.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.startup.Initializer;

import java.util.Collections;
import java.util.List;

/**
 * 使用ContentProvider初始化三方库
 *
 * @author yyx
 */
public class AppInitializer implements Initializer<Void> {

    @NonNull
    @Override
    public Void create(@NonNull Context context) {
        Log.e("AppInitializer", "AppInitializer初始化");
        Log.i("Oh", "\n" +
                " \n" +
                "         _______                   _____          \n" +
                "        /::\\    \\                 /\\    \\         \n" +
                "       /::::\\    \\               /::\\____\\        \n" +
                "      /::::::\\    \\             /:::/    /        \n" +
                "     /::::::::\\    \\           /:::/    /         \n" +
                "    /:::/--\\:::\\    \\         /:::/    /          \n" +
                "   /:::/    \\:::\\    \\       /:::/____/           \n" +
                "  /:::/    / \\:::\\    \\     /::::\\    \\           \n" +
                " /:::/____/   \\:::\\____\\   /::::::\\    \\   _____  \n" +
                "|:::|    |     |:::|    | /:::/\\:::\\    \\ /\\    \\ \n" +
                "|:::|____|     |:::|    |/:::/  \\:::\\    /::\\____\\\n" +
                " \\:::\\    \\   /:::/    / \\::/    \\:::\\  /:::/    /\n" +
                "  \\:::\\    \\ /:::/    /   \\/____/ \\:::\\/:::/    / \n" +
                "   \\:::\\    /:::/    /             \\::::::/    /  \n" +
                "    \\:::\\__/:::/    /               \\::::/    /   \n" +
                "     \\::::::::/    /                /:::/    /    \n" +
                "      \\::::::/    /                /:::/    /     \n" +
                "       \\::::/    /                /:::/    /      \n" +
                "        \\::/    /                /:::/    /       \n" +
                "         \\/____/                 \\::/    /        \n" +
                "                                  \\/____/         \n" +
                "                                                  \n");
        ApplicationUtils.init((Application) context);
        // 注册生命周期
        ApplicationManager.getInstance().init(ApplicationUtils.getApp());
        // 突破65535的限制
        MultiDex.install(context);
        return null;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }
}
