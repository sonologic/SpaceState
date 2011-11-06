/**
 * SpaceStatus Preferences
 */
package com.sonologic.spacestatus;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author gmc
 *
 * Wrapper for SharedPreferences providing methods specific to SpaceStatus
 * 
 */
public class SpaceStatusPrefs {
	private Context context;
	private SharedPreferences prefs;
	private SharedPreferences.Editor edit;

	/**
	 * 
	 */
	public SpaceStatusPrefs(Context context) {
		this.context=context;
		this.prefs = context.getSharedPreferences(context.getString(R.string.prefsName),0);
		this.edit=prefs.edit();
	}
	
	public long getUpdateInterval() {
		return this.prefs.getLong("updateInterval", 1000*60*5);
	}
	
	public void setUpdateInterval(long interval) {
		this.edit.putLong("updateInterval", interval);
		this.edit.commit();
	}

	
}
