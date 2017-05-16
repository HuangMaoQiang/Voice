package com.careyun.voiceassistant.voice.recognition.model.logic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.RemoteException;

import com.careyun.voiceassistant.app.VoiceApplication;
import com.careyun.voiceassistant.util.Constant;
import com.careyun.voiceassistant.voice.recognition.model.clcmd.DataSave;
import com.careyun.voiceassistant.voice.tts.presenter.SynthesizerPresenterImpl;
import com.ruiyi.cardvr.aidlservice.NaviServiceManager;

import org.json.JSONObject;

import java.io.IOException;


/**
 * Created by Huangmq on 2017/3/28.
 */

public class NavigationLogic {

    public NaviServiceManager mNaviServiceManager;

    public NavigationLogic(){

        mNaviServiceManager= NaviServiceManager.getmInstance();


    }

    public void Logic(String asrRes,JSONObject command) throws RemoteException, IOException {
        String domain = command.optString("domain");
        String mIntent = command.optString("intent");
        if(mIntent.equals("quit"))
        {
            if(mNaviServiceManager.mNavigationService!=null)
            {
                mNaviServiceManager.mNavigationService.cancelNavigation();
            }
        }
        else if(mIntent.equals("route_home"))
        {
            Intent i1 = new Intent();
            i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i1.setData(Uri.parse("baidumap://map/navi/common?addr=home"));
            VoiceApplication.mContext.startActivity(i1);
        }
        else if(mIntent.equals("route_work"))
        {
            Intent i1 = new Intent();
            i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i1.setData(Uri.parse("baidumap://map/navi/common?addr=company"));
            VoiceApplication.mContext.startActivity(i1);
        }
        else
        {
            DataSave.getInstance().Save(domain+"\t"+asrRes, Constant.SPEECH_TEXT);
            DataSave.getInstance().copyFromAssetsToSdcard(false,asrRes);
            SynthesizerPresenterImpl.getInstance().startSpeak("不好意思我没听清，请从说", Constant.CONTINUE_LISTEN);
        }

    }



}
