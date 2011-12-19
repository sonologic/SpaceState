/**
 * SpaceStatus Preferences
 */
package com.sonologic.spacestatus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

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
		this.prefs = context.getSharedPreferences(context.getString(R.string.prefsName),
				Context.MODE_WORLD_READABLE |
				Context.MODE_WORLD_WRITEABLE);
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
		if(this.prefs.getString("subscriptions", "").length()>0) {
			for(int i=0;i<subs.length;i++) newList.add(subs[i]);
		}
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
	
	public List<Pair<String,String>> getSpaceList() {
		String [] spaces = this.prefs.getString("spacenames", "").split(",");
		String [] urls = this.prefs.getString("spaceurls","").split(",");
		List<Pair<String,String>> newList = new ArrayList<Pair<String,String>>();
		if(this.prefs.getString("spacenames", "").length()>0) {
		  for(int i=0;i<spaces.length;i++) newList.add(new Pair(spaces[i],urls[i]));
		}
		return newList;
	}
	
	public void setSpaceList(StatusDirectoryParcelable dir) {
		String names="";
		String urls="";
		for(int i=0;i<dir.size();i++) {
			names=names+(dir.get(i).getName());
			urls=urls+(dir.get(i).getUrl());
			if(i!=dir.size()-1) {
				// @todo what if name or url has comma?
				names=names+",";
				urls=urls+",";
			}
		}
		this.edit.putString("spacenames",names);
		this.edit.putString("spaceurls",urls);
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

	public void setWidgetData(int widgetId, String name) {
		this.edit.putString("widget_"+Integer.toString(widgetId),name);
		this.edit.commit();
	}
	
	public String getWidgetData(int widgetId) {
		return this.prefs.getString("widget_"+Integer.toString(widgetId), null);
	}
	
	public void rmWidgetData(int widgetId) {
		this.edit.remove("widget_"+Integer.toString(widgetId));
		this.edit.commit();
	}

	
}
