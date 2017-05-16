package com.careyun.voiceassistant.voice.recognition.model.logic;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.careyun.aidlfm.ServiceManager;
import com.careyun.aidlmusic.MusicServiceManager;
import com.careyun.voiceassistant.app.VoiceApplication;
import com.careyun.voiceassistant.util.Constant;
import com.careyun.voiceassistant.view.floatwindow.FloatWindowManager;
import com.careyun.voiceassistant.voice.recognition.model.clcmd.ScreenCmd;
import com.careyun.voiceassistant.voice.recognition.model.clcmd.VoiceCmd;
import com.careyun.voiceassistant.voice.tts.presenter.SynthesizerPresenterImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * Created by Huangmq on 2017/3/28.
 */

public class InstructionLogic {

    private VoiceCmd mVoiceCmd;
    private ScreenCmd mScreenCmd;
    private ServiceManager serviceManager;
    private MusicServiceManager musicServiceManager;
    private String Number = null;

    public InstructionLogic() {
        mVoiceCmd = new VoiceCmd(VoiceApplication.mContext);
        mScreenCmd = new ScreenCmd(VoiceApplication.mContext);
        serviceManager = ServiceManager.getmInstance();
        musicServiceManager = MusicServiceManager.getmInstance();
    }

    public void Logic(String asrRes, JSONObject command, String select) throws JSONException, IOException, RemoteException {
        String domain = command.optString("domain");
        String mIntent = command.optString("intent");
        String Obj = command.optString("object");
        JSONObject r = new JSONObject(Obj);
        Number = r.optString("option");
        String mOperate = r.optString("operate");
        if (Number != null) {
            switch (Number) {
                case "一":
                    Number = "1";
                    break;
                case "二":
                    Number = "2";
                    break;
                case "三":
                    Number = "3";
                    break;
                case "四":
                    Number = "4";
                    break;
                case "五":
                    Number = "5";
                    break;
                case "六":
                    Number = "6";
                    break;
                case "七":
                    Number = "7";
                    break;
                case "八":
                    Number = "8";
                    break;
                case "九":
                    Number = "9";
                    break;
            }
        }
        switch (mIntent) {
            case "quit":
                SynthesizerPresenterImpl.getInstance().startSpeak("再见", Constant.QUIT_VOICE);
                break;
            case "back":
                if (serviceManager.miTingFMService != null) {
                    serviceManager.miTingFMService.seletePrePage();
                    SynthesizerPresenterImpl.getInstance().startSpeak("你要听第几个", Constant.MUSIC_SCENE);
                } else if (musicServiceManager.iMusicService != null) {
                    musicServiceManager.iMusicService.seletePrePage();
                    SynthesizerPresenterImpl.getInstance().startSpeak("你要听第几个", Constant.MUSIC_SCENE);
                } else {
                    SynthesizerPresenterImpl.getInstance().startSpeak("播放器未打开，请打开后重试", Constant.CONTINUE_LISTEN);
                }
                break;
            case "next":
                if (serviceManager.miTingFMService != null) {
                    SynthesizerPresenterImpl.getInstance().startSpeak("你要听第几个", Constant.MUSIC_SCENE);
                    serviceManager.miTingFMService.seleteNextPage();
                } else if (musicServiceManager.iMusicService != null) {
                    SynthesizerPresenterImpl.getInstance().startSpeak("你要听第几个", Constant.MUSIC_SCENE);
                    musicServiceManager.iMusicService.seleteNextPage();
                } else {
                    SynthesizerPresenterImpl.getInstance().startSpeak("播放器未打开，请打开后重试", Constant.CONTINUE_LISTEN);
                }
                break;
            case "volume_up":
                mVoiceCmd.voiceRaise();
                SynthesizerPresenterImpl.getInstance().startSpeak("音量已为您调高", Constant.STOP_LISTEN);
                break;
            case "volume_down":
                mVoiceCmd.voiceDown();
                SynthesizerPresenterImpl.getInstance().startSpeak("音量已为您调低", Constant.STOP_LISTEN);
                break;
            case "volume_up_max":
                mVoiceCmd.voiceMax();
                SynthesizerPresenterImpl.getInstance().startSpeak("音量已为您条到最大", Constant.STOP_LISTEN);
                break;
            case "volume_down_min":
            case "mute":
                mVoiceCmd.voiceMin();
                SynthesizerPresenterImpl.getInstance().startSpeak("音量已为您条到最小", Constant.STOP_LISTEN);
                break;
            case "light_up":
//                mScreenCmd.setUpSystemBrightness();
                SynthesizerPresenterImpl.getInstance().startSpeak("屏幕亮度调节暂不支持", Constant.CONTINUE_LISTEN);
                break;
            case "light_down":
//                mScreenCmd.setDownSystemBrightness();
                SynthesizerPresenterImpl.getInstance().startSpeak("屏幕亮度调节暂不支持", Constant.CONTINUE_LISTEN);
                break;
            case "light_up_max":
//                mScreenCmd.setMaxSystemBrightness();
                SynthesizerPresenterImpl.getInstance().startSpeak("屏幕亮度调节暂不支持", Constant.CONTINUE_LISTEN);
                break;
            case "light_down_min":
//                mScreenCmd.setMinSystemBrightness();
                SynthesizerPresenterImpl.getInstance().startSpeak("屏幕亮度调节暂不支持", Constant.CONTINUE_LISTEN);
                break;
            case "select":
                if (select != null) {
                    if (select.equals("music")) {
                        if (serviceManager.miTingFMService != null) {
                            if (serviceManager.miTingFMService.seleteIndex(Integer.valueOf(Number))) {
                                SynthesizerPresenterImpl.getInstance().startSpeak("已为您播放", Constant.STOP_LISTEN);
                                Log.e(select, "收音机选择第" + Number + "个");
                            }
                        } else if (musicServiceManager.iMusicService != null) {
                            if (musicServiceManager.iMusicService.seleteIndex(Integer.valueOf(Number))) {
                                SynthesizerPresenterImpl.getInstance().startSpeak("已为您播放", Constant.STOP_LISTEN);
                                Log.e(select, "音乐播放器选择第" + Number + "个");
                            }
                        } else {
                            SynthesizerPresenterImpl.getInstance().startSpeak("播放器未打开，请打开后重试", Constant.CONTINUE_LISTEN);
                        }
                    } else if (select.equals("map")) {
                        VoiceApplication.callback(0,Number);
                    }
                } else {
//                    DataSave.getInstance(mContext).Save(domain+"\t"+asrRes,Constant.SPEECH_TEXT);
//                    DataSave.getInstance(mContext).copyFromAssetsToSdcard(false,asrRes);
                    SynthesizerPresenterImpl.getInstance().startSpeak("不好意思,我没听懂请重试", Constant.RE_TRY);
                }
                break;
            //////offline/////////////////////////////////////////////////////////////
            case "volume":
                switch (mOperate) {
                    case "调高":
                    case "调大":
                        mVoiceCmd.voiceRaise();
                        SynthesizerPresenterImpl.getInstance().startSpeak("音量已为您调高", Constant.STOP_LISTEN);
                        break;
                    case "调低":
                    case "调小":
                        mVoiceCmd.voiceDown();
                        SynthesizerPresenterImpl.getInstance().startSpeak("音量已为您调第", Constant.STOP_LISTEN);
                        break;
                    case "最大":
                    case "最高":
                        mVoiceCmd.voiceMax();
                        SynthesizerPresenterImpl.getInstance().startSpeak("音量已为您条到最大", Constant.STOP_LISTEN);
                        break;
                    case "最小":
                    case "最低":
                        mVoiceCmd.voiceMin();
                        SynthesizerPresenterImpl.getInstance().startSpeak("音量已为您条到最低", Constant.STOP_LISTEN);
                        break;
                    default:
                        SynthesizerPresenterImpl.getInstance().startSpeak("不好意思我还不会，请从说", Constant.RE_TRY);
                }
                break;
            case "screen":
                switch (mOperate) {
                    case "调高":
                    case "调大":
//                        mScreenCmd.setUpSystemBrightness();
                        SynthesizerPresenterImpl.getInstance().startSpeak("屏幕亮度调节暂不支持", Constant.CONTINUE_LISTEN);
                        break;
                    case "调低":
                    case "调小":
//                        mScreenCmd.setDownSystemBrightness();
                        SynthesizerPresenterImpl.getInstance().startSpeak("屏幕亮度调节暂不支持", Constant.CONTINUE_LISTEN);
                        break;
                    case "最大":
                    case "最高":
//                        mScreenCmd.setMaxSystemBrightness();
                        SynthesizerPresenterImpl.getInstance().startSpeak("屏幕亮度调节暂不支持", Constant.CONTINUE_LISTEN);
                        break;
                    case "最小":
                    case "最低":
//                        mScreenCmd.setMinSystemBrightness();
                        SynthesizerPresenterImpl.getInstance().startSpeak("屏幕亮度调节暂不支持", Constant.CONTINUE_LISTEN);
                        break;
                    default:
//                        DataSave.getInstance(mContext).Save(domain+"\t"+asrRes,Constant.SPEECH_TEXT);
//                        DataSave.getInstance(mContext).copyFromAssetsToSdcard(false,asrRes);
                        SynthesizerPresenterImpl.getInstance().startSpeak("不好意思我还没有学会此功能", Constant.RE_TRY);
                }
                break;
            case "change":
                if (serviceManager.miTingFMService != null) {
                    if (serviceManager.miTingFMService.seleteNextPage()) {
                        SynthesizerPresenterImpl.getInstance().startSpeak("你要听第几个", Constant.MUSIC_SCENE);
                        Log.e(select, "收音机翻页");
                    }
                } else if (musicServiceManager.iMusicService != null) {
                    if (musicServiceManager.iMusicService.seleteNextPage()) {
                        SynthesizerPresenterImpl.getInstance().startSpeak("你要听第几个", Constant.MUSIC_SCENE);
                        Log.e(select, "音乐播放器翻页");
                    }
                } else {
                    SynthesizerPresenterImpl.getInstance().startSpeak("播放器未打开，请打开后重试", Constant.CONTINUE_LISTEN);
                }
                break;
            default:
//                DataSave.getInstance(mContext).Save(domain+"\t"+asrRes,Constant.SPEECH_TEXT);
//                DataSave.getInstance(mContext).copyFromAssetsToSdcard(false,asrRes);
                SynthesizerPresenterImpl.getInstance().startSpeak("不好意思我还没有学会此功能", Constant.RE_TRY);
        }
    }
}
