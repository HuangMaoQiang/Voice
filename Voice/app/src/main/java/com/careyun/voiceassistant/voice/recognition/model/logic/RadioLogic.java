package com.careyun.voiceassistant.voice.recognition.model.logic;

import android.content.Context;
import android.os.RemoteException;

import com.careyun.aidlfm.ServiceManager;
import com.careyun.voiceassistant.app.VoiceApplication;
import com.careyun.voiceassistant.util.Constant;
import com.careyun.voiceassistant.voice.recognition.model.clcmd.AppInfoService;
import com.careyun.voiceassistant.voice.tts.presenter.SynthesizerPresenterImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * Created by Huangmq on 2017/4/10.
 */

public class RadioLogic {

    private ServiceManager serviceManager;
    private String appName = "网络收音机";
    private AppInfoService mAppInfoService;

    public RadioLogic( ) {
        serviceManager = ServiceManager.getmInstance();
        mAppInfoService = new AppInfoService(VoiceApplication.mContext);
    }

    public void Logic(String asrRes, JSONObject command) throws JSONException, RemoteException, IOException {

        String domain = command.optString("domain");
        String mIntent = command.optString("intent");
        String Obj = command.optString("object");
        JSONObject r = new JSONObject(Obj);
        if (mIntent.equals("close")) {
            if (serviceManager.miTingFMService != null) {
                serviceManager.miTingFMService.playFinish();
                SynthesizerPresenterImpl.getInstance().startSpeak("已为您关闭" + appName, Constant.STOP_LISTEN);
            } else {
                SynthesizerPresenterImpl.getInstance().startSpeak("不好意思我没有学会此功能，请从说", Constant.RE_TRY);
            }
        } else if (domain.equals("radio")) {
            mAppInfoService.OpenApp(appName);
            SynthesizerPresenterImpl.getInstance().startSpeak("已为您打开" + appName, Constant.STOP_LISTEN);
        }
    }
}
