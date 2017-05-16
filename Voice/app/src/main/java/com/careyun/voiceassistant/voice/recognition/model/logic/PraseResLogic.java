package com.careyun.voiceassistant.voice.recognition.model.logic;

import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import com.careyun.aidlfm.ServiceManager;
import com.careyun.aidlmusic.MusicServiceManager;
import com.careyun.voiceassistant.app.VoiceApplication;
import com.careyun.voiceassistant.util.Constant;
import com.careyun.voiceassistant.util.readTxtFile;
import com.careyun.voiceassistant.view.activity.HorizonHelpActivity;
import com.careyun.voiceassistant.voice.recognition.model.clcmd.ScreenCmd;
import com.careyun.voiceassistant.voice.recognition.model.clcmd.VoiceCmd;
import com.careyun.voiceassistant.voice.tts.presenter.SynthesizerPresenterImpl;
import com.ruiyi.cardvr.aidlservice.DvrServiceManager;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Huangmq on 2017/4/18.
 */

public class PraseResLogic {
    private ServiceManager serviceManager;
    private MusicServiceManager musicServiceManager;
    private VoiceCmd mVoiceCmd;
    private ScreenCmd mScreenCmd;
    public DvrServiceManager mDvrServiceManager;
    private readTxtFile mreadTxtFile;
    private ArrayList<String> ReadRes;
    private String match;

    public PraseResLogic() {
        serviceManager = ServiceManager.getmInstance();
        musicServiceManager = MusicServiceManager.getmInstance();
        mreadTxtFile = new readTxtFile(VoiceApplication.mContext);
        ReadRes = new ArrayList<String>();
        mVoiceCmd = new VoiceCmd(VoiceApplication.mContext);
        mScreenCmd = new ScreenCmd(VoiceApplication.mContext);
        mDvrServiceManager = DvrServiceManager.getmInstance();
    }

