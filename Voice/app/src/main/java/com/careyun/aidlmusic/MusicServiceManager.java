package com.careyun.aidlmusic;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.ruiyi.music.aidl.IMusicService;


/**
 * Created by Huangmq on 2017/3/14.
 */

public class MusicServiceManager {
    private static final String TAG = "FMServiceManager";

    private Context mContext;
    public IMusicService iMusicService;
    private static MusicServiceManager mInstance;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iMusicService = IMusicService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            iMusicService = null;
            bindMpospService(mContext);
        }
    };
    synchronized public static MusicServiceManager getmInstance() {
        if (mInstance == null) {
            mInstance = new MusicServiceManager();
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
        intent.setAction("com.ruiyi.carmusic.aidlservice.MusicService");
        intent.setPackage("com.ruiyi.carmusic");
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


