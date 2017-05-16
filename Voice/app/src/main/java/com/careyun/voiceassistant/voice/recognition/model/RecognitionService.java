package com.careyun.voiceassistant.voice.recognition.model;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.support.annotation.Nullable;


/**
 * Created by Huangmq on 2017/4/12.
 */

public abstract class RecognitionService extends Service implements RecognitionListener {



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
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {

    }

/*    @Override
    public void onError(int i) {

    }*/

/*    @Override
    public void onResults(Bundle bundle) {

    }*/

/*    @Override
    public void onPartialResults(Bundle bundle) {

    }*/

    @Override
    public void onEvent(int i, Bundle bundle) {

    }
}
