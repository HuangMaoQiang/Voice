package com.careyun.voiceassistant.voice.recognition.model.logic;

import android.os.RemoteException;

import com.careyun.voiceassistant.app.VoiceApplication;
import com.careyun.voiceassistant.util.Constant;
import com.careyun.voiceassistant.voice.recognition.model.clcmd.DataSave;
import com.careyun.voiceassistant.voice.tts.presenter.SynthesizerPresenterImpl;
import com.ruiyi.cardvr.aidlservice.DvrServiceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * Created by Huangmq on 2017/3/28.
 */

public class CustomLogic {
    public DvrServiceManager mDvrServiceManager;

    public CustomLogic(){
        mDvrServiceManager = DvrServiceManager.getmInstance();

    }

    public void Logic(String asrRes,JSONObject command) throws JSONException, RemoteException, IOException {
        String domain = command.optString("domain");
        String Cmd = command.optString("intent");
        String Obj = command.optString("object");
        JSONObject r = new JSONObject(Obj);
        String func = r.optString("function");
        if (Cmd.equals("open")) {
            if (func.equals("安全辅助"))
            {
                if (mDvrServiceManager.mDvrService != null) {
                    try {
                        mDvrServiceManager.mDvrService.setAdas(true);
                    } catch (RemoteException mE) {
//                        WakeUp.getInstance(mContext).start();
                        mE.printStackTrace();
                    }
                }else {
                    SynthesizerPresenterImpl.getInstance().startSpeak("行车记录仪未打开", Constant.CONTINUE_LISTEN);
                }
            }
            else if(func.equals("录像")){
                if (mDvrServiceManager.mDvrService != null) {
                    //mDvrServiceManager.mDvrService.onVideoRecord();
                    if (mDvrServiceManager.mDvrService.startRecord()){
                        SynthesizerPresenterImpl.getInstance().startSpeak("开始录像", Constant.STOP_LISTEN);
                    }
                }else {
                    SynthesizerPresenterImpl.getInstance().startSpeak("行车记录仪未打开", Constant.CONTINUE_LISTEN);
                }
            }
            else
            {
                DataSave.getInstance().Save(domain+"\t"+asrRes,Constant.SPEECH_TEXT);
                SynthesizerPresenterImpl.getInstance().startSpeak("不好意思我没有学会此功能，请从说", Constant.CONTINUE_LISTEN);
            }

        } else if (Cmd.equals("close")) {
            if (func.equals("安全辅助")) {
                if (mDvrServiceManager.mDvrService != null) {
                    try {
                        mDvrServiceManager.mDvrService.setAdas(false);
                    } catch (RemoteException mE) {
//                        WakeUp.getInstance(mContext).start();
                        mE.printStackTrace();
                    }
                }else {
                    SynthesizerPresenterImpl.getInstance().startSpeak("行车记录仪未打开", Constant.CONTINUE_LISTEN);
                }
            }
            else if(func.equals("录像")){
                if (mDvrServiceManager.mDvrService != null) {
                    if(mDvrServiceManager.mDvrService.stopRecord()) {
                        SynthesizerPresenterImpl.getInstance().startSpeak("已为您停止录像", Constant.STOP_LISTEN);
                    }
                }
                else if (mDvrServiceManager.mDvrService == null) {
                    SynthesizerPresenterImpl.getInstance().startSpeak("行车记录仪未打开", Constant.CONTINUE_LISTEN);
                }
            }
            else {
                boolean rest = mDvrServiceManager.mDvrService.stopMedia();
                if (rest == true) {
//                    DataSave.getInstance(mContext).Save(asrRes,Constant.SPEECH_TEXT);
                    SynthesizerPresenterImpl.getInstance().startSpeak("已为您关闭" + func, Constant.STOP_LISTEN);
                } else {
                    DataSave.getInstance().Save(domain+"\t"+asrRes,Constant.SPEECH_TEXT);
                    DataSave.getInstance().copyFromAssetsToSdcard(false,asrRes);
                    SynthesizerPresenterImpl.getInstance().startSpeak("不好意思我没有学会此功能，请从说", Constant.RE_TRY);
                }
            }
        }
//        WakeUp.getInstance(mContext).start();
    }
}
