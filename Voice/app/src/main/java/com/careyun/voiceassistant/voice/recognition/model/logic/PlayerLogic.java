package com.careyun.voiceassistant.voice.recognition.model.logic;

import android.os.RemoteException;

import com.careyun.aidlfm.ServiceManager;
import com.careyun.aidlmusic.MusicServiceManager;
import com.careyun.voiceassistant.util.Constant;
import com.careyun.voiceassistant.voice.recognition.model.clcmd.DataSave;
import com.careyun.voiceassistant.voice.tts.presenter.SynthesizerPresenterImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
/**
 * Created by Huangmq on 2017/4/1.
 */

public class PlayerLogic {
    private ServiceManager serviceManager;
    private MusicServiceManager musicServiceManager;

    public PlayerLogic(){
        serviceManager = ServiceManager.getmInstance();
        musicServiceManager = MusicServiceManager.getmInstance();
    }

    public void Logic(String asrRes,JSONObject command) throws JSONException, RemoteException, IOException {
        String domain = command.optString("domain");
        String Cmd = command.optString("intent");
        String Obj = command.optString("object");
        JSONObject r = new JSONObject(Obj);
        String action = r.optString("action_type");
        switch (Cmd){
            case "next":
                if(serviceManager.miTingFMService!= null){
                    if (serviceManager.miTingFMService.playNext()){
                        SynthesizerPresenterImpl.getInstance().startSpeak("已选择", Constant.STOP_LISTEN);
                    }else {
                        SynthesizerPresenterImpl.getInstance().startSpeak("已选择", Constant.STOP_LISTEN);
                    }
                }else if(musicServiceManager.iMusicService!=null){
                    if (musicServiceManager.iMusicService.playNext()){
                        SynthesizerPresenterImpl.getInstance().startSpeak("已选择", Constant.STOP_LISTEN);
                    }else {
                        SynthesizerPresenterImpl.getInstance().startSpeak("已选择", Constant.STOP_LISTEN);
                    }
                } else {
                    SynthesizerPresenterImpl.getInstance().startSpeak("播放器未打开，请打开后重试", Constant.CONTINUE_LISTEN);
                }
                break;
            case "previous":
                if(serviceManager.miTingFMService!= null){
                    if (serviceManager.miTingFMService.playPre()){
                        SynthesizerPresenterImpl.getInstance().startSpeak("已选择", Constant.STOP_LISTEN);
                    }else {
                        SynthesizerPresenterImpl.getInstance().startSpeak("已选择", Constant.STOP_LISTEN);
                    }
                }else if(musicServiceManager.iMusicService!=null){
                    if (musicServiceManager.iMusicService.playPre()){
                        SynthesizerPresenterImpl.getInstance().startSpeak("已选择", Constant.STOP_LISTEN);
                    }else {
                        SynthesizerPresenterImpl.getInstance().startSpeak("已选择", Constant.STOP_LISTEN);
                    }
                }else {
                    SynthesizerPresenterImpl.getInstance().startSpeak("播放器未打开，请打开后重试", Constant.CONTINUE_LISTEN);
                }
                break;
            case "pause":
                if(serviceManager.miTingFMService!= null){
                    if (serviceManager.miTingFMService.playPause()){
                        SynthesizerPresenterImpl.getInstance().startSpeak("已经暂停播放", Constant.STOP_LISTEN);
                    }
                }else if(musicServiceManager.iMusicService!=null){
                    musicServiceManager.iMusicService.playPause();
                    SynthesizerPresenterImpl.getInstance().startSpeak("已经暂停播放", Constant.STOP_LISTEN);
/*                    if (musicServiceManager.iMusicService.playPause()){
                        TestService.getInstance().speak("已经暂停播放", Constant.STOP_LISTEN);
                    }*/
                }else {
                    SynthesizerPresenterImpl.getInstance().startSpeak("播放器未打开，请打开后重试", Constant.CONTINUE_LISTEN);
                }
                break;
            case "play":
                if(serviceManager.miTingFMService!= null){
                    if (serviceManager.miTingFMService.playStart()){
                        SynthesizerPresenterImpl.getInstance().startSpeak("开始播放", Constant.STOP_LISTEN);
                    }
                }else if(musicServiceManager.iMusicService!=null){
                    musicServiceManager.iMusicService.playStart();
                    SynthesizerPresenterImpl.getInstance().startSpeak("开始播放", Constant.STOP_LISTEN);
             /*       if (musicServiceManager.iMusicService.playStart()){
                        TestService.getInstance().speak("开始播放", Constant.STOP_LISTEN);
                    }*/
                }else {
                    SynthesizerPresenterImpl.getInstance().startSpeak("播放器未打开，请打开后重试", Constant.CONTINUE_LISTEN);
                }
                break;
            case "exitplayer":
                if(musicServiceManager.iMusicService!=null){
                    if (musicServiceManager.iMusicService.playFinish()){
                        SynthesizerPresenterImpl.getInstance().startSpeak("已为您退出播放器", Constant.STOP_LISTEN);
                    }
                }else if(serviceManager.miTingFMService!= null){
                    if (serviceManager.miTingFMService.playFinish()){
                        SynthesizerPresenterImpl.getInstance().startSpeak("已为您退出播放器", Constant.STOP_LISTEN);
                    }
                }else {
                    SynthesizerPresenterImpl.getInstance().startSpeak("播放器未打开，请打开后重试", Constant.CONTINUE_LISTEN);
                }
                break;
            default:
                DataSave.getInstance().Save(domain + "\t" + asrRes, Constant.SPEECH_TEXT);
                DataSave.getInstance().copyFromAssetsToSdcard(false,asrRes);
                SynthesizerPresenterImpl.getInstance().startSpeak("不好意思我还不会此功能", Constant.RE_TRY);
        }
    }

}
