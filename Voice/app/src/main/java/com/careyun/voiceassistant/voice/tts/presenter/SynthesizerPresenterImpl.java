package com.careyun.voiceassistant.voice.tts.presenter;

import android.content.Context;
import android.util.Log;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.TtsMode;
import com.careyun.voiceassistant.app.VoiceApplication;
import com.careyun.voiceassistant.eventbus.RecogEvent;
import com.careyun.voiceassistant.util.Constant;
import com.careyun.voiceassistant.voice.tts.model.SynthesizerService;
import com.careyun.voiceassistant.voice.tts.view.ISynthesizerView;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Huangmq on 2017/4/19.
 */

public class SynthesizerPresenterImpl extends SynthesizerService implements ISynthesizerPresenter {

    private final String TAG = "SynthesizerPresenter";
    private String mSampleDirPath;
    private static final String SAMPLE_DIR_NAME = "baiduTTS";
    private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";
    private static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";
    private static final String TEXT_MODEL_NAME = "bd_etts_text.dat";
    private static final String LICENSE_FILE_NAME = "temp_license";
    private static final String ENGLISH_SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female_en.dat";
    private static final String ENGLISH_SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male_en.dat";
    private static final String ENGLISH_TEXT_MODEL_NAME = "bd_etts_text_en.dat";
    private int COUNT_RECOG_TIMES = 0;
    private SpeechSynthesizer mSpeechSynthesizer;
    private ISynthesizerView iSynthesizerView;


    private static class SingletonHolder{
        private final static SynthesizerPresenterImpl instance=new SynthesizerPresenterImpl();
    }
    public static SynthesizerPresenterImpl getInstance(){
        return SingletonHolder.instance;
    }

    public SynthesizerPresenterImpl() {
    }

    /**
     * 语音合成初始化
     */
    @Override
    public void initTTS() {
        // 获取语音合成对象实例
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        // 设置context
        mSpeechSynthesizer.setContext(VoiceApplication.mContext);
        // 设置语音合成状态监听器
        mSpeechSynthesizer.setSpeechSynthesizerListener(this);
        // 设置在线语音合成授权，需要填入从百度语音官网申请的api_key和secret_key
        mSpeechSynthesizer.setApiKey("6pCE63zo5KaGD1L8FV5PZb6L", "b7f85458ea784b193bdf6e176345e9e4");
        // 设置离线语音合成授权，需要填入从百度语音官网申请的app_id
        mSpeechSynthesizer.setAppId("9510831");
        // 文本模型文件路径 (离线引擎使用)
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mSampleDirPath + "/"
                + TEXT_MODEL_NAME);
        // 声学模型文件路径 (离线引擎使用)
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, mSampleDirPath + "/"
                + SPEECH_FEMALE_MODEL_NAME);
        // 设置语音合成声音授权文件(离线使用)
        // mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, "your_licence_path");
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOCODER_OPTIM_LEVEL, "2");
        // 获取语音合成授权信息
        // 发音人（在线引擎），可用参数为0,1,2,3。。。（服务器端会动态增加，各值含义参考文档，以文档说明为准。0--普通女声，1--普通男声，2--特别男声，3--情感男声。。。）
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置Mix模式的合成策略
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_HIGH_SPEED_NETWORK);
        // 授权检测接口(只是通过AuthInfo进行检验授权是否成功。)
        // AuthInfo接口用于测试开发者是否成功申请了在线或者离线授权，如果测试授权成功了，可以删除AuthInfo部分的代码（该接口首次验证时比较耗时），不会影响正常使用（合成使用时SDK内部会自动验证授权）
        AuthInfo authInfo = mSpeechSynthesizer.auth(TtsMode.MIX);
        // 加载离线英文资源（提供离线英文合成功能）
        // mSpeechSynthesizer.loadModel("assets:///bd_etts_speech_female.dat","assets:///bd_etts_text.dat");
        // 判断授权信息是否正确，如果正确则初始化语音合成器并开始语音合成，如果失败则做错误处理
/*        if (authInfo.isSuccess()) {
            //mSpeechSynthesizer.speak("百度语音合成示例程序正在运行");
            Toast.makeText(context, "初始化成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "合成初始化失败", Toast.LENGTH_SHORT).show();
        }*/
        mSpeechSynthesizer.initTts(TtsMode.MIX);
        mSpeechSynthesizer.loadModel(mSampleDirPath + "/" + TEXT_MODEL_NAME, mSampleDirPath
                + "/" + SPEECH_FEMALE_MODEL_NAME);
        mSpeechSynthesizer.loadEnglishModel(mSampleDirPath + "/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath
                + "/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME);

    }

    RecogEvent myEvent = new RecogEvent();
    @Override
    public void onSpeechFinish(String ms) {
        Log.e(TAG, "<---------播报结束------->");

        switch (ms){
            case Constant.CONTINUE_LISTEN:
                myEvent.setMassage(Constant.ONLINE_LISTEN_MASSAGE);
                EventBus.getDefault().post(myEvent);
                break;
            case Constant.STOP_LISTEN:
                myEvent.setMassage(Constant.STOP_LISTEN_MASSAGE);
                EventBus.getDefault().post(myEvent);
                break;
            case Constant.RE_TRY:
                COUNT_RECOG_TIMES = COUNT_RECOG_TIMES + 1;
                Log.e(TAG,"第"+String.valueOf(COUNT_RECOG_TIMES)+"次监听");
                if(COUNT_RECOG_TIMES !=5) {
                    myEvent.setMassage(Constant.RE_TRY_MASSAGE);
                    EventBus.getDefault().post(myEvent);
                }
                else if (COUNT_RECOG_TIMES == 5 ){
                    mSpeechSynthesizer.speak("可能附近噪音有点大，我听不清，再见!",Constant.QUIT_VOICE);
                    myEvent.setMassage(Constant.STOP_LISTEN_MASSAGE);
                    EventBus.getDefault().post(myEvent);
                    COUNT_RECOG_TIMES=0;
                }

                break;
            case Constant.NAVI_SCENE:
                myEvent.setMassage(Constant.NAVI_SCENE_MASSAGE);
                EventBus.getDefault().post(myEvent);
                break;
            case Constant.MUSIC_SCENE:
                myEvent.setMassage(Constant.MUSIC_SCENE_MASSAGE);
                EventBus.getDefault().post(myEvent);
                break;
            case Constant.HELP_SPEECH:
                myEvent.setMassage(Constant.HELP_SPEECH_MASSAGE);
                EventBus.getDefault().post(myEvent);
                break;
            case Constant.QUIT_VOICE:
                myEvent.setMassage(Constant.QUIT_VOICE_MASSAGE);
                EventBus.getDefault().post(myEvent);
                break;
        }

    }

    @Override
    public void startSpeak(String speech, String Massage) {
        Log.e(TAG, "<---------开始合成------->");
        initTTS();
        mSpeechSynthesizer.speak(speech, Massage);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "<---------释放合成资源------->");
        mSpeechSynthesizer.release();
    }
}
