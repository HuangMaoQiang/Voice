package com.careyun.voiceassistant.voice.recognition.model.logic;

import android.content.Context;
import android.content.Intent;

import com.careyun.voiceassistant.app.VoiceApplication;
import com.careyun.voiceassistant.util.Constant;
import com.careyun.voiceassistant.view.activity.HorizonHelpActivity;
import com.careyun.voiceassistant.voice.recognition.model.clcmd.BluetoothCmd;
import com.careyun.voiceassistant.voice.recognition.model.clcmd.ScreenCmd;
import com.careyun.voiceassistant.voice.recognition.model.clcmd.WifiCmd;
import com.careyun.voiceassistant.voice.tts.presenter.SynthesizerPresenterImpl;
import com.ruiyi.cardvr.aidlservice.DvrServiceManager;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Huangmq on 2017/3/28.
 */

public class SettingLogic {

    private WifiCmd mWifi;
    //    private VoiceCmd mVoiceCmd;
    private ScreenCmd mScreenCmd;
    private BluetoothCmd mBluetoothCmd;
    public DvrServiceManager mDvrServiceManager;

    public SettingLogic() {

        mWifi = new WifiCmd(VoiceApplication.mContext);
//        mVoiceCmd = new VoiceCmd(mContext);
        mScreenCmd = new ScreenCmd(VoiceApplication.mContext);
        mBluetoothCmd = new BluetoothCmd();
        mDvrServiceManager = DvrServiceManager.getmInstance();


    }

    public void Logic(String asrRes, JSONObject command) throws JSONException {

        String mIntent = command.optString("intent");
        String Obj = command.optString("object");
        JSONObject r = new JSONObject(Obj);
        String mSettingtype = r.optString("settingtype");
        String module = r.optString("module");

        if (mIntent.equals("open")) {
            switch (module) {
                case "wifi":
                    mWifi.openWifi();
                    SynthesizerPresenterImpl.getInstance().startSpeak("WIFI已为您开启", Constant.STOP_LISTEN);
                    break;
                case "蓝牙":
                    mBluetoothCmd.turnOnBluetooth();
                    SynthesizerPresenterImpl.getInstance().startSpeak("蓝牙已为您打开", Constant.STOP_LISTEN);
                    break;
            }
        } else if (mIntent.equals("close")) {
            switch (module) {
                case "wifi":
                    mWifi.closeWifi();
                    SynthesizerPresenterImpl.getInstance().startSpeak("WIFI已为您关闭", Constant.STOP_LISTEN);
                    break;
                case "蓝牙":
                    mBluetoothCmd.turnOffBluetooth();
                    SynthesizerPresenterImpl.getInstance().startSpeak("蓝牙已为您关闭", Constant.STOP_LISTEN);
                    break;
                case "锁屏":
                    mScreenCmd.lockScreen();
                    SynthesizerPresenterImpl.getInstance().startSpeak("屏幕已为您关闭", Constant.STOP_LISTEN);
                    break;
            }

        } else if (mIntent.equals("set") && Obj != "{}") {
            switch (mSettingtype) {
                case "wifi_on":
                    mWifi.openWifi();
                    SynthesizerPresenterImpl.getInstance().startSpeak("WIFI已为您开启", Constant.STOP_LISTEN);
                    break;
                case "wifi_off":
                    mWifi.closeWifi();
                    SynthesizerPresenterImpl.getInstance().startSpeak("WIFI已为您关闭", Constant.STOP_LISTEN);
                    break;
                case "bluetooth_on":
                    mBluetoothCmd.turnOnBluetooth();
                    SynthesizerPresenterImpl.getInstance().startSpeak("蓝牙已为您打开", Constant.STOP_LISTEN);
                    break;
                case "bluetooth_off":
                    mBluetoothCmd.turnOffBluetooth();
                    SynthesizerPresenterImpl.getInstance().startSpeak("蓝牙已为您关闭", Constant.STOP_LISTEN);
                    break;
                case "data_on":
                    mWifi.openMobileNet();
                    SynthesizerPresenterImpl.getInstance().startSpeak("数据网络已打开", Constant.STOP_LISTEN);
                    break;
                case "data_off":
                    mWifi.closeMobileNet();
                    SynthesizerPresenterImpl.getInstance().startSpeak("数据网络已关闭", Constant.STOP_LISTEN);
                    break;
                case "lock_screen":
//                    mScreenCmd.lockScreen();
                    SynthesizerPresenterImpl.getInstance().startSpeak("屏幕亮度调节暂不支持", Constant.CONTINUE_LISTEN);
                    break;
                case "help":
                    Intent helpIntent = new Intent(VoiceApplication.mContext, HorizonHelpActivity.class);
                    helpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    VoiceApplication.mContext.startActivity(helpIntent);
                    break;
            }
        }
    }
}
