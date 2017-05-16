package com.careyun.voiceassistant.voice.recognition.model.logic;

import android.content.Context;
import android.os.RemoteException;
import android.text.TextUtils;

import com.careyun.aidlfm.ServiceManager;
import com.careyun.aidlmusic.MusicServiceManager;
import com.careyun.voiceassistant.app.VoiceApplication;
import com.careyun.voiceassistant.util.Constant;
import com.careyun.voiceassistant.voice.recognition.model.clcmd.AppInfoService;
import com.careyun.voiceassistant.voice.tts.presenter.SynthesizerPresenterImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * Created by Huangmq on 2017/4/6.
 */

public class MusicLogic {

    private Context mContext;
    private ServiceManager serviceManager;
    private MusicServiceManager musicServiceManager;
    private AppInfoService mAppInfoService;
    private String name;
    private String byartist;
    private String type;
    private String tv;
    private String genre;
    private String appName ="音乐播放器";

    public MusicLogic( ){
        serviceManager = ServiceManager.getmInstance();
        musicServiceManager = MusicServiceManager.getmInstance();
        mAppInfoService = new AppInfoService(VoiceApplication.mContext);
    }

    public void Logic(String asrRes,JSONObject command) throws JSONException, IOException, RemoteException {
        String domain = command.optString("domain");
        String mIntent = command.optString("intent");
        String Obj = command.optString("object");
        JSONObject r = new JSONObject(Obj);
//        JSONArray tempname = r.optJSONArray("name");
        JSONArray tempbyartist = r.optJSONArray("byartist");
        name = r.optString("name");
        type =r.optString("type");
        tv = r.optString("tv");
        genre = r.optString("genre");
        if (tempbyartist!=null){
            byartist = tempbyartist.getString(0);
        }
        else {
            byartist = "";
        }
//        String mOperate = r.optString("operate");
        switch (mIntent) {
            case "play":
                if(musicServiceManager.iMusicService!= null ){
                    if(!TextUtils.isEmpty(name)||!TextUtils.isEmpty(byartist)){
                        if (musicServiceManager.iMusicService.setSearchTracks(byartist+name,true)){
                        SynthesizerPresenterImpl.getInstance().startSpeak("已为您播放", Constant.STOP_LISTEN);
                        }
                    }
                    else if(TextUtils.isEmpty(name) && TextUtils.isEmpty(byartist)){
                    if (!musicServiceManager.iMusicService.playStart()){
                        SynthesizerPresenterImpl.getInstance().startSpeak("开始播放", Constant.STOP_LISTEN);
                    }
                    else if(!TextUtils.isEmpty(genre) && !TextUtils.isEmpty(tv)) {
                        if (musicServiceManager.iMusicService.setSearchTracks(tv + genre, true)) {
                            SynthesizerPresenterImpl.getInstance().startSpeak("开始播放", Constant.STOP_LISTEN);
                        }
                    }
                }
            }else {
                    if(type!=null && type.equals("歌曲")) {
                        mAppInfoService.OpenApp(appName);
                        SynthesizerPresenterImpl.getInstance().startSpeak("播放器已为您打开,是否开始播放", Constant.CONTINUE_LISTEN);
                    }
                    else {
                        SynthesizerPresenterImpl.getInstance().startSpeak("音乐播放器未打开，请重试", Constant.CONTINUE_LISTEN);
                    }
            }
                break;
            case "search":
                if(musicServiceManager.iMusicService!= null ){
                    if(!TextUtils.isEmpty(name)||!TextUtils.isEmpty(byartist)){
                        if (musicServiceManager.iMusicService.setSearchTracks(byartist+name,false)){
                            SynthesizerPresenterImpl.getInstance().startSpeak("找到以下歌曲,你要听第几个", Constant.MUSIC_SCENE);
                        }
                    }
                } else {
                    SynthesizerPresenterImpl.getInstance().startSpeak("播放器未打开，请打开后重试", Constant.RE_TRY);
                }
                break;
        }
    }
}
