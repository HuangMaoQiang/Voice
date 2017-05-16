package com.careyun.voiceassistant.voice.recognition.model.clcmd;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by Huangmq on 2017/1/19.
 * 控制手机音量大小
 */

public class VoiceCmd {

    //定义 AudioManager对象
    private  AudioManager mAudioManager;

    //构造器
    public VoiceCmd(Context mContext)
    {
        //获取 AudioManager对象
        mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    //调高媒体音量
    public void voiceRaise()
    {
        //mAudioManager.adjustVolume(AudioManager.ADJUST_LOWER,  0);
        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,AudioManager.FLAG_PLAY_SOUND
                | AudioManager.FLAG_SHOW_UI);
    }
    //调低媒体音量
    public void voiceDown()
    {
        //mAudioManager.adjustVolume(AudioManager.ADJUST_RAISE,  0);
        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,AudioManager.FLAG_PLAY_SOUND
                | AudioManager.FLAG_SHOW_UI);
    }
    //最大媒体音量
    public void voiceMax()
    {
        //mAudioManager.adjustVolume(AudioManager.ADJUST_RAISE,  0);
        int maxVol = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,maxVol,AudioManager.FLAG_PLAY_SOUND
                | AudioManager.FLAG_SHOW_UI);
    }
    //最小媒体音量
    public void voiceMin()
    {
        //mAudioManager.adjustVolume(AudioManager.ADJUST_RAISE,  0);\
        int minVol = 0;
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,minVol,AudioManager.FLAG_PLAY_SOUND
                | AudioManager.FLAG_SHOW_UI);
    }



}
