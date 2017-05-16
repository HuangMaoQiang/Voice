package com.careyun.voiceassistant.voice.recognition.model.logic;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.careyun.voiceassistant.app.VoiceApplication;
import com.careyun.voiceassistant.map.Search.PoiSearchHorizontal;
import com.careyun.voiceassistant.util.Constant;
import com.careyun.voiceassistant.voice.recognition.model.clcmd.DataSave;
import com.careyun.voiceassistant.voice.tts.presenter.SynthesizerPresenterImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * Created by Huangmq on 2017/3/28.
 */

public class MapLogic {

    private Context mContext;
    private String mArrival = null;
    private String centre = null;
    private String loca = null;
    private String domain = null;


    public MapLogic( ){
//        this.mContext = mContext;
    }

    public void Logic(String asrRes,JSONObject command,String myloca) throws JSONException, IOException {

        if(command!=null) {
            domain = command.optString("domain");
            String Obj = command.optString("object");
            JSONObject r = new JSONObject(Obj);
            mArrival = r.optString("arrival");
            centre = r.optString("centre");
        }else {
            if(centre == null){
                centre = asrRes;
            }
        }
        if (!TextUtils.isEmpty(mArrival)) {
            Intent mpoiSear = new Intent(VoiceApplication.mContext, PoiSearchHorizontal.class);
            mpoiSear.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mpoiSear.putExtra("ARRIVAL", mArrival);
            mpoiSear.putExtra("RESULT",asrRes);
            mpoiSear.putExtra("LOCATION",myloca);
            VoiceApplication.mContext.startActivity(mpoiSear);
        }
        else if(!TextUtils.isEmpty(centre)){
            Intent mpoiSear = new Intent(VoiceApplication.mContext, PoiSearchHorizontal.class);
            mpoiSear.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mpoiSear.putExtra("ARRIVAL", centre);
            mpoiSear.putExtra("RESULT",asrRes);
            mpoiSear.putExtra("LOCATION",myloca);
            VoiceApplication.mContext.startActivity(mpoiSear);
        }
        else {
            DataSave.getInstance().Save(domain+"\t"+asrRes, Constant.SPEECH_TEXT);
            DataSave.getInstance().copyFromAssetsToSdcard(false,asrRes);
            SynthesizerPresenterImpl.getInstance().startSpeak("不好意思我还不会，请从说", Constant.RE_TRY);
        }
    }
}
