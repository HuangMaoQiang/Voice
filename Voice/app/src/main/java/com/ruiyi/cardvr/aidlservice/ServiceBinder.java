package com.ruiyi.cardvr.aidlservice;

import android.os.RemoteException;
import android.util.Log;

import com.careyun.voiceassistant.util.Constant;
import com.careyun.voiceassistant.voice.tts.presenter.SynthesizerPresenterImpl;
import com.ruiyi.cardvr.aidl.IMediaCallBack;


public class ServiceBinder extends IMediaCallBack.Stub {
    private static String TAG="CarvrServiceBinder";

    @Override
    public void TakePhotoCallBack(String aPath) throws RemoteException {
        Log.e(TAG,"抓拍成功回调");
        SynthesizerPresenterImpl.getInstance().startSpeak("抓拍成功", Constant.STOP_LISTEN);
    }

    @Override
    public void TakeVideoCallBack(String aPath) throws RemoteException {
        Log.e(TAG,"录像成功回调");
        SynthesizerPresenterImpl.getInstance().startSpeak("录像成功", Constant.STOP_LISTEN);
    }

    @Override
    public void AdasCallBack(boolean isAdas) throws RemoteException {
        Log.e(TAG,"Adas回调");
        if (isAdas == true) {
            SynthesizerPresenterImpl.getInstance().startSpeak("安全辅助已打开",  Constant.STOP_LISTEN);
        }
        else
        {
            SynthesizerPresenterImpl.getInstance().startSpeak("安全辅助已关闭",  Constant.STOP_LISTEN);
        }
    }

    @Override
    public void onSpeech(String mS) throws RemoteException {
//        mSpeechSynthesizer.speak(mS,"1");
        SynthesizerPresenterImpl.getInstance().startSpeak(mS, Constant.STOP_LISTEN);
    }


}
