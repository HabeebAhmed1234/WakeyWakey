package com.example.alarmclock;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class AlarmHandler extends Activity {
	
	private PreferencesHandler PreferencesHandler;
	private Preferences prefs;
	
	void wakeUpUser()
	{
		// what is the desired behaviour when no option is selected? is this even a use case?
		// what will currently happen when no option is selected?
		//MusicAlertActivity is default
		Intent intent = new Intent(this, ShakeToWakeActivity.class);
		
		/*Log.d("debuggings", "Alarmhandler Oncreate");
		
	    if(prefs.getfacebook())
	    {
	    	Log.d("debuggings","FacebookAlertActivity before");
	    	intent = new Intent(this, FacebookAlertActivity.class);
			Log.d("debuggings", "FacebookAlertActivity after");
	    }
		if(prefs.gettextcontacts())
		{
	    	Log.d("debuggings","TextContactsAlertActivity before");
			intent = new Intent(this, TextContactsAlertActivity.class);
			Log.d("debuggings","TextContactsAlertActivity after");
		}
		if(prefs.getmusic())
		{
			Log.d("debuggings","MusicAlertActivity before");
			intent = new Intent(this, MusicAlertActivity.class);
			Log.d("debuggings","MusicAlertActivity after");
		}
		if(prefs.getvideonews())
		{
			Log.d("debuggings","VideoNewsAlertActivity before");
			intent = new Intent(this, VideoNewsAlertActivity.class);
			Log.d("debuggings","VideoNewsAlertActivity after");
		}
		
		Log.d("debuggings","startActivity before");*/
	    startActivity(intent);	
	    Log.d("debuggings","startActivity after");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_handler);
		
		//Intent intent = new Intent(this, FacebookAlertActivity.class);
		//startActivity(intent);
		
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
