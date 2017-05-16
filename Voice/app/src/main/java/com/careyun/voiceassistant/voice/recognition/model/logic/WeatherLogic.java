package com.careyun.voiceassistant.voice.recognition.model.logic;

import android.content.Context;
import android.text.TextUtils;

import com.careyun.aidlweather.WeatherServiceManager;
import com.careyun.voiceassistant.util.Constant;
import com.careyun.voiceassistant.view.floatwindow.FloatWindowManager;
import com.careyun.voiceassistant.voice.tts.presenter.SynthesizerPresenterImpl;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Huangmq on 2017/4/11.
 */

public class WeatherLogic implements WeatherServiceManager.WeatherResponseListener {



    public WeatherLogic(){

    }

    public void Logic(String asrRes,JSONObject command) throws JSONException {

        String query = command.optString("intent");
        String Obj = command.optString("object");
        JSONObject r = new JSONObject(Obj);
        String reagion = r.optString("region");
        String date = r.optString("date");
        String result = null;
        if(!TextUtils.isEmpty(date) ){
            result = date.substring(0, date.indexOf(","));
        }
        WeatherServiceManager.getmInstance().setWeatherResponseListener(this);
        if(!TextUtils.isEmpty(reagion) && !TextUtils.isEmpty(result)) {
            WeatherServiceManager.getmInstance().otherCityWeather(reagion,result);
        }
        else if(!TextUtils.isEmpty(reagion) && TextUtils.isEmpty(result)) {
            WeatherServiceManager.getmInstance().otherCityWeather(reagion,null);
        }
        else if(TextUtils.isEmpty(reagion) && !TextUtils.isEmpty(result)){
            WeatherServiceManager.getmInstance().localCityWeather(result);
        }else {
           SynthesizerPresenterImpl.getInstance().startSpeak("不好意思我还不会", Constant.RE_TRY);
        }

    }

    @Override
    public void onWeatherResponse(String result) {
        SynthesizerPresenterImpl.getInstance().startSpeak(result, Constant.STOP_LISTEN);
    }
}
