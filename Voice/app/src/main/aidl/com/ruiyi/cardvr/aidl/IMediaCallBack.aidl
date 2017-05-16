// IMediaCallBack.aidl
package com.ruiyi.cardvr.aidl;

// Declare any non-default types here with import statements

interface IMediaCallBack {
    /**
     *图片抓拍回调函数，aPath不等于空，表示抓拍成功；否则反之。
     *
     */
    void TakePhotoCallBack(String aPath);
    /**
     *小视频录制回调函数，aPath不等于空，表示抓拍成功；否则反之。
     *
     */
    void TakeVideoCallBack(String aPath);
    /**
     *
     *Adas功能设置回调函数，isAdas是否开启成功
     *
     */
    void AdasCallBack(boolean isAdas);
    /**
     *
     * 语音播报
     *
     */
    void onSpeech(String mString);

}
