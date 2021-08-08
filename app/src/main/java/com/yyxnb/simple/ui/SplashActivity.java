package com.yyxnb.simple.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yyxnb.simple.R;

/**
 * ================================================
 * 作    者：yyx
 * 日    期：2021/08/08
 * 描    述：闪屏页
 * ================================================
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // 白屏时间 + 2秒进入首页 ≈ 3~5秒
        handler.sendEmptyMessageDelayed(1, 2 * 1000);
    }

    private final Handler handler = new Handler(message -> {
        if (message.what == 1) {
            inMain();
        }
        return false;
    });

    private void inMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 避免内存泄漏
        handler.removeCallbacksAndMessages(null);
    }
}