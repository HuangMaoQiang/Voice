package com.careyun.voiceassistant.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.careyun.voiceassistant.R;
import com.careyun.voiceassistant.eventbus.RecogEvent;
import com.careyun.voiceassistant.util.Constant;
import com.careyun.voiceassistant.view.activity.ActivityCollerctor;
import com.careyun.voiceassistant.view.activity.MainActivity;
import com.careyun.voiceassistant.view.floatwindow.FloatWindowManager;
import com.careyun.voiceassistant.view.floatwindow.FloatWindowSmallView;
import com.careyun.voiceassistant.voice.recognition.presenter.IRecognitionPresenter;
import com.careyun.voiceassistant.voice.recognition.presenter.RecognitionPresenterImpl;
import com.careyun.voiceassistant.voice.recognition.view.IRecognitionView;
import com.careyun.voiceassistant.voice.tts.presenter.ISynthesizerPresenter;
import com.careyun.voiceassistant.voice.tts.presenter.SynthesizerPresenterImpl;
import com.careyun.voiceassistant.voice.tts.view.ISynthesizerView;
import com.careyun.voiceassistant.voice.wakeup.presenter.IWakePresenter;
import com.careyun.voiceassistant.voice.wakeup.presenter.WakeupPresenterImpl;
import com.careyun.voiceassistant.voice.wakeup.view.IWakeUpView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class VoiceService extends Service implements IWakeUpView, IRecognitionView,ISynthesizerView{

    private final String TAG = "VoiceService";
    private final String TMPLE_AUDIO_FILE = "temp.pcm";
    private IWakePresenter iWakePresenter;
    private IRecognitionPresenter iRecognitionPresenter;
    private ISynthesizerPresenter iSynthesizerPresenter;
    private FloatWindowManager floatWindowManager;
    private FloatWindowSmallView mSmallView;


    public VoiceService() {
    }

    Intent mIntent = new Intent();
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listenEvent(RecogEvent recongEvent){
        switch (recongEvent.getMassage()){
            case Constant.ONLINE_LISTEN_MASSAGE:
                iWakePresenter.stopWakeup();
                iRecognitionPresenter.startListening(mIntent);
                break;
            case Constant.RE_TRY_MASSAGE:
                iWakePresenter.stopWakeup();
                iRecognitionPresenter.startListening(mIntent);
                break;
            case Constant.STOP_LISTEN_MASSAGE:
                iWakePresenter.setVolumeUp();
                iWakePresenter.startWakeup();
                iRecognitionPresenter.sendSCENNE(null);
                ActivityCollerctor.finishAll();
                break;
            case Constant.HELP_SPEECH_MASSAGE:
                iWakePresenter.startWakeup();
                iWakePresenter.setVolumeUp();
                iRecognitionPresenter.sendSCENNE(null);
                break;
            case Constant.MUSIC_SCENE_MASSAGE:
                iWakePresenter.setVolumeDown();
                iWakePresenter.stopWakeup();
                iRecognitionPresenter.startListening(mIntent);
                iRecognitionPresenter.sendSCENNE("music");
                break;
            case Constant.NAVI_SCENE_MASSAGE:
                iWakePresenter.setVolumeDown();
                iWakePresenter.stopWakeup();
                iRecognitionPresenter.startListening(mIntent);
                iRecognitionPresenter.sendSCENNE("map");
                break;
            case Constant.UPDATE_MYRESULT_MASSAGE:
                mSmallView.updataMyText(recongEvent.getMatch());
                mSmallView.updatabaiduText(null);
                break;
            case Constant.QUIT_VOICE_MASSAGE:
                iWakePresenter.setVolumeUp();
                iWakePresenter.startWakeup();
                iRecognitionPresenter.sendSCENNE(null);
                floatWindowManager.removeAll();
                ActivityCollerctor.finishAll();
                break;
        }
    }



    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        iWakePresenter = new WakeupPresenterImpl(this);
        iRecognitionPresenter = new RecognitionPresenterImpl(this);
        iSynthesizerPresenter = SynthesizerPresenterImpl.getInstance();
        floatWindowManager = FloatWindowManager.getInstance(this);
//        mSmallView = floatWindowManager.createSmallWindow(this);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("卡耳云语音助手")
                .setContentText("正在运行...")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.logo)
                .setContentIntent(pi)
                .build();
        startForeground(1, notification);

        iSynthesizerPresenter.initTTS();
        iSynthesizerPresenter.startSpeak("语音功能已开启","100");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        iWakePresenter.startWakeup();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 唤醒成功回调
     */
    @Override
    public void wakeupSuccess() {
        Log.e(TAG, "唤醒成功");
        mSmallView = floatWindowManager.createSmallWindow(this);
        iWakePresenter.stopWakeup();
        iWakePresenter.setVolumeDown();
        iSynthesizerPresenter.initTTS();
        iRecognitionPresenter.bindParams(mIntent);
        iRecognitionPresenter.startListening(mIntent);
    }
    /**
     * 唤醒失败回调
     */
    @Override
    public void wakeupFail() {
//        Log.e(TAG, "唤醒失败");
    }

    /**
     * 识别结果显示
     */
    @Override
    public void showResult(String result) {
        Log.e(TAG, result);
        mSmallView.updatabaiduText(result);
        mSmallView.updataMyText(null);
    }

    @Override
    public void showMyResult(String myRes) {
        Log.e(TAG, myRes);
        mSmallView.updataMyText(myRes);
        mSmallView.updatabaiduText(null);
    }

    @Override
    public void showNone() {
        Log.e(TAG, "无解析结果");
        mSmallView.updataMyText(null);
        mSmallView.updatabaiduText(null);
    }

    /**
     * 临时识别结果显示
     */
    @Override
    public void showPartialResult(String partial_result) {
        Log.e(TAG, partial_result);
        mSmallView.updataText(partial_result);
    }

    /**
     * 识别错误信息显示
     */
    @Override
    public void showFailedError(int error_massage) {
        Log.e(TAG, String.valueOf(error_massage));
        iWakePresenter.startWakeup();
        ActivityCollerctor.finishAll();
    }


    @Override
    public void onDestroy() {
        iRecognitionPresenter.onDestroy();
        iWakePresenter.onDestroy();
        iSynthesizerPresenter.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showError() {

    }
}
