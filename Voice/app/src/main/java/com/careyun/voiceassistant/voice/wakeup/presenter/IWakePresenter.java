package com.careyun.voiceassistant.voice.wakeup.presenter;

/**
 * Created by Huangmq on 2017/4/10.
 */

public interface IWakePresenter {

    void startWakeup();

    void stopWakeup();

    void setVolumeDown();

    void setVolumeUp();

    void onDestroy();

}
