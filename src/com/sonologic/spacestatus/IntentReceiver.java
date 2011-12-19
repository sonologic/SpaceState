package com.sonologic.spacestatus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class IntentReceiver extends BroadcastReceiver {

	private SpaceStatus activity;

	/**
	 * @param activity
	 */
	public IntentReceiver(SpaceStatus activity) {
		super();
		this.activity = activity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("com.sonologic.spacestatus.UPDATE")) {
			SpaceStatusParcelable status = intent.getParcelableExtra("status");
			activity.updateStatus(status);
		}
		if (intent.getAction().equals("com.sonologic.spacestatus.LISTUPDATE")) {
			StatusDirectoryParcelable directory = intent.getParcelableExtra("directory");
			activity.getPrefs().setSpaceList(directory);
			activity.updateList(directory);
		}
	}
}
