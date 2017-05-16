package com.careyun.voiceassistant.voice.recognition.model.logic;

import android.content.Context;
import android.os.RemoteException;
import android.text.TextUtils;

import com.careyun.aidlfm.ServiceManager;
import com.careyun.voiceassistant.util.Constant;
import com.careyun.voiceassistant.voice.tts.presenter.SynthesizerPresenterImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * Created by Huangmq on 2017/4/6.
 */

public class VedioLogic {

    private ServiceManager serviceManager;
    private String actor;
    private String type;

    public VedioLogic() {
        serviceManager = ServiceManager.getmInstance();
    }

    public void Logic(String asrRes, JSONObject command) throws JSONException, IOException, RemoteException {
        String domain = command.optString("domain");
        String mIntent = command.optString("intent");
        String Obj = command.optString("object");
        JSONObject r = new JSONObject(Obj);

        JSONArray temptype = r.optJSONArray("type");
        JSONArray tempactor = r.optJSONArray("actor");

        if (temptype != null) {
            type = temptype.getString(0);
        } else {
            type = "";
        }
        if (tempactor != null) {
            actor = tempactor.getString(0);
        } else {
            actor = "";
        }
        switch (mIntent) {
            case "search":
                if (serviceManager.miTingFMService != null) {
                    if (!TextUtils.isEmpty(type) || !TextUtils.isEmpty(actor)) {
                        if (serviceManager.miTingFMService.setSearchTracks(actor + type, false)) {
                            SynthesizerPresenterImpl.getInstance().startSpeak("找到以下节目,你要听第几个", Constant.MUSIC_SCENE);
                        }
                    }
                } else {
                    SynthesizerPresenterImpl.getInstance().startSpeak("播放器未打开，请打开后重试", Constant.RE_TRY);
                }
                break;
            case "play":
                if (serviceManager.miTingFMService != null) {
                    if (!TextUtils.isEmpty(type) || !TextUtils.isEmpty(actor)) {
                        if (serviceManager.miTingFMService.setSearchTracks(actor + type, true)) {
                            SynthesizerPresenterImpl.getInstance().startSpeak("已为您播放", Constant.STOP_LISTEN);
                        }
                    }
                } else {
                    SynthesizerPresenterImpl.getInstance().startSpeak("播放器未打开，请打开后重试", Constant.RE_TRY);
                }
                break;
        }
    }
}
