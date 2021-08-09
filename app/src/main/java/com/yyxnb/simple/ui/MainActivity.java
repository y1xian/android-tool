package com.yyxnb.simple.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.yyxnb.oh.core.UITask;
import com.yyxnb.simple.R;

/**
 * ================================================
 * 作    者：yyx
 * 日    期：2021/08/08
 * 描    述：主页
 * ================================================
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UITask.run(() -> {

            Log.w("---->", "io " + Thread.currentThread());

            UITask.post(() -> {
                Log.w("---->", "main " +Thread.currentThread());
            });

        });
    }
}