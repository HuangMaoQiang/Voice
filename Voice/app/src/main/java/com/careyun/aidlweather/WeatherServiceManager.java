package com.careyun.aidlweather;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.careyun.weather.IWeatherCallBack;
import com.careyun.weather.IWeatherService;

/**
 * Created by Huangmq on 2017/3/14.
 */

public class WeatherServiceManager {
    private static final String TAG = "WeatherServiceManager";

    private Context mContext;
    private IWeatherService mIWeatherService;
    private WeatherResponseListener mWeatherResponseListener;
    private static WeatherServiceManager mInstance;
    private IWeatherCallBack.Stub mIWeatherCallBack = new IWeatherCallBack.Stub() {
        @Override
        public void CityWeatherCallback(String result){
            Log.e("aaaaaaaa",result);
            if(mWeatherResponseListener != null){
                mWeatherResponseListener.onWeatherResponse(result);
            }
        }
        @Override
        public void ExtendFunctionCallBack(){

        }
    };
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIWeatherService = IWeatherService.Stub.asInterface(service);
            try {
                if(mIWeatherService != null)
                    mIWeatherService.registerCallback(mIWeatherCallBack);
            } catch (RemoteException e) {

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIWeatherService = null;
            bindMpospService(mContext);
        }
    };
    synchronized public static WeatherServiceManager getmInstance() {
        if (mInstance == null) {
            mInstance = new WeatherServiceManager();
        }
        return mInstance;
    }
    /**
     * 绑定aidl服务
     *
     * @param mContext
     */
    public void bindMpospService(Context mContext) {
        this.mContext = mContext;
        // 绑定远程服务
        Intent intent = new Intent();
        intent.setAction("com.careyun.aidlweather.WeatherService");
        intent.setPackage("com.careyun.weather");
        while (!mContext.bindService(intent, mConnection, Service.BIND_AUTO_CREATE)) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException mE) {
                mE.printStackTrace();
            }
        }
    }
    /**
     * 解除aidl服务绑定
     *
     * @param mContext
     */
    public void unbindMposService(Context mContext) {
        mContext.unbindService(mConnection);
    }
    public void localCityWeather(String time) {
        try {
            if(mIWeatherService  != null){
                mIWeatherService.localCityWeather(time);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public void otherCityWeather(String cityname,String time) {
        try {
            if(mIWeatherService  != null){
                mIWeatherService.otherCityWeather(cityname,time);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public void setWeatherResponseListener(WeatherResponseListener l){
        mWeatherResponseListener = l;
    }
    public interface WeatherResponseListener{
        public void onWeatherResponse(String result);
    }
}

