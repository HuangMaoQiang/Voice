package com.ruiyi.cardvr.aidlservice;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.careyun.navigation.INavigationService;


/**
 * Created by Huangmq on 2017/2/24.
 */

public class NaviServiceManager {
    private static final String TAG = "NaviServiceManager";

    private Context mContext;
    public INavigationService mNavigationService;
    private static NaviServiceManager mInstance;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected");
            mNavigationService = INavigationService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName mComponentName) {
            Log.e(TAG, "onServiceDisconnected");
            mNavigationService = null;
            bindMpospService(mContext);
        }
    };

    synchronized public static NaviServiceManager getmInstance() {
        if (mInstance == null) {
            mInstance = new NaviServiceManager();
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
        intent.setAction(CarNaviService.class.getName());
        intent.setPackage("com.careyun.navigation");
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

}
