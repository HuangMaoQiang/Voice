package com.careyun.voiceassistant.voice.tts.model;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.careyun.voiceassistant.voice.tts.util.InitEnvImpl;

/**
 * Created by Huangmq on 2017/4/19.
 */

public abstract class SynthesizerService extends Service implements SpeechSynthesizerListener{




    @Override
    public void onCreate() {

//        new InitEnvImpl(this).initEnv();

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onSynthesizeStart(String s) {

    }

    @Override
    public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {

    }

    @Override
    public void onSynthesizeFinish(String s) {

    }

    @Override
    public void onSpeechStart(String s) {

    }

    @Override
    public void onSpeechProgressChanged(String s, int i) {

    }

/*    @Override
    public void onSpeechFinish(String s) {

    }*/

    @Override
    public void onError(String s, SpeechError speechError) {

    }
}
