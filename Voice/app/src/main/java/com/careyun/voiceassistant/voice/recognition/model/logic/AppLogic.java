package com.careyun.voiceassistant.voice.recognition.model.logic;

import android.content.Context;
import android.os.RemoteException;

import com.careyun.aidlfm.ServiceManager;
import com.careyun.aidlmusic.MusicServiceManager;
import com.careyun.voiceassistant.app.VoiceApplication;
import com.careyun.voiceassistant.util.Constant;
import com.careyun.voiceassistant.voice.recognition.model.clcmd.AppInfoService;
import com.careyun.voiceassistant.voice.tts.presenter.SynthesizerPresenterImpl;
import com.ruiyi.cardvr.aidlservice.DvrServiceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * Created by Huangmq on 2017/3/28.
 */

public class AppLogic {
    private AppInfoService mAppInfoService;
    public DvrServiceManager mDvrServiceManager;
    private ServiceManager serviceManager;
    private MusicServiceManager musicServiceManager;

    public AppLogic() {
        mAppInfoService = new AppInfoService(VoiceApplication.mContext);
        mDvrServiceManager = DvrServiceManager.getmInstance();
        serviceManager = ServiceManager.getmInstance();
        musicServiceManager = MusicServiceManager.getmInstance();
    }

    public void Logic(String asrRes, JSONObject command) throws JSONException, RemoteException, IOException {
        String domain = command.optString("domain");
        String Cmd = command.optString("intent");
        String Obj = command.optString("object");
        JSONObject r = new JSONObject(Obj);
        String appName = r.optString("appname");
        if (Cmd.equals("open")) {
            mAppInfoService.OpenApp(appName);
            SynthesizerPresenterImpl.getInstance().startSpeak("已为您打开" + appName, Constant.STOP_LISTEN);
        } else if (Cmd.equals("close")) {
            if (appName.equals("行车记录仪")) {
                boolean rest = mDvrServiceManager.mDvrService.stopMedia();
                if (rest == true) {
                    SynthesizerPresenterImpl.getInstance().startSpeak("已为您关闭" + appName, Constant.STOP_LISTEN);
                } else {
                    SynthesizerPresenterImpl.getInstance().startSpeak("不好意思我没有学会此功能，请从说", Constant.RE_TRY);
                }
            } else if (appName.equals("网络收音机")) {
                if (serviceManager.miTingFMService != null) {
                    if (serviceManager.miTingFMService.playFinish()) {
                        SynthesizerPresenterImpl.getInstance().startSpeak("已为您关闭" + appName, Constant.STOP_LISTEN);
                    }else {
                        SynthesizerPresenterImpl.getInstance().startSpeak("不好意思我没有学会此功能，请从说", Constant.RE_TRY);
                    }
                }
            } else if(appName.equals("音乐播放器")){
                if (musicServiceManager.iMusicService != null) {
                    if (musicServiceManager.iMusicService.playFinish()) {
                        SynthesizerPresenterImpl.getInstance().startSpeak("已为您关闭" + appName, Constant.STOP_LISTEN);
                    }else {
                        SynthesizerPresenterImpl.getInstance().startSpeak("不好意思我没有学会此功能，请从说", Constant.RE_TRY);
                    }
                }
            }
            else {
                SynthesizerPresenterImpl.getInstance().startSpeak("不好意思我没有学会此功能，请从说", Constant.RE_TRY);
            }
        }
    }
}
