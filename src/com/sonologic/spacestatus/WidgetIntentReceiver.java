package com.sonologic.spacestatus;

import com.sonologic.spacestatus.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

public class WidgetIntentReceiver extends BroadcastReceiver {
	
	RemoteViews views;
	
	/**
	 * @param activity
	 */
	public WidgetIntentReceiver(RemoteViews views) {
		super();
		this.views = views;
	}


	@Override
	public void onReceive(Context ctx, Intent i) {
		views.setTextViewText(R.layout.spacestatus_appwidget, "-");
	}
}
