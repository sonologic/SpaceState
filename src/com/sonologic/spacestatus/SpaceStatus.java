/**
 * Activity, provides the application UI.
 * 
 * @author Koen Martens <kmartens@sonologic.net>
 */
package com.sonologic.spacestatus;

import java.util.Calendar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


public class SpaceStatus extends Activity {

	private SpaceStatusPrefs prefs;
	
	private TextView logText;
	private TextView lastChange;
	private TextView statusText;
	private TextView updateFreq;
	private LinearLayout spaceList;

	private final BroadcastReceiver receiver = new IntentReceiver(this);
	private final IntentFilter filter = new IntentFilter();

	public void updateStatus(SpaceStatusParcelable status) {
		Calendar c = Calendar.getInstance();
		logText.setText("Last update: " + c.getTime().toString() );
		if(status.getOpen()) {
			statusText.setText(getString(R.string.open));
			statusText.setBackgroundColor(Color.GREEN);
		} else {
			statusText.setText(getString(R.string.closed));
			statusText.setBackgroundColor(Color.RED);
		}
		Time t = new Time();
		t.set(status.getLastchange()*1000);
		lastChange.setText(getString(R.string.lastchange).concat(t.format(" %H:%M, %a %e %b")));
	}
	
	public void updateList(StatusDirectoryParcelable directory) {
		this.spaceList.removeAllViews();
		
		for(int i=0;i<directory.size();i++) {
		  LinearLayout row = new LinearLayout(this);
		  row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		  row.setOrientation(LinearLayout.HORIZONTAL);
		  
		  CheckBox check = new CheckBox(this);
		  check.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		  check.setChecked(this.prefs.isSubscribed(directory.get(i).getName()));
		  check.setTag(directory.get(i).getName());
		  check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			  public void onCheckedChanged(CompoundButton b, boolean checked) {
				  CheckBox check = (CheckBox)b;
				  if(checked) {
					  ((SpaceStatus)b.getContext()).prefs.subscribe((String)check.getTag());
				  } else {
					  ((SpaceStatus)b.getContext()).prefs.unsubscribe((String)check.getTag());
				  }
			  }
		  });
		  row.addView(check);

		  TextView child = new TextView(this);
		  child.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		  child.setText(directory.get(i).getName());
		  row.addView(child);
		  
		  this.spaceList.addView(row);
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.prefs = new SpaceStatusPrefs(this);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
		//        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);

		// initialize intent filter
		this.filter.addAction("com.sonologic.spacestatus.UPDATE");
		this.filter.addAction("com.sonologic.spacestatus.LISTUPDATE");
		
		// initialize text fields
		logText=(TextView)findViewById(R.id.textview);
		lastChange=(TextView)findViewById(R.id.lastchange);
		statusText=(TextView)findViewById(R.id.spacestatus);
		updateFreq=(TextView)findViewById(R.id.showfreq);
		spaceList=(LinearLayout)findViewById(R.id.spacelist);
		
		updateFreq.setText("Update every "+Long.toString(this.prefs.getUpdateInterval()/60000)+" min");
		
		statusText.setBackgroundColor(Color.GRAY);
		statusText.setTextColor(Color.YELLOW);
		logText.setText("Waiting for first update..");

		// start the GetStatusService
		Intent svc = new Intent(this, GetStatusService.class);
		startService(svc);
		
		// initialize slider event
		SeekBar seekbar = (SeekBar)findViewById(R.id.updatefreq);
		seekbar.setProgress((int)this.prefs.getUpdateInterval()/60000);
		seekbar.setOnSeekBarChangeListener(seekbarListener);
		

	}
	
	private SeekBar.OnSeekBarChangeListener seekbarListener = new OnSeekBarChangeListener() {
		private int progress=-1;
		/* (non-Javadoc)
		 * @see android.widget.SeekBar.OnSeekBarChangeListener#onStartTrackingTouch(android.widget.SeekBar)
		 */
		public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
		  if(fromUser) {
			  this.progress=progress+1;
			  updateFreq.setText("Update every "+Integer.toString(this.progress)+" min");
		  }
		}

		/* (non-Javadoc)
		 * @see android.widget.SeekBar.OnSeekBarChangeListener#onStartTrackingTouch(android.widget.SeekBar)
		 */
		public void onStartTrackingTouch(SeekBar seekBar) {
			// NOP
		}

		/* (non-Javadoc)
		 * @see android.widget.SeekBar.OnSeekBarChangeListener#onStopTrackingTouch(android.widget.SeekBar)
		 */
		public void onStopTrackingTouch(SeekBar seekBar) {
			Intent i = new Intent("com.sonologic.spacestatus.SETFREQ");
			i.putExtra("f", this.progress);
			seekBar.getContext().sendBroadcast(i);
		}
		
	};

	public void onClickUpdate(View v) {
		Intent i = new Intent("com.sonologic.spacestatus.GETUPDATE");
		this.sendBroadcast(i);
	};

	private void registerReceiver() {
		this.registerReceiver(receiver, filter);
	}

	private void unregisterReceiver() {
		this.unregisterReceiver(receiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver();
		Intent i1 = new Intent("com.sonologic.spacestatus.GETUPDATE");
		this.sendBroadcast(i1);
		Intent i2 = new Intent("com.sonologic.spacestatus.UPDATELIST");
		this.sendBroadcast(i2);
		// The activity has become visible (it is now "resumed").
		//updateStatus();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		unregisterReceiver();
		super.onPause();
	}

	/**
	 * 
	 * @return
	 */
	public SpaceStatusPrefs getPrefs() {
		return this.prefs;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.mainmenu, menu);
	    return true;
	}
	*/

	
}