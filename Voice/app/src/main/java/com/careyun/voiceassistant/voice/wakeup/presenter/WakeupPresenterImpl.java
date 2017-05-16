package com.careyun.voiceassistant.voice.wakeup.presenter;

import android.content.Context;
import android.os.RemoteException;
import android.util.AndroidRuntimeException;
import android.util.Log;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.careyun.aidlfm.ServiceManager;
import com.careyun.aidlmusic.MusicServiceManager;
import com.careyun.voiceassistant.app.VoiceApplication;
import com.careyun.voiceassistant.voice.wakeup.view.IWakeUpView;
import com.ruiyi.cardvr.aidlservice.DvrServiceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Huangmq on 2017/4/10.
 */

public class WakeupPresenterImpl implements IWakePresenter {

    public static final String TAG = "WakeupPresenter";
    private EventManager mWpEventManager;
    private static IWakeUpView iWakeUpView;
    private DvrServiceManager mDvrServiceManager;
    private ServiceManager serviceManager;
    private MusicServiceManager musicServiceManager;



    public WakeupPresenterImpl(IWakeUpView iWakeUpView) {
        this.iWakeUpView =iWakeUpView;
        mDvrServiceManager= DvrServiceManager.getmInstance();
        musicServiceManager = MusicServiceManager.getmInstance();
        serviceManager = ServiceManager.getmInstance();
        //create方法示是一个静态方法，还有一个重载方法EventManagerFactory.create(context, name, version)
        //由于百度文档没有给出每个参数具体含义，我们只能按照官网给的demo写了
        mWpEventManager = EventManagerFactory.create(VoiceApplication.mContext, "wp");
        //注册监听事件
        mWpEventManager.registerListener(new MyEventListener());
    }

    public void setWakeupListener(IWakeUpView iWakeUpView) {
        this.iWakeUpView = iWakeUpView;
    }

    @Override
    public void startWakeup() {
        HashMap<String, String> params = new HashMap();
        // 设置唤醒资源, 唤醒资源请到 http://yuyin.baidu.com/wake#m4 来评估和导出
        params.put("kws-file", "assets:///WakeUp.bin");
        mWpEventManager.send("wp.start", new JSONObject(params).toString(), null, 0, 0);
        Log.e(TAG, "<---------唤醒已经开始工作了------->");
    }

    @Override
    public void stopWakeup() {
        // 具体参数的百度没有具体说明，大体需要以下参数
        // send(String arg1, byte[] arg2, int arg3, int arg4)
        mWpEventManager.send("wp.stop", null, null, 0, 0);
        Log.e(TAG, "<-----------唤醒已经停止------------->");
    }

    @Override
    public void setVolumeDown() {
        if (musicServiceManager.iMusicService != null) {
            try {
                musicServiceManager.iMusicService.setVolume(0.1f,0.1f);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        if (mDvrServiceManager.mDvrService != null) {
            try {
                mDvrServiceManager.mDvrService.setVolume(0.1f,0.1f);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        if(serviceManager.miTingFMService!= null){
            try {
                if (serviceManager.miTingFMService.setVolume(0.1f,0.1f)){
                }else {
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setVolumeUp() {
        if(serviceManager.miTingFMService!= null){
            try {
                if (serviceManager.miTingFMService.setVolume(1f,1f)){
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        if (musicServiceManager.iMusicService != null) {
            try {
                musicServiceManager.iMusicService.setVolume(1f,1f);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        if (mDvrServiceManager.mDvrService != null) {
            try {
                mDvrServiceManager.mDvrService.setVolume(1f,1f);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        mWpEventManager.unregisterListener(new MyEventListener());
    }

    private class MyEventListener implements EventListener {
        @Override
        public void onEvent(String name, String params, byte[] data, int offset, int length) {
            try {
                //解析json文件
                JSONObject json = new JSONObject(params);
                if ("wp.data".equals(name)) { // 每次唤醒成功, 将会回调name=wp.data的时间, 被激活的唤醒词在params的word字段
                    String word = json.getString("word"); // 唤醒词
                     /*
                      * 这里大家可以根据自己的需求实现唤醒后的功能，这里我们简单打印出唤醒词
                      */
                    Log.e(TAG, word);
                    //通知外面调用此类的类已经唤醒成功
                    if (iWakeUpView != null) {
                        iWakeUpView.wakeupSuccess();
                    }
                } else if ("wp.exit".equals(name)) {
                    if (iWakeUpView != null) {
                        iWakeUpView.wakeupFail();
                    }
                }
            } catch (JSONException e) {
                throw new AndroidRuntimeException(e);
            }
        }
    }
}
