package com.example.alarmclock;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class AlarmHandler extends Activity {
	private PreferencesHandler PreferencesHandler;
	private Preferences prefs;
	void wakeUpUser()
	{
		Intent intent = new Intent(this, AlarmHandler.class);
		
	    if(prefs.getfacebook())intent = new Intent(this, FacebookAlertActivity.class);
		if(prefs.gettextcontacts())intent = new Intent(this, TextContactsAlertActivity.class);
		if(prefs.getmusic())intent = new Intent(this, MusicAlertActivity.class);
		if(prefs.getvideonews())intent = new Intent(this, VideoNewsAlertActivity.class);
		
	    startActivity(intent);	
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_handler);
		PreferencesHandler=new PreferencesHandler(this);
		prefs=PreferencesHandler.getSettings();
		wakeUpUser();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_alarm_handler, menu);
		return true;
	}

}
