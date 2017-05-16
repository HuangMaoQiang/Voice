package com.careyun.voiceassistant.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Huangmq on 2017/3/16.
 * 基础Activity
 */

public class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("BaseActivity",getClass().getSimpleName());
        ActivityCollerctor.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollerctor.removeActivity(this);
    }
}
