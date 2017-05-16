
package com.ruiyi.cardvr.aidlservice;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.ruiyi.cardvr.aidl.IMediaService;

public class DvrServiceManager {
    private static final String TAG = "DvrServiceManager";

    private Context mContext;
    public IMediaService mDvrService;
    private static DvrServiceManager mInstance;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected");
            mDvrService = IMediaService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected");
            mDvrService = null;
            bindMpospService(mContext);
        }

    };

    synchronized public static DvrServiceManager getmInstance() {
        if (mInstance == null) {
            mInstance = new DvrServiceManager();
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
        intent.setAction(CarDVRService.class.getName());
        intent.setPackage("com.org.opencv.rycardvr");
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
