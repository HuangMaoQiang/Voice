package com.careyun.aidlfm;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.ruiyi.tingfm.aidl.ITingFMService;

/**
 * Created by Huangmq on 2017/3/14.
 */

public class ServiceManager {
    private static final String TAG = "FMServiceManager";

    private Context mContext;
    public ITingFMService miTingFMService;
    private static ServiceManager mInstance;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            miTingFMService = ITingFMService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            miTingFMService = null;
            bindMpospService(mContext);
        }
    };
    synchronized public static ServiceManager getmInstance() {
        if (mInstance == null) {
            mInstance = new ServiceManager();
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
        intent.setAction("com.ruiyi.cartingfm.aidlservice.TingFMService");
        intent.setPackage("com.ruiyi.cartingfm");
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

