package com.yyxnb.widget.utils;

import android.os.Handler;
import android.os.Looper;

import com.yyxnb.view.proxy.http.HttpOptions;
import com.yyxnb.view.proxy.http.ICallBack;
import com.yyxnb.view.proxy.http.IHttpProxy;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpProxy implements IHttpProxy {

    private Handler handler = new Handler(Looper.getMainLooper());
    private OkHttpClient okHttpClient = new OkHttpClient();
    @Override
    public void loadHttp(@NotNull HttpOptions options) {

        Request request = new Request.Builder()
                .url(options.getUrl())
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handler.post(() -> {
                    options.getCallBack().onFailure(e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    handler.post(() -> {
                        try {
                            options.getCallBack().onSuccess(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    handler.post(() -> {
                        options.getCallBack().onFailure(response.toString());
                    });
                }
            }
        });

//        if (options.getType() == HttpType.GET){
//
//        }else if (options.getType() == HttpType.POST){
//
//        }
    }

    @Override
    public void cancel(@NotNull String tag) {
//        okHttpClient
    }
}
