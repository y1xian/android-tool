package com.yyxnb.widget.utils;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.yyxnb.http.interceptor.HttpLogger;
import com.yyxnb.view.proxy.http.HttpOptions;
import com.yyxnb.view.proxy.http.HttpType;
import com.yyxnb.view.proxy.http.IHttp;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttp implements IHttp {

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void loadHttp(@NotNull HttpOptions options) {
        File sdCache = Environment.getExternalStorageDirectory().getAbsoluteFile();
        int cacheSize = 20 * 1024 * 1024;

        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(options.getConnectTimeout(), TimeUnit.SECONDS)
                .writeTimeout(options.getWriteTimeout(), TimeUnit.SECONDS)
                .readTimeout(options.getReadTimeout(), TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cache(new Cache(sdCache.getAbsoluteFile(), cacheSize))
                .addInterceptor(logInterceptor);

       final OkHttpClient okHttpClient = builder.build();

        Request request;

        if (options.getType() == HttpType.GET){
            request = new Request.Builder()
                    .url(options.getUrl())
                    .get()//默认就是GET请求，可以不写
                    .build();
        }else {
            //请求body
//            RequestBody body = new FormBody.Builder()
//                    .add("weaid", "1")
//                    .add("date", "2018-08-13")
//                    .add("appkey", "10003")
//                    .add("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4")
//                    .add("format", "json")
//                    .build();

            JSONArray jArray = new JSONArray();
            jArray.put(options.getParams());
            String json = jArray.toString();
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

            request = new Request.Builder()
                    .url(options.getUrl())
                    .post(body)
                    .build();
        }

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
                            new Thread(() -> {
                                try {
                                    options.getCallBack().onSuccess(response.body().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        } catch (Exception e) {
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
