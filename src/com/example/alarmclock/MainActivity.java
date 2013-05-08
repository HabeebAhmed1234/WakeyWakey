package com.example.alarmclock;

import android.os.Bundle;
import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import java.util.Calendar;
import java.util.Locale;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import com.example.alarmclock.MyTextToSpeech;

//add a options selector
public class MainActivity extends Activity implements TextToSpeech.OnInitListener,OnClickListener {
	
	private TimePicker timePicker1;
 
	private int hour;
	private int minute;
	
	private RadioGroup radioGroup;
	private RadioButton radioButton;
	
	private PreferencesHandler prefsHandler;
	private Preferences prefs;
	
	private Button ContactsManagerButton;
	private Button MusicManagerButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
		
		MyTextToSpeech speaker = new MyTextToSpeech(this);
		speaker.say("Wake up!");
		
		prefsHandler=new PreferencesHandler(this);
		
		ContactsManagerButton = (Button) findViewById(R.id.editContactsToText);
		ContactsManagerButton.setOnClickListener(this);
		
		MusicManagerButton = (Button) findViewById(R.id.editMusic);
		MusicManagerButton.setOnClickListener(this);
		//setCurrentTimeOnView();
		//addListenerOnButton();
        //check for successful instantiation
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onToggleClicked(View view) {
	    //handle radio buttons first
	    handleRadioButton();
	    
	    // Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();
	    
	    if (on) {
	    	
	        Calendar cal = Calendar.getInstance();
	        cal.set(Calendar.MINUTE, timePicker1.getCurrentMinute());
	        cal.set(Calendar.HOUR_OF_DAY, timePicker1.getCurrentHour());
	        cal.set(Calendar.SECOND, 0);
	        cal.set(Calendar.MILLISECOND, 0);
	        
	        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
	        PendingIntent sender = PendingIntent.getBroadcast(this, 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	        
	        // Get the AlarmManager service
	        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
	        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
	    } else {
	        // Disable vibrate
	    }
	}
	
	private void handleRadioButton()
	{
		radioGroup = (RadioGroup) findViewById(R.id.optionsgroup);
		// get selected radio button from radioGroup
		int selectedId = radioGroup.getCheckedRadioButtonId();

		// find the radiobutton by returned id
	    radioButton = (RadioButton) findViewById(selectedId);
		
	    String selectedoption=(String) radioButton.getText();

    	Log.d("debuggings",selectedoption);
    	
		if(selectedoption.compareTo("Text Contacts")==0) 
		{
			prefsHandler.setTextContactsOption(true);
			prefsHandler.setMusicOption(false);
			prefsHandler.setFacebookOption(false);
			prefsHandler.setVideoNewsOption(false);
		}
		if(selectedoption.compareTo("Video News Feed")==0) 
		{
			prefsHandler.setTextContactsOption(false);
			prefsHandler.setMusicOption(false);
			prefsHandler.setFacebookOption(false);
			prefsHandler.setVideoNewsOption(true);
		}
		if(selectedoption.compareTo("Music")==0) 
		{
			prefsHandler.setTextContactsOption(false);
			prefsHandler.setMusicOption(true);
			prefsHandler.setFacebookOption(false);
			prefsHandler.setVideoNewsOption(false);
		}
		if(selectedoption.compareTo("Facebook")==0) 
		{
			prefsHandler.setTextContactsOption(false);
			prefsHandler.setMusicOption(false);
			prefsHandler.setFacebookOption(true);
			prefsHandler.setVideoNewsOption(false);
		}
	}
	private TimePickerDialog.OnTimeSetListener timePickerListener = 
            new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;
 
			// set current time into textview
			//tvDisplayTime.setText(new StringBuilder().append(pad(hour))
			//		.append(":").append(pad(minute)));
 
			// set current time into timepicker
			timePicker1.setCurrentHour(hour);
			timePicker1.setCurrentMinute(minute);
 
		}
	};
 
	private static String pad(int c) {
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}

	@Override
	public void onInit(int status) {
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		Log.d("debuggings","buttonClicked");
		if(v.getId()==R.id.editContactsToText)
		{
			Intent intent = new Intent(this, ContactManager.class);
			startActivity(intent);	
		}
		if(v.getId()==R.id.editMusic)
		{
			Log.d("debuggings","editMusicClicked");
			Intent intent = new Intent(this, MusicManagerActivity.class);
			startActivity(intent);	
		}
		
	}
}