package com.sonologic.spacestatus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class WidgetConfiguration extends Activity {
	int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	SpaceStatusPrefs prefs;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// Set the result to CANCELED. This will cause the widget host to cancel
		// out of the widget placement if they press the back button.
		setResult(RESULT_CANCELED);

		// Set the view layout resource to use.
		setContentView(R.layout.widgetconf);

		// Find the widget id from the intent.
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		// If they gave us an intent without the widget id, just bail.
		if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			finish();
		}

		List<String> spacenames = new ArrayList<String>();

		Log.d("com.sonologic.spacestate", "test");

		prefs = new SpaceStatusPrefs(this);
		List<Pair<String, String>> spacelist = prefs.getSpaceList();

		Log.d("com.sonologic.spacestatus",
				"space count: " + Integer.toString(spacelist.size()));

		// LinearLayout spaceListView = (LinearLayout)
		// findViewById(R.id.widgetconflist);

		// spaceListView.removeAllViews();

		for (Iterator<Pair<String, String>> i = spacelist.iterator(); i
				.hasNext();) {
			Pair<String, String> item = i.next();
			spacenames.add(item.first);
		}

		LinearLayout spaceListView = (LinearLayout) findViewById(R.id.widgetconflist);

		spaceListView.removeAllViews();

		ListView lv = new ListView(this);
		lv.setAdapter(new ArrayAdapter<String>(this, R.layout.widgetconf_item,
				spacenames));

		lv.setTextFilterEnabled(true);
		lv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));

		lv.setOnItemClickListener(this.onClickListener);

		spaceListView.addView(lv);

	}

	AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			final Context context = WidgetConfiguration.this;

			String name = (String) ((TextView) view).getText();
			
			Toast.makeText(getApplicationContext(),
					name, Toast.LENGTH_SHORT).show();

			prefs.subscribe(name);
			
			prefs.setWidgetData(widgetId,name);
			
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);

			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.spacestatus_appwidget);

			appWidgetManager.updateAppWidget(widgetId, views);

			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
			setResult(RESULT_OK, resultValue);
			finish();

		}

	};

}
