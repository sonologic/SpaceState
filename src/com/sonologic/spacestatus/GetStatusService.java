/**
 *  Runs in the background and periodically forces an update.
 *  
 *  It creates the StatusUpdater, which does the actual request (and
 *  can be forced to fetch by sending the GETUPDATE intent.
 *  
 *  @author Koen Martens <kmartens@sonologic.net>
 *  
 */
package com.sonologic.spacestatus;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

/**
 * @author gmc
 *
 */
public class GetStatusService extends Service {

	private SpaceStatusPrefs prefs;

	private Handler mHandler = new Handler();

	private StatusUpdater updater;
	private final IntentFilter filter = new IntentFilter();

	@Override
	public void onCreate() {
		super.onCreate();

		this.prefs = new SpaceStatusPrefs(this);
		// updater listens for GETUPDATE intents, which can be used by the
		// activity to force an update
		updater=new StatusUpdater(this);
		filter.addAction("com.sonologic.spacestatus.GETUPDATE");
		filter.addAction("com.sonologic.spacestatus.UPDATELIST");
		filter.addAction("com.sonologic.spacestatus.SETFREQ");
		this.registerReceiver(updater, filter);

		// start the updaterTask, which will periodically fetch space status
		startUpdater();
		Log.i("spacestatus","ping");
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		stopUpdater();
	}

	private Runnable updaterTask = new Runnable() {
		public void run() {
			getUpdate();
			mHandler.postAtTime(this,
					SystemClock.uptimeMillis()+getUpdateFrequency());
		}
	};

	private void startUpdater() {

		mHandler.removeCallbacks(updaterTask);
		mHandler.postDelayed(updaterTask, 100);

	}

	private void stopUpdater() {
		mHandler.removeCallbacks(updaterTask);
		//if (timer != null) timer.cancel();
	}

	private void getUpdate() {
		//String s = null;
		//s+="abc";
		//System.out.println(s);
		Log.i("spacestatus","updating..");

		updater.update();	 
		updater.updateList();
	}

	public long getUpdateFrequency() {
		return this.prefs.getUpdateInterval();
	}

	public void setUpdateFrequency(long f) {
		this.prefs.setUpdateInterval(f*1000);
		this.stopUpdater();
		this.startUpdater();
	}


	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
