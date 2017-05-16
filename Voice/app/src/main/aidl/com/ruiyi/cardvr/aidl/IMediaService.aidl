// IMediaService.aidl
package com.ruiyi.cardvr.aidl;

// Declare any non-default types here with import statements

interface IMediaService {

    //抓拍图片
    void onTakePhoto();
    //视频录制
    void onVideoRecord();
    //是否开启Adas功能
    void setAdas(boolean isAdas);
    //停止行车记录仪
    boolean stopMedia();

    /**
     * 停止录像
     */
    boolean stopRecord();
    /**
     *开始录像
     */
    boolean startRecord();

    //控制音量(0.0到1.0)
    void setVolume(float leftVolume, float rightVolume);
}
