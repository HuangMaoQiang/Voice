package com.careyun.voiceassistant.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.careyun.voiceassistant.R;
import com.careyun.voiceassistant.util.Constant;
import com.careyun.voiceassistant.voice.tts.presenter.SynthesizerPresenterImpl;


public class HorizonHelpActivity extends BaseActivity implements View.OnClickListener{

    private static final int VIDEO_CONTENT_DESC_MAX_LINE = 1;// 默认展示最大行数3行
    private static final int SHOW_CONTENT_NONE_STATE = 0;// 扩充
    private static final int SHRINK_UP_STATE = 1;// 收起状态
    private static final int SPREAD_STATE = 2;// 展开状态
    private static int mState = SHRINK_UP_STATE;//默认收起状态

    private TextView mNaviT;
    private RelativeLayout mNaviR;
    private ImageView mNaviD;
    private ImageView mNaviU;

    private TextView mWeatherT;
    private RelativeLayout mWeatherR;
    private ImageView mWeatherD;
    private ImageView mWeatherU;

    private TextView mMusicT;
    private RelativeLayout mMusicR;
    private ImageView mMusicD;
    private ImageView mMusicU;

    private TextView mSetT;
    private RelativeLayout mSetR;
    private ImageView mSetD;
    private ImageView mSetU;

    private TextView mAppT;
    private RelativeLayout mAppR;
    private ImageView mAppD;
    private ImageView mAppU;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horizon_help);

        initView();
        HelpSpeech();

    }

    private  void HelpSpeech(){
        SynthesizerPresenterImpl.getInstance().startSpeak("你可以这样说，导航到天府广场，我要听歌，打开行车记录仪，打开蓝牙，今天天气如何。", Constant.HELP_SPEECH);
    }

    private void initView() {

        mNaviT = (TextView) findViewById(R.id.navi_speech);
        mNaviR = (RelativeLayout) findViewById(R.id.navi_block);
        mNaviD = (ImageView) findViewById(R.id.navi_down);
        mNaviU = (ImageView) findViewById(R.id.navi_up);
        mNaviR.setOnClickListener(this);

        mAppT = (TextView) findViewById(R.id.app_speech);
        mAppR = (RelativeLayout) findViewById(R.id.app_block);
        mAppD = (ImageView) findViewById(R.id.app_down);
        mAppU = (ImageView) findViewById(R.id.app_up);
        mAppR.setOnClickListener(this);

        mWeatherT = (TextView) findViewById(R.id.weather_speech);
        mWeatherR = (RelativeLayout) findViewById(R.id.weather_block);
        mWeatherD = (ImageView) findViewById(R.id.weather_down);
        mWeatherU = (ImageView) findViewById(R.id.weather_up);
        mWeatherR.setOnClickListener(this);

        mSetT = (TextView) findViewById(R.id.set_speech);
        mSetR = (RelativeLayout) findViewById(R.id.set_block);
        mSetD = (ImageView) findViewById(R.id.set_down);
        mSetU = (ImageView) findViewById(R.id.set_up);
        mSetR.setOnClickListener(this);

        mMusicT = (TextView) findViewById(R.id.music_speech);
        mMusicR = (RelativeLayout) findViewById(R.id.music_block);
        mMusicD = (ImageView) findViewById(R.id.music_down);
        mMusicU = (ImageView) findViewById(R.id.music_up);
        mMusicR.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navi_block: {
                if (mState == SPREAD_STATE) {
                    mNaviT.setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
                    mNaviT.requestLayout();
                    mNaviU.setVisibility(View.GONE);
                    mNaviD.setVisibility(View.VISIBLE);
                    mState = SHRINK_UP_STATE;
                } else if (mState == SHRINK_UP_STATE) {
                    mNaviT.setMaxLines(Integer.MAX_VALUE);
                    mNaviT.requestLayout();
                    mNaviU.setVisibility(View.VISIBLE);
                    mNaviD.setVisibility(View.GONE);
                    mState = SPREAD_STATE;
                }
                break;
            }
            case R.id.app_block: {
                if (mState == SPREAD_STATE) {
                    mAppT.setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
                    mAppT.requestLayout();
                    mAppU.setVisibility(View.GONE);
                    mAppD.setVisibility(View.VISIBLE);
                    mState = SHRINK_UP_STATE;
                } else if (mState == SHRINK_UP_STATE) {
                    mAppT.setMaxLines(Integer.MAX_VALUE);
                    mAppT.requestLayout();
                    mAppU.setVisibility(View.VISIBLE);
                    mAppD.setVisibility(View.GONE);
                    mState = SPREAD_STATE;
                }
                break;
            }
            case R.id.weather_block: {
                if (mState == SPREAD_STATE) {
                    mWeatherT.setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
                    mWeatherT.requestLayout();
                    mWeatherU.setVisibility(View.GONE);
                    mWeatherD.setVisibility(View.VISIBLE);
                    mState = SHRINK_UP_STATE;
                } else if (mState == SHRINK_UP_STATE) {
                    mWeatherT.setMaxLines(Integer.MAX_VALUE);
                    mWeatherT.requestLayout();
                    mWeatherU.setVisibility(View.VISIBLE);
                    mWeatherD.setVisibility(View.GONE);
                    mState = SPREAD_STATE;
                }
                break;
            }
            case R.id.set_block: {
                if (mState == SPREAD_STATE) {
                    mSetT.setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
                    mSetT.requestLayout();
                    mSetU.setVisibility(View.GONE);
                    mSetD.setVisibility(View.VISIBLE);
                    mState = SHRINK_UP_STATE;
                } else if (mState == SHRINK_UP_STATE) {
                    mSetT.setMaxLines(Integer.MAX_VALUE);
                    mSetT.requestLayout();
                    mSetU.setVisibility(View.VISIBLE);
                    mSetD.setVisibility(View.GONE);
                    mState = SPREAD_STATE;
                }
                break;
            }
            case R.id.music_block: {
                if (mState == SPREAD_STATE) {
                    mMusicT.setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
                    mMusicT.requestLayout();
                    mMusicU.setVisibility(View.GONE);
                    mMusicD.setVisibility(View.VISIBLE);
                    mState = SHRINK_UP_STATE;
                } else if (mState == SHRINK_UP_STATE) {
                    mMusicT.setMaxLines(Integer.MAX_VALUE);
                    mMusicT.requestLayout();
                    mMusicU.setVisibility(View.VISIBLE);
                    mMusicD.setVisibility(View.GONE);
                    mState = SPREAD_STATE;
                }
                break;
            }

            default:
                break;
        }
    }
}
