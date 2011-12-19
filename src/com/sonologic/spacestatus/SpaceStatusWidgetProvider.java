/**
 * 
 */
package com.sonologic.spacestatus;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * @author gmc
 * 
 */
public class SpaceStatusWidgetProvider extends AppWidgetProvider {

	private RemoteViews views;
	private boolean status = false;
	private SpaceStatusPrefs prefs = null;

	public void updateStatus(Context context) {
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);

		views = new RemoteViews(context.getPackageName(),
				R.layout.spacestatus_appwidget);

		if (this.status) {
			views.setTextColor(R.id.text, Color.GREEN);
			views.setTextViewText(R.id.text, "O");
		} else {
			views.setTextColor(R.id.text, Color.RED);
			views.setTextViewText(R.id.text, "C");
		}
		// update the widget
		ComponentName widgetComponent = new ComponentName(
				"com.sonologic.spacestatus",
				"com.sonologic.spacestatus.SpaceStatusWidgetProvider");
		appWidgetManager.updateAppWidget(widgetComponent, views);
	}

	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		final int N = appWidgetIds.length;

		if(prefs==null) prefs = new SpaceStatusPrefs(context);
		
		// Perform this loop procedure for each App Widget that belongs to this
		// provider
		for (int i = 0; i < N; i++) {			
			int appWidgetId = appWidgetIds[i];

			String spacename = prefs.getWidgetData(appWidgetId);
			
			Log.d("com.sonologic.spacestatus","widget "+Integer.toString(appWidgetId)+" has space "+spacename);
			
			// create an intent to launch the SpaceStatus activity
			Intent clickIntent = new Intent(context, SpaceStatus.class);
			PendingIntent clickPI = PendingIntent.getActivity(context, 0,clickIntent, 0);

			// attach event to the onClick listener of the widget button
			RemoteViews widget = new RemoteViews(context.getPackageName(),R.layout.spacestatus_appwidget);
			widget.setOnClickPendingIntent(R.id.text, clickPI);
			
			appWidgetManager.updateAppWidget(appWidgetId, widget);
		}
		updateStatus(context);
	}

	/*
	 * Receives the intent, and updates the internal status
	 * 
	 * @see
	 * android.appwidget.AppWidgetProvider#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		if (intent.getAction().equals("com.sonologic.spacestatus.UPDATE")) {
			SpaceStatusParcelable status = intent.getParcelableExtra("status");
			this.status = status.getOpen();
			updateStatus(context);
		}
	}

	/*
	 * Start the service when widget is enabled
	 * 
	 * @see
	 * android.appwidget.AppWidgetProvider#onEnabled(android.content.Context)
	 */
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		
		

		Intent svc = new Intent(context, GetStatusService.class);
		context.startService(svc);

		// TODO: figure out why widget doesn't initialize properly
		Intent force = new Intent("com.sonologic.spacestatus.GETUPDATE");
		context.sendBroadcast(force);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		
		for(int i=0;i<appWidgetIds.length;i++) {
			prefs.rmWidgetData(appWidgetIds[i]);
		}
	}
	


}
