package com.careyun.voiceassistant.voice.recognition.view;

/**
 * Created by Huangmq on 2017/4/11.
 */

public interface IRecognitionView {

    void showResult(String result);

    void showMyResult(String myRes);

    void showNone();

    void showPartialResult(String partial_result);

    void showFailedError(int error_massage);



}
