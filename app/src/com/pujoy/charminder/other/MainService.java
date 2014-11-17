/*
**  Class MainService
**  src/com/pujoy/charminder/other/MainService.java
*/
package com.pujoy.charminder.other;

import com.pujoy.charminder.helper.NotificationController;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MainService extends Service {
	
	public static MainService mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (G.mTimerThread == null)
			G.mTimerThread = new TimerThread();

		startForeground(NotificationController.ID,
				NotificationController.getDefaultNotification());

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		G.mTimerThread.destroy();
		G.mTimerThread = null;
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
}
