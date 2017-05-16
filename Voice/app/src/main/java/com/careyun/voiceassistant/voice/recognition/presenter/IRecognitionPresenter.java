package com.careyun.voiceassistant.voice.recognition.presenter;

import android.content.Intent;

/**
 * Created by Huangmq on 2017/4/11.
 */

public interface IRecognitionPresenter {

    void startListening(Intent mIntent);

    void bindParams(Intent mIntent);

    void stopListening();

    void onDestroy();

    void sendSCENNE(String SCENNE);

}
