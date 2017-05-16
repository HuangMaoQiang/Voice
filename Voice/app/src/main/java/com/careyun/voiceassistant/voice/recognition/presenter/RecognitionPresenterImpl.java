package com.careyun.voiceassistant.voice.recognition.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.speech.SpeechRecognizer;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.speech.VoiceRecognitionService;
import com.careyun.aidlfm.IFMCallback;
import com.careyun.aidlfm.ServiceManager;
import com.careyun.aidlmusic.IMusicCallback;
import com.careyun.aidlmusic.MusicServiceManager;
import com.careyun.voiceassistant.R;
import com.careyun.voiceassistant.app.VoiceApplication;
import com.careyun.voiceassistant.map.Search.SuggestSearch;
import com.careyun.voiceassistant.map.Search.SuggestSearchCallBack;
import com.careyun.voiceassistant.util.Constant;
import com.careyun.voiceassistant.voice.recognition.model.RecognitionService;
import com.careyun.voiceassistant.voice.recognition.model.clcmd.DataSave;
import com.careyun.voiceassistant.voice.recognition.model.logic.AppLogic;
import com.careyun.voiceassistant.voice.recognition.model.logic.CustomLogic;
import com.careyun.voiceassistant.voice.recognition.model.logic.InstructionLogic;
import com.careyun.voiceassistant.voice.recognition.model.logic.MapLogic;
import com.careyun.voiceassistant.voice.recognition.model.logic.MusicLogic;
import com.careyun.voiceassistant.voice.recognition.model.logic.NavigationLogic;
import com.careyun.voiceassistant.voice.recognition.model.logic.PlayerLogic;
import com.careyun.voiceassistant.voice.recognition.model.logic.PraseResLogic;
import com.careyun.voiceassistant.voice.recognition.model.logic.RadioLogic;
import com.careyun.voiceassistant.voice.recognition.model.logic.SettingLogic;
import com.careyun.voiceassistant.voice.recognition.model.logic.VedioLogic;
import com.careyun.voiceassistant.voice.recognition.model.logic.Vehicle_InstuctionLogic;
import com.careyun.voiceassistant.voice.recognition.model.logic.WeatherLogic;
import com.careyun.voiceassistant.voice.recognition.view.IRecognitionView;
import com.careyun.voiceassistant.voice.tts.presenter.ISynthesizerPresenter;
import com.careyun.voiceassistant.voice.tts.presenter.SynthesizerPresenterImpl;
import com.careyun.voiceassistant.voice.wakeup.presenter.WakeupPresenterImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Huangmq on 2017/4/12.
 */

public class RecognitionPresenterImpl extends RecognitionService implements IRecognitionPresenter {

    private final String TAG = "RecognitionPresenter";
    private SharedPreferences mSharedPreferences;
    private SpeechRecognizer mSpeechRecognizer;
    private ISynthesizerPresenter iSynthesizerPresenter;
    private IRecognitionView iRecognitionView;
    private DataSave dataSave;
    public String SCENNE = null;
    private SharedPreferences LocationInfo;
    private SuggestSearch suggestSearch;
    private String Mylocation;
    private final String CHINA = "中国";
//    private Intent mIntent;


/*    private static class SingletonHolder{
        private final static RecognitionPresenterImpl instance=new RecognitionPresenterImpl(iRecognitionView, mcontext);
    }

    public static RecognitionPresenterImpl getInstance(){
        return SingletonHolder.instance;
    }*/

    public RecognitionPresenterImpl(IRecognitionView iRecognitionView) {
        this.iRecognitionView = iRecognitionView;
        mSharedPreferences = VoiceApplication.mContext.getSharedPreferences(Constant.EXTRA_SETTING, Context.MODE_PRIVATE);
        //语音识别
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(VoiceApplication.mContext, new ComponentName(VoiceApplication.mContext, VoiceRecognitionService.class));
        // 注册监听器
        mSpeechRecognizer.setRecognitionListener(this);
        iSynthesizerPresenter = SynthesizerPresenterImpl.getInstance();
        LocationInfo = VoiceApplication.mContext.getSharedPreferences(Constant.EXTRA_LOCATION_INFO, MODE_PRIVATE);
        Mylocation = LocationInfo.getString(Constant.MYLOCATION_CITY, "");
        suggestSearch = SuggestSearch.getInstance();
        dataSave = DataSave.getInstance();
//        mIntent = new Intent();

    }


    @Override
    public void startListening(Intent mIntent) {
        Log.e(TAG, "<---------开始监听------->");
 /*       Intent mIntent = new Intent();
        bindParams(mIntent)*/
        ;
        mSpeechRecognizer.startListening(mIntent);
    }

    @Override
    public void stopListening() {
        Log.e(TAG, "<---------停止监听------->");
        mSpeechRecognizer.stopListening();
    }

    @Override
    public void bindParams(Intent mIntent) {
        // 设置识别参数
        /**
         * 设置提示音
         */
        mIntent.putExtra(Constant.EXTRA_SOUND_START, R.raw.cruiser_pass);
        /**
         * 输入文件
         */
        /**
         *保存录音
         */
//        mIntent.putExtra(Constant.EXTRA_OUTFILE,audiopath);
        /**
         *采样频率
         */
        mIntent.putExtra(Constant.EXTRA_SAMPLE, Constant.SAMPLE_16K);
        /**
         * 语种
         */
        if (mSharedPreferences.contains(Constant.EXTRA_LANGUAGE)) {
            mIntent.putExtra(Constant.EXTRA_LANGUAGE, mSharedPreferences.getString(Constant.EXTRA_LANGUAGE, ""));
        }
        /**
         * 设置语音解析
         */
        mIntent.putExtra(Constant.EXTRA_NLU, "enable");
        /**
         * VAD设置
         * search 适用于关键字搜索
         * input 适用于短信、微信等长句输入
         * model-vad 抗噪能力强，但是需要依赖较大的资源包
         */
        mIntent.putExtra(Constant.EXTRA_VAD, Constant.VAD_SEARCH);
        /**
         * 离线
         * offline asr
         */
        mIntent.putExtra(Constant.EXTRA_GRAMMAR, "assets:///baidu_speech_grammar.bsg");
    }


    @Override
    public void onError(int mI) {
        iRecognitionView.showFailedError(mI);
        switch (mI) {
            case SpeechRecognizer.ERROR_AUDIO:
                Log.e(TAG, "音频问题");
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                Log.e(TAG, "没有语音输入");
                iSynthesizerPresenter.startSpeak("你好像没说话，再见!", Constant.QUIT_VOICE);
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                Log.e(TAG, "其它客户端错误");
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                Log.e(TAG, "权限不足");
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                iSynthesizerPresenter.startSpeak("当前网络状态不佳，请重试！", Constant.RE_TRY);
                Log.e(TAG, "网络问题");
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                iSynthesizerPresenter.startSpeak("没有匹配的识别结果，请从说!", Constant.RE_TRY);
                Log.e(TAG, "没有匹配的识别结果");
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                Log.e(TAG, "引擎忙");
                break;
            case SpeechRecognizer.ERROR_SERVER:
                Log.e(TAG, "服务端错误");
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                iSynthesizerPresenter.startSpeak("网络连接超时，请重试!", Constant.RE_TRY);
                Log.e(TAG, "连接超时");
                break;
        }
    }

    String asrRes;
    JSONObject command;