    public String Logic(String asrRes) throws RemoteException {
        ReadRes = mreadTxtFile.readFromAssets();
        for (int i = 0; i < ReadRes.size(); i++) {
            match = main(ReadRes.get(i), asrRes);
            if (match != null) {
                Log.e("找到匹配结果：", match);
                switch (match) {
                    case "导航":
                        SynthesizerPresenterImpl.getInstance().startSpeak("你要去哪里？", Constant.NAVI_SCENE);
                        break;
                    case "音乐":
                        if (musicServiceManager.iMusicService != null) {
                            musicServiceManager.iMusicService.playStart();
                            SynthesizerPresenterImpl.getInstance().startSpeak("开始播放", Constant.STOP_LISTEN);
                        } else {
                            SynthesizerPresenterImpl.getInstance().startSpeak("播放器未打开，请打开后重试", Constant.CONTINUE_LISTEN);
                        }
                        break;
                    case "开始":
                    case "播放":
                        if (serviceManager.miTingFMService != null) {
                            if (serviceManager.miTingFMService.playStart()) {
                                SynthesizerPresenterImpl.getInstance().startSpeak("开始播放", Constant.STOP_LISTEN);
                            }
                        } else if (musicServiceManager.iMusicService != null) {
                            musicServiceManager.iMusicService.playStart();
                            SynthesizerPresenterImpl.getInstance().startSpeak("开始播放", Constant.STOP_LISTEN);
                        } else {
                            SynthesizerPresenterImpl.getInstance().startSpeak("播放器未打开，请打开后重试", Constant.CONTINUE_LISTEN);
                        }
                        break;
                    case "暂停":
                    case "停止":
                        if (serviceManager.miTingFMService != null) {
                            if (serviceManager.miTingFMService.playPause()) {
                                SynthesizerPresenterImpl.getInstance().startSpeak("已经暂停播放", Constant.STOP_LISTEN);
                            }
                        } else if (musicServiceManager.iMusicService != null) {
                            musicServiceManager.iMusicService.playPause();
                            SynthesizerPresenterImpl.getInstance().startSpeak("已经暂停播放", Constant.STOP_LISTEN);
                        } else {
                            SynthesizerPresenterImpl.getInstance().startSpeak("播放器未打开，请打开后重试", Constant.CONTINUE_LISTEN);
                        }
                        break;
                    case "录像":
                        if (mDvrServiceManager.mDvrService != null) {
                            if (mDvrServiceManager.mDvrService.startRecord()) {
                                SynthesizerPresenterImpl.getInstance().startSpeak("开始录像", Constant.STOP_LISTEN);
                            }
                        } else {
                            SynthesizerPresenterImpl.getInstance().startSpeak("行车记录仪未打开", Constant.CONTINUE_LISTEN);
                        }
                        break;
                    case "拍照":
                        if (mDvrServiceManager.mDvrService != null) {
                            mDvrServiceManager.mDvrService.onTakePhoto();
                        } else {
                            SynthesizerPresenterImpl.getInstance().startSpeak("行车记录仪未打开", Constant.CONTINUE_LISTEN);
                        }
                        break;
                    case "打开":
                        SynthesizerPresenterImpl.getInstance().startSpeak("请问你需要打开什么软件", Constant.CONTINUE_LISTEN);
                        break;
                    case "调大":
                        mVoiceCmd.voiceRaise();
                        SynthesizerPresenterImpl.getInstance().startSpeak("音量以为您调大", Constant.STOP_LISTEN);
                        break;
                    case "调小":
                        mVoiceCmd.voiceDown();
                        SynthesizerPresenterImpl.getInstance().startSpeak("音量已为您调小", Constant.STOP_LISTEN);
                        break;
                    case "调亮":
                        SynthesizerPresenterImpl.getInstance().startSpeak("屏幕亮度调节暂不支持", Constant.CONTINUE_LISTEN);
                        break;
                    case "调暗":
                        SynthesizerPresenterImpl.getInstance().startSpeak("屏幕亮度调节暂不支持", Constant.CONTINUE_LISTEN);
                        break;
                    case "关闭":
                        if (mDvrServiceManager.mDvrService != null) {
                            try {
                                mDvrServiceManager.mDvrService.setAdas(true);
                            } catch (RemoteException mE) {
//                                WakeUp.getInstance(mContext).start();
                                mE.printStackTrace();
                            }
                        } else {
                            SynthesizerPresenterImpl.getInstance().startSpeak("行车记录仪未打开", Constant.CONTINUE_LISTEN);
                        }
                        break;
                    case "安全":
                        if (mDvrServiceManager.mDvrService != null) {
                            try {
                                mDvrServiceManager.mDvrService.setAdas(true);
                            } catch (RemoteException mE) {
//                                WakeUp.getInstance(mContext).start();
                                mE.printStackTrace();
                            }
                        } else {
                            SynthesizerPresenterImpl.getInstance().startSpeak("行车记录仪未打开", Constant.CONTINUE_LISTEN);
                        }
                        break;
                    case "帮助":
                        Intent helpIntent = new Intent(VoiceApplication.mContext, HorizonHelpActivity.class);
                        helpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        VoiceApplication.mContext.startActivity(helpIntent);
                        break;
                    case "退出":
                    case "取消":
                        SynthesizerPresenterImpl.getInstance().startSpeak("再见", Constant.QUIT_VOICE);
                        break;
                    case "上一首":
                        if (serviceManager.miTingFMService != null) {
                            if (serviceManager.miTingFMService.playPre()) {
                                SynthesizerPresenterImpl.getInstance().startSpeak("已选择", Constant.STOP_LISTEN);
                            }
                        } else if (musicServiceManager.iMusicService != null) {
                            if (musicServiceManager.iMusicService.playPre()) {
                            }
                        } else {
                            SynthesizerPresenterImpl.getInstance().startSpeak("播放器未打开，请打开后重试", Constant.CONTINUE_LISTEN);
                        }
                        break;
                    case "下一首":
                        if (serviceManager.miTingFMService != null) {
                            if (serviceManager.miTingFMService.playNext()) {
                                SynthesizerPresenterImpl.getInstance().startSpeak("已选择", Constant.STOP_LISTEN);
                            }
                        } else if (musicServiceManager.iMusicService != null) {
                            if (musicServiceManager.iMusicService.playNext()) {
                                SynthesizerPresenterImpl.getInstance().startSpeak("已选择", Constant.STOP_LISTEN);
                            }
                        } else {
                            SynthesizerPresenterImpl.getInstance().startSpeak("播放器未打开，请打开后重试", Constant.CONTINUE_LISTEN);
                        }
                        break;
                    case "上一页":
                        if (serviceManager.miTingFMService != null) {
                            if (serviceManager.miTingFMService.seletePrePage()) {
                                SynthesizerPresenterImpl.getInstance().startSpeak("你要听第几个", Constant.MUSIC_SCENE);
                            }
                        } else if (musicServiceManager.iMusicService != null) {
                            if (musicServiceManager.iMusicService.seletePrePage()) {
                                SynthesizerPresenterImpl.getInstance().startSpeak("你要听第几个", Constant.MUSIC_SCENE);
                            }
                        } else {
                            SynthesizerPresenterImpl.getInstance().startSpeak("播放器未打开，请打开后重试", Constant.CONTINUE_LISTEN);
                        }
                        break;
                    case "下一页":
                        if (serviceManager.miTingFMService != null) {
                            if (serviceManager.miTingFMService.seleteNextPage()) {
                                SynthesizerPresenterImpl.getInstance().startSpeak("你要听第几个", Constant.MUSIC_SCENE);
                            }
                        } else if (musicServiceManager.iMusicService != null) {
                            if (musicServiceManager.iMusicService.seleteNextPage()) {
                                SynthesizerPresenterImpl.getInstance().startSpeak("你要听第几个", Constant.MUSIC_SCENE);
                            }
                        } else {
                            SynthesizerPresenterImpl.getInstance().startSpeak("播放器未打开，请打开后重试", Constant.CONTINUE_LISTEN);
                        }
                        break;
                    case "调大音量":
                    case "调高音量":
                    case "音量调大":
                    case "音量调高":
                        mVoiceCmd.voiceRaise();
                        SynthesizerPresenterImpl.getInstance().startSpeak("音量已为您调高", Constant.STOP_LISTEN);
                        break;
                    case "调小音量":
                    case "调低音量":
                    case "音量调小":
                    case "音量调低":
                        mVoiceCmd.voiceDown();
                        SynthesizerPresenterImpl.getInstance().startSpeak("音量已为您调低", Constant.STOP_LISTEN);
                        break;
                }
//                MyApplication.callback(Constant.UPDATE_MYRESULT_MASSAGE, match);
                return match;
            } else {
                continue;
            }
        }
        return null;
    }


    public String main(String regEx, String str) {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find()) {
//            Log.e("结果结果：",m.group());
            return m.group();
        } else {
//           Log.e("结果结果：","没找到"+regEx);
            return null;
        }
    }

}
