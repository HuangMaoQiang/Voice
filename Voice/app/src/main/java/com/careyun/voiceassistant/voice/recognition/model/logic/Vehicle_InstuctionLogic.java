package com.careyun.voiceassistant.voice.recognition.model.logic;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;

import com.careyun.voiceassistant.app.VoiceApplication;
import com.careyun.voiceassistant.util.Constant;
import com.careyun.voiceassistant.view.activity.HorizonHelpActivity;
import com.careyun.voiceassistant.voice.recognition.model.clcmd.DataSave;
import com.careyun.voiceassistant.voice.tts.presenter.SynthesizerPresenterImpl;
import com.ruiyi.cardvr.aidlservice.DvrServiceManager;
import com.ruiyi.cardvr.aidlservice.NaviServiceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * Created by Huangmq on 2017/3/28.
 */

public class Vehicle_InstuctionLogic {

    public DvrServiceManager mDvrServiceManager;
    private NaviServiceManager mNaviServiceManager;

    public Vehicle_InstuctionLogic(){
        mNaviServiceManager=NaviServiceManager.getmInstance();
        mDvrServiceManager=DvrServiceManager.getmInstance();
    }

    public void Logic(String asrRes,JSONObject command) throws JSONException, RemoteException, IOException {
        String domain = command.optString("domain");
        String Cmd = command.optString("intent");
        String Obj = command.optString("object");
        JSONObject r = new JSONObject(Obj);
        String take_pho = r.optString("function");
        String Vedio = r.optString("software");
        String Equipment =r.optString("equipment");

        if (Cmd.equals("operate")) {
            if (take_pho.equals("take_picture")||take_pho.equals("拍照")) {
                if (mDvrServiceManager.mDvrService != null) {
                    mDvrServiceManager.mDvrService.onTakePhoto();
                }else{
                    SynthesizerPresenterImpl.getInstance().startSpeak("行车记录仪未打开", Constant.CONTINUE_LISTEN);
                }
            }
            else if(take_pho.equals("录像"))
            {
                if (mDvrServiceManager.mDvrService != null) {
                    if (mDvrServiceManager.mDvrService.startRecord()){
                        SynthesizerPresenterImpl.getInstance().startSpeak("开始录像", Constant.STOP_LISTEN);
                    }
                }
                else if (mDvrServiceManager.mDvrService == null) {
                    SynthesizerPresenterImpl.getInstance().startSpeak("行车记录仪未打开", Constant.CONTINUE_LISTEN);
                }
            }
            else if(take_pho.equals("帮助")){
                Intent helpIntent = new Intent(VoiceApplication.mContext, HorizonHelpActivity.class);
                helpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                VoiceApplication.mContext.startActivity(helpIntent);
            }
            else {
                DataSave.getInstance().Save(domain+"\t"+asrRes,Constant.SPEECH_TEXT);
                DataSave.getInstance().copyFromAssetsToSdcard(false,asrRes);
                SynthesizerPresenterImpl.getInstance().startSpeak("不好意思我还没有学会此功能", Constant.RE_TRY);
            }
        } else if (Cmd.equals("open")) {
            if (Vedio.equals("video")) {
                if (mDvrServiceManager.mDvrService != null) {
                    mDvrServiceManager.mDvrService.onVideoRecord();
                }else{
                    SynthesizerPresenterImpl.getInstance().startSpeak("行车记录仪未打开", Constant.CONTINUE_LISTEN);
                }
            }
            else if (Equipment.equals("navigate")){
                SynthesizerPresenterImpl.getInstance().startSpeak("你要去哪里？", Constant.NAVI_SCENE);
            }
            else {
                DataSave.getInstance().Save(domain+"\t"+asrRes,Constant.SPEECH_TEXT);
                DataSave.getInstance().copyFromAssetsToSdcard(false,asrRes);
                SynthesizerPresenterImpl.getInstance().startSpeak("不好意思我还没有学会此功能", Constant.RE_TRY);
            }
        }
        else if(Cmd.equals("close")) {
            if (Equipment.equals("navigate")) {
                if(mNaviServiceManager.mNavigationService!=null) {
                    mNaviServiceManager.mNavigationService.cancelNavigation();
                }
            }
            else {
                DataSave.getInstance().Save(domain+"\t"+asrRes,Constant.SPEECH_TEXT);
                DataSave.getInstance().copyFromAssetsToSdcard(false,asrRes);
                SynthesizerPresenterImpl.getInstance().startSpeak("不好意思我还没有学会此功能", Constant.RE_TRY);
            }
        }
        else {
            DataSave.getInstance().Save(domain+"\t"+asrRes,Constant.SPEECH_TEXT);
            DataSave.getInstance().copyFromAssetsToSdcard(false,asrRes);
            SynthesizerPresenterImpl.getInstance().startSpeak("不好意思我还没有学会此功能", Constant.RE_TRY);
        }
    }
}
