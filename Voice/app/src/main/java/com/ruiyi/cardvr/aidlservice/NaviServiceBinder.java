package com.ruiyi.cardvr.aidlservice;

import android.os.RemoteException;
import android.util.Log;

import com.careyun.navigation.INavigationCallBack;
import com.careyun.voiceassistant.util.Constant;
import com.careyun.voiceassistant.voice.tts.presenter.SynthesizerPresenterImpl;

/**
 * Created by Huangmq on 2017/2/24.
 */

public class NaviServiceBinder extends INavigationCallBack.Stub{

    private static String TAG="NaviServiceBinder";


    @Override
    public void PoiSearchCallBack() throws RemoteException {
        Log.e(TAG,"执行了POISearch");
        SynthesizerPresenterImpl.getInstance().startSpeak("找到如下地址，请说出第几个", Constant.CONTINUE_LISTEN);
    }

    @Override
    public void GeoCoderCallBack() throws RemoteException {
        Log.e(TAG,"执行了eoCoderCallBack");
    }

    @Override
    public void StartNavigationCallBack() throws RemoteException {
        Log.e(TAG,"执行了StartNavigationCallBack");
    }

    @Override
    public void cancelNavigationCallBack() throws RemoteException {
        Log.e(TAG,"执行了取消导航");
    }

    @Override
    public void onSpeech(String mS ,String mN) throws RemoteException {
//        mSpeechSynthesizer.speak(mS,"1");
        Log.e(TAG,"onspeech执行了");
        SynthesizerPresenterImpl.getInstance().startSpeak(mS,mN);
    }

}
