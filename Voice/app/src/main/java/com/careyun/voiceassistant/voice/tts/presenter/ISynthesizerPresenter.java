package com.careyun.voiceassistant.voice.tts.presenter;

/**
 * Created by Huangmq on 2017/4/19.
 */

public interface ISynthesizerPresenter {

    void initTTS();

    void startSpeak(String speech, String Massage);

    void onDestroy();
}
