package com.yyxnb.simple.base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {


    abstract void initView(Bundle savedInstanceState);
}