    @Override
    public void onResults(Bundle results) {
        Log.e(TAG, "onResults识别结回调");
        String json_res = results.getString(Constant.RESULT_NLU);
        String domain = null;
        try {
            JSONObject r = new JSONObject(json_res);
            asrRes = r.optString("raw_text");
            JSONArray commands = r.optJSONArray("results");
            if (commands != null && commands.length() > 0) {
                command = commands.optJSONObject(0);
                domain = command.optString("domain");
                iRecognitionView.showResult(command.toString());
                switch (domain) {
                    case "setting":
                        SettingLogic settingLogic = new SettingLogic();
                        settingLogic.Logic(asrRes, command);
                        break;
                    case "map":
                        String Obj = command.optString("object");
                        r = new JSONObject(Obj);
                        final String mArrival1 = r.optString("arrival");
                        final String mArrival2 = r.optString("centre");
                        suggestSearch.setSuggestCallback(new SuggestSearchCallBack() {
                            @Override
                            public void Funded() throws JSONException {
                                try {
                                    MapLogic mapLogic = new MapLogic();
                                    mapLogic.Logic(asrRes, command, Mylocation);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void NotFunded() {
                                suggestSearch.setSuggestCallback(new SuggestSearchCallBack() {
                                    @Override
                                    public void Funded() throws JSONException {
                                        try {
                                            MapLogic mapLogic = new MapLogic();
                                            mapLogic.Logic(asrRes, command, CHINA);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    @Override
                                    public void NotFunded() {
                                        iSynthesizerPresenter.startSpeak("没找到该地址，换个地名试试", Constant.RE_TRY);
                                    }
                                });
                                if (!TextUtils.isEmpty(mArrival1)) {
                                    suggestSearch.StartSearch(mArrival1, CHINA);
                                } else if (!TextUtils.isEmpty(mArrival2)) {
                                    suggestSearch.StartSearch(mArrival2, CHINA);
                                }
                            }
                        });
                        if (!TextUtils.isEmpty(mArrival1)) {
                            suggestSearch.StartSearch(mArrival1, Mylocation);
                        } else if (!TextUtils.isEmpty(mArrival2)) {
                            suggestSearch.StartSearch(mArrival2, Mylocation);
                        }
                        break;
                    case "app":
                        AppLogic appLogic = new AppLogic();
                        appLogic.Logic(asrRes, command);
                        break;
                    case "radio":
                        RadioLogic radioLogic = new RadioLogic();
                        radioLogic.Logic(asrRes, command);
                        break;
                    case "custom":
                        CustomLogic customLogic = new CustomLogic();
                        customLogic.Logic(asrRes, command);
                        break;
                    case "instruction":
                        InstructionLogic instructionLogic = new InstructionLogic();
                        instructionLogic.Logic(asrRes, command, SCENNE);
                        break;
                    case "navigate_instruction":
                        NavigationLogic navigationLogic = new NavigationLogic();
                        navigationLogic.Logic(asrRes, command);
                        break;
                    case "vehicle_instruction":
                        Vehicle_InstuctionLogic vehicle_instuctionLogic = new Vehicle_InstuctionLogic();
                        vehicle_instuctionLogic.Logic(asrRes, command);
                        break;
                    case "music":
                        MusicLogic musicLogic = new MusicLogic();
                        musicLogic.Logic(asrRes, command);
                        break;
                    case "video":
                        VedioLogic vedioLogic = new VedioLogic();
                        vedioLogic.Logic(asrRes, command);
                        break;
                    case "weather":
                        WeatherLogic weatherLogic = new WeatherLogic();
                        weatherLogic.Logic(asrRes, command);
                        break;
                    case "calendar":
                        Obj = command.optString("object");
                        r = new JSONObject(Obj);
                        String ans = r.optString("ANSWER");
                        if (ans != null) {
                            iSynthesizerPresenter.startSpeak(ans, Constant.CONTINUE_LISTEN);
                        }
                        break;
                    case "player":
                        PlayerLogic playerLogic = new PlayerLogic();
                        playerLogic.Logic(asrRes, command);
                        break;
                    default:
                        iSynthesizerPresenter.startSpeak("我还不会", Constant.RE_TRY);
                        break;
                }
            } else {
                if (SCENNE != null) {
                    switch (SCENNE) {
                        case "map":
                            command = null;
                            suggestSearch.setSuggestCallback(new SuggestSearchCallBack() {
                                @Override
                                public void Funded() throws JSONException {
                                    try {
                                        MapLogic mapLogic = new MapLogic();
                                        mapLogic.Logic(asrRes, command, CHINA);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    SCENNE = null;
                                }
                                @Override
                                public void NotFunded() {
                                    iSynthesizerPresenter.startSpeak("没找到该地址，换个地名试试", Constant.NAVI_SCENE);
                                }
                            });
                            suggestSearch.StartSearch(asrRes, CHINA);
                            break;
                    }
                } else {
                    PraseResLogic praseResLogic = new PraseResLogic();
                    String match = praseResLogic.Logic(asrRes);
                    if (match != null) {
                        iRecognitionView.showMyResult(match);
                    } else {
                        iRecognitionView.showNone();
//                        dataSave.Save(domain + "\t" + asrRes, Constant.SPEECH_TEXT);
//                        dataSave.copyFromAssetsToSdcard(false, asrRes);
                        iSynthesizerPresenter.startSpeak("我还不会", Constant.RE_TRY);
                    }
                }
            }
        } catch (Exception mE) {
            Log.e(TAG, mE.toString());
            mE.printStackTrace();
            Log.e(TAG, "程序异常");
            iSynthesizerPresenter.startSpeak("程序异常", Constant.STOP_LISTEN);
        }

    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.e(TAG, "onPartialResults识别结回调");
        ArrayList<String> partial = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        iRecognitionView.showPartialResult(partial.get(0));
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "<---------监听销毁------->");
        mSpeechRecognizer.destroy();
    }


    @Override
    public void sendSCENNE(String sc) {
        SCENNE = sc;
    }


}
