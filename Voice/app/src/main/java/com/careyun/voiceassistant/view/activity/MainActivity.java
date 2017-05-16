package com.careyun.voiceassistant.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.careyun.voiceassistant.R;
import com.careyun.voiceassistant.service.VoiceService;
import com.careyun.voiceassistant.voice.recognition.model.clcmd.ScreenCmd;
import com.lib.incrementalsdk.PatchUtils;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    ScreenCmd screenCmd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, VoiceService.class);
        startService(intent);
//        finish();
        Button up = (Button) findViewById(R.id.btn_up);
        Button down = (Button) findViewById(R.id.btn_down);
        down.setOnClickListener(this);
        up.setOnClickListener(this);
        screenCmd = new ScreenCmd(this);
    }


    @Override
    public void onClick(View view) {
        final String old="/sdcard/1.0.0.apk";
        final String newd="/sdcard/2.0.0.apk";
        final String oldP="/sdcard/1.0.0.patch";
        final String temp="/sdcard/temp2.0.0.apk";
        switch (view.getId()){

            case R.id.btn_up:
//                screenCmd.setUpSystemBrightness();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int res=PatchUtils.createPatch(old,newd,oldP);
                        Log.e("xxxxxxxxxxxxx","差分结果"+res);
                    }
                }).start();
                break;
            case R.id.btn_down:
//                screenCmd.setDownSystemBrightness();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int res=PatchUtils.createPatch(old,temp,oldP);
                        Log.e("xxxxxxxxxxxxx","合成结果"+res);
                    }
                }).start();
                break;
        }

    }
}
