package com.careyun.voiceassistant.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.baidu.mapapi.SDKInitializer;
import com.careyun.aidlfm.ServiceManager;
import com.careyun.aidlmusic.MusicServiceManager;
import com.careyun.aidlweather.WeatherServiceManager;
import com.careyun.voiceassistant.map.Local.MyLocation;
import com.careyun.voiceassistant.voice.tts.util.InitEnvImpl;
import com.ruiyi.cardvr.aidlservice.DvrServiceManager;

//import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Huangmq on 2017/4/12.
 */

public class VoiceApplication extends Application {
    private static ArrayList<Handler> mSecurityEngineEvent;
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        new InitEnvImpl().initEnv();
//
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        mSecurityEngineEvent=new ArrayList<Handler>();
        MyLocation mMyLocation = new MyLocation();
        mMyLocation.StartLocation();

        new Thread(new Runnable() {
            @Override
            public void run() {
                WeatherServiceManager.getmInstance().bindMpospService(mContext);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServiceManager.getmInstance().bindMpospService(mContext);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                MusicServiceManager.getmInstance().bindMpospService(mContext);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                DvrServiceManager.getmInstance().bindMpospService(mContext);
            }
        }).start();
    }

    public static void addHandler(Handler handler) {
        if (!mSecurityEngineEvent.contains(handler))
            mSecurityEngineEvent.add(handler);
    }

    public static void removeHandler(Handler handler) {
        mSecurityEngineEvent.remove(handler);
    }
    /** 消息分发 **/
    public static void callback(int what, Object obj) {
        for (Handler h : mSecurityEngineEvent) {
            h.obtainMessage(what, obj).sendToTarget();
        }
    }
}
