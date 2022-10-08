package com.ams.amslib;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ams.amsandroidlib.ToastMsg;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ToastMsg.s(MainActivity.this,"Hey World!");
    }
}