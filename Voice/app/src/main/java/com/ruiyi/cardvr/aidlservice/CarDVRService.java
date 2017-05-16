package com.ruiyi.cardvr.aidlservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CarDVRService extends Service {

	private ServiceBinder mServiceBinder = new ServiceBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return mServiceBinder;
	}
}
