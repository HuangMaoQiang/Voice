package com.ruiyi.cardvr.aidlservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CarNaviService extends Service {

    private NaviServiceBinder mServiceBinder = new NaviServiceBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mServiceBinder;
    }
}
