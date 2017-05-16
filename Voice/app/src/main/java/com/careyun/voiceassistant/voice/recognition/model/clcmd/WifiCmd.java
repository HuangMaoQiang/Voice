package com.careyun.voiceassistant.voice.recognition.model.clcmd;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Huangmq on 2017/1/18.
 * 检测和控制当前网络状态
 */

public class WifiCmd {

    // 定义WifiManager对象
    private WifiManager mWifiManager;
    // 定义WifiInfo对象
    private WifiInfo mWifiInfo;
    // 定义网络ConnettivityManager对象
    private ConnectivityManager mConnectivityManager;
    // 定义 NetworInfo对象
    private NetworkInfo mNetworkInfo;

    //构造器
    public WifiCmd(Context mContext) {
        //取得WifiManager对象
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        //取得WifiInfo
        mWifiInfo = mWifiManager.getConnectionInfo();
        //取得Connectivity对象
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        //取得NetworkInfo对象
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
    }

    // 打开WIFI
    public void openWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    // 断开当前网络
    public void disconnectWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.disconnect();
        }
    }

    // 关闭WIFI
    public void closeWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    //检测当前Wifi状态
    public int checkwifiState() {
        return mWifiManager.getWifiState();
    }

    //检测当前移动网路

    public int checkNetState() {
        return mNetworkInfo.getType();

    }

    public boolean isOpenNetwork() {

        if (mNetworkInfo != null) {
            if (ConnectivityManager.TYPE_WIFI == checkNetState()) {

            } else if (ConnectivityManager.TYPE_MOBILE == checkNetState()) {

            }
            return mConnectivityManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

    // 打开移动网络
    public void openMobileNet() {
        ToggleMobileData(true);
    }

    // 关闭移动网络
    public void closeMobileNet() {
        ToggleMobileData(false);
    }

    public void ToggleMobileData(boolean state) {

        Method method = null;
        try {
            method = mConnectivityManager.getClass().getMethod(
                    "setMobileDataEnabled", new Class[]{boolean.class});
            try {
                method.invoke(mConnectivityManager, state);
            } catch (IllegalAccessException mE) {
                mE.printStackTrace();
            } catch (InvocationTargetException mE) {
                mE.printStackTrace();
            }
        } catch (NoSuchMethodException mE) {
            mE.printStackTrace();
        }
    }

}

