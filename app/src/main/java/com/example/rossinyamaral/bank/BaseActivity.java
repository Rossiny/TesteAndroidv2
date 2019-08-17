package com.example.rossinyamaral.bank;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    protected void adjustBar(int color) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        ViewsUtils.setStatusBarColor(this, color);
    }
}
