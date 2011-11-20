/**
 * SpaceStatus Preferences
 */
package com.sonologic.spacestatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
	private List<String> subscriptions=new ArrayList<String>();
	
	/**
	 * 
	 */
	public SpaceStatusPrefs(Context context) {
		this.setContext(context);
		this.prefs = context.getSharedPreferences(context.getString(R.string.prefsName),0);
		this.edit=prefs.edit();
		this.subscriptions = this.getSubscriptions();
	}
	
	public long getUpdateInterval() {
		return this.prefs.getLong("updateInterval", 1000*60*5);
	}
	
	public void setUpdateInterval(long interval) {
		this.edit.putLong("updateInterval", interval);
		this.edit.commit();
	}
	
	public List<String> getSubscriptions() {
		String[] subs = this.prefs.getString("subscriptions", "").split(",");
		List<String> newList = new ArrayList<String>();
		for(int i=0;i<subs.length;i++) newList.add(subs[i]);
		return newList;
	}
	
	private void setSubscriptions() {
		String val="";
		for(Iterator<String> i = subscriptions.iterator(); i.hasNext(); ) {
			val=val+i.next();
			if(i.hasNext()) val=val+",";
		}
		this.edit.putString("subscriptions", val);
		this.edit.commit();
	}
	
	public void subscribe(String name) {
		if(!subscriptions.contains(name)) subscriptions.add(name);
		this.setSubscriptions();
	}
	
	public void unsubscribe(String name) {
		subscriptions.remove(name);
		this.setSubscriptions();
	}
	
	public boolean isSubscribed(String name) {
		return subscriptions.contains(name);
	}

	/**
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	
}
