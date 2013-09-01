package com.example.alarmclock;

import android.os.Bundle;
import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import com.example.alarmclock.MyTextToSpeech;

//add a options selector
public class MainActivity extends Activity implements TextToSpeech.OnInitListener,OnClickListener {
	
	private ArrayList<Alarm> alarms;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		PreferencesHandler prefsHandler = new PreferencesHandler(this);
		Preferences prefs = prefsHandler.getSettings();
		
		this.alarms=prefs.getAlarms();
		
		createAlarmsViews();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void createAlarmsViews()
	{
		TableLayout mainTable = new TableLayout(this);
		LayoutParams mainTableParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		mainTableParams.setMargins(0, 20, 0, 0);
		mainTable.setLayoutParams(mainTableParams);
		
		for(int i = 0; i<this.alarms.size(); i+=2)
		{
			Alarm currentAlarm = alarms.get(i);
			
			TableRow tableRow = new TableRow(this);
			LayoutParams tableRowParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			tableRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));  
			tableRow.setPadding(5, 5, 5, 5);
			
			tableRow.addView(createAlarmView(alarms.get(i)));
			tableRow.addView(createAlarmView(alarms.get(i+1)));
			
			mainTable.addView(tableRow);
		}
		
		this.setContentView(mainTable);
	}
	
	private LinearLayout createAlarmView(Alarm alarm)
	{
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setBackgroundColor(Color.BLUE);
		linearLayout.setPadding(5, 5, 5, 5);
		LayoutParams linearLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		linearLayoutParams.weight=1;
		linearLayoutParams.setMargins(10, 0, 10, 0);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setLayoutParams(linearLayoutParams);
		
		linearLayout.addView(getTimeView(alarm));
		linearLayout.addView(getSettingImages(alarm));
		
		return linearLayout;
	}
	
	private TextView getTimeView(Alarm alarm)
	{ 
		TextView timeView = new TextView(this);
		LayoutParams timeLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		timeView.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
		timeView.setText(alarm.getTime());
		
		return timeView;
	}
	
	private LinearLayout getSettingImages(Alarm alarm)
	{
		LinearLayout settingsLinearLayout = new LinearLayout(this);
		settingsLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams settingsLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT); 
		
		ImageView facebookImageView = new ImageView(this);
		facebookImageView.setId(1);
		facebookImageView.setBackgroundResource(R.drawable.facebook_icon);
		LayoutParams facebookLayoutParams = new LayoutParams(30,30);
		facebookLayoutParams.setMargins(1, 1, 1, 1);
		facebookImageView.setLayoutParams(facebookLayoutParams);
		
		ImageView musicImageView = new ImageView(this);
		musicImageView.setId(2);
		musicImageView.setBackgroundResource(R.drawable.music_icon);
		LayoutParams musicLayoutParams = new LayoutParams(30,30);
		musicLayoutParams.setMargins(1, 1, 1, 1);
		musicImageView.setLayoutParams(musicLayoutParams);
		
		ImageView textContactsImageView = new ImageView(this);
		textContactsImageView.setId(3);
		textContactsImageView.setBackgroundResource(R.drawable.text_contacts);
		LayoutParams textContactsLayoutParams = new LayoutParams(30,30);
		textContactsLayoutParams.setMargins(1, 1, 1, 1);
		textContactsImageView.setLayoutParams(textContactsLayoutParams);
		
		settingsLinearLayout.addView(facebookImageView);
		settingsLinearLayout.addView(musicImageView);
		settingsLinearLayout.addView(textContactsImageView);
		
		return settingsLinearLayout;
	}

	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}