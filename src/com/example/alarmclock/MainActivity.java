package com.example.alarmclock;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.View.OnLongClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
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
public class MainActivity extends Activity implements TextToSpeech.OnInitListener,OnClickListener, OnLongClickListener {
	
	private ArrayList<Alarm> alarms;
	public static final int ALARM_ADDER_ID = -100;
	private int longClickedAlarmId;
	private AlarmFactory alarmFactory;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		alarmFactory = new AlarmFactory(this);
		
		PreferencesHandler prefsHandler = new PreferencesHandler(this);
		Preferences prefs = prefsHandler.getSettings();
		
		alarms = new ArrayList();
		
		this.alarms=prefs.getAlarms();
		
		try {
			createAlarmsViews();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    
		int selectionID = item.getItemId();

		if(selectionID == R.id.delete)
		{
			deleteAlarm(this.getAlarmByID(longClickedAlarmId));
		}
		
		if(selectionID == R.id.edit)
		{
			this.startSettingsActivity(this.getAlarmByID(longClickedAlarmId));
		}
		
	    return true;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.d("debugger",v.getId()+" was clicked");
		
		int ID = v.getId();
		
		//check if alarm adder was clicked Alarm adder id is -100
		if(ID == ALARM_ADDER_ID)
		{
			Time now = new Time();
			now.setToNow();
			
			Alarm newAlarm = new Alarm(now.hour, now.minute, getNewID());
			alarms.add(newAlarm);
			
			PreferencesHandler prefsHandler = new PreferencesHandler(this);
			prefsHandler.setAlarms(alarms);
			
			startSettingsActivity(newAlarm);
		}
		
		if(!(matchToAlarmIds(ID))) return;
		
		Alarm clickedAlarm = getAlarmByID(ID);
		
		clickedAlarm = this.toggleAlarm(clickedAlarm, false);
		
		float startAlpha = 1f;
		float endAlpha = 0.5f;
		
		if(clickedAlarm.enabled())
		{
			startAlpha = 0.5f;
			endAlpha = 1f;
		}
		else{
			startAlpha = 1f;
			endAlpha = 0.5f;
		}
		
		AlphaAnimation alpha = new AlphaAnimation(startAlpha, endAlpha);
		alpha.setDuration(500); 
		alpha.setFillAfter(true); 
		v.startAnimation(alpha);
	}
	
	@Override
	public boolean onLongClick(View v) {
		if(!(matchToAlarmIds(v.getId()))) return false;
		
		Log.d("debugger",v.getId()+" was long clicked");
		
		this.longClickedAlarmId = v.getId();
		this.openOptionsMenu();
		
		return false;
	}
	
	void startSettingsActivity(Alarm alarm)
	{
		Intent i = new Intent(this, SettingsActivity.class);
		Log.d ("AlarmClock","created new alarm of id "+alarm.getID());
		i.putExtra(AlarmFactory.ALARM_ID, Integer.toString(alarm.getID()));
		this.startActivity(i);
		finish();
	}
	
	private int getNewID()
	{
		int max = 0;
		for(int i = 0 ; i < alarms.size() ; i++)
		{
			if(alarms.get(i).getID()>max)max = alarms.get(i).getID();
		}
		max+=1;
		return max;
	}
	
	private boolean matchToAlarmIds(int id)
	{
		for (int i=0; i<alarms.size();i++)
		{
			if(alarms.get(i).getID()==id) return true;
		}
		return false;
	}
	
	private Alarm getAlarmByID(int id)
	{
		for (int i=0; i<alarms.size();i++)
		{
			if(alarms.get(i).getID()==id) return alarms.get(i);
		}
		return null;
	}
	
	private void createAlarmsViews() throws ParseException
	{
		TableLayout mainTable = new TableLayout(this);
		LayoutParams mainTableParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		mainTableParams.setMargins(0, 20, 0, 0);
		mainTable.setLayoutParams(mainTableParams);
		
		//add in the alarm adder tile
		Alarm alarmAdder = new Alarm(0,0,this.ALARM_ADDER_ID);
		alarms.add(alarmAdder);
		
		for(int i = 0; i<alarms.size();i+=2)
		{
			Alarm currentAlarm = alarms.get(i);
			
			TableRow tableRow = new TableRow(this);
			LayoutParams tableRowParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			tableRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));  
			tableRow.setPadding(5, 5, 5, 5);
			
			tableRow.addView(createAlarmView(alarms.get(i)));
			
			if((i+1)<alarms.size()) tableRow.addView(createAlarmView(alarms.get(i+1)));
			
			mainTable.addView(tableRow);
		}
		
		alarms.remove(alarms.size()-1);
		
		this.setContentView(mainTable);
	}
	
	private LinearLayout createAlarmView(Alarm alarm) throws ParseException
	{
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setBackgroundColor(this.getResources().getColor(R.color.Blue));
		
		if(!(alarm.getID()==this.ALARM_ADDER_ID))
		{
			float startAlpha = 1f;
			float endAlpha = 0.5f;
			
			if(alarm.enabled())
			{
				startAlpha = 0.5f;
				endAlpha = 1f;
			}
			else{
				startAlpha = 1f;
				endAlpha = 0.5f;
			}
			
			AlphaAnimation alpha = new AlphaAnimation(startAlpha, endAlpha);
			alpha.setDuration(500); 
			alpha.setFillAfter(true); 
			linearLayout.startAnimation(alpha);
		}
		
		linearLayout.setPadding(5, 5, 5, 5);
		LayoutParams linearLayoutParams = new LayoutParams((getScreenWidth() - 40)/2,LayoutParams.WRAP_CONTENT);
		linearLayoutParams.weight=1;
		linearLayoutParams.setMargins(10, 0, 10, 0);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setLayoutParams(linearLayoutParams);
		
		if(!(alarm.getID()==this.ALARM_ADDER_ID))
		{
			linearLayout.addView(getNameView(alarm));
			linearLayout.addView(getTimeView(alarm));
			linearLayout.addView(getSettingImages(alarm));
		}
		
		if((alarm.getID()==this.ALARM_ADDER_ID))
		{
			ImageView plusSign = new ImageView(this);
			plusSign.setImageDrawable(this.getResources().getDrawable(R.drawable.plus));
			linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
			linearLayout.setGravity(Gravity.CENTER_VERTICAL);
			linearLayout.addView(plusSign);
		}
		
		linearLayout.setId(alarm.getID());
		linearLayout.setClickable(true);
		linearLayout.setOnClickListener(this);
		
		linearLayout.setOnLongClickListener(this);
		
		return linearLayout;
	}
	
	@SuppressLint("NewApi") private int getScreenWidth()
	{
		int width = 0;
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1){
		    //Do something for API 17 only (4.2)
			display.getRealSize(size);
			width = size.x;
		}
		else if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2){
		    // Do something for API 13 and above , but below API 17 (API 17 will trigger the above block
		    //getSize()
			display.getSize(size);
			width = size.x;
		} else{
		    // do something for phones running an SDK before Android 3.2 (API 13)
		    //getWidth(), getHeight()
			width=display.getWidth();
		}
		
		return width;
	}
	
	private TextView getTimeView(Alarm alarm) throws ParseException
	{ 
		TextView timeView = new TextView(this);
		LayoutParams timeLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		timeView.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
		Log.d("AlarmClock","Time text view of time "+alarm.getHour()+":"+alarm.getMinute());
		timeView.setText(militaryToTwelveHour(alarm.getHour(),alarm.getMinute()));
		
		return timeView;
	}
	
	private TextView getNameView(Alarm alarm)
	{
		TextView timeView = new TextView(this);
		LayoutParams timeLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		timeView.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
		timeView.setText(alarm.getName());
		
		return timeView;
	}
	
	private LinearLayout getSettingImages(Alarm alarm)
	{
		LinearLayout settingsLinearLayout = new LinearLayout(this);
		settingsLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams settingsLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT); 
		
		ImageView rssNewsFeedImageView = new ImageView(this);
		rssNewsFeedImageView.setId(1);
		rssNewsFeedImageView.setBackgroundResource(R.drawable.newsfeed_icon);
		LayoutParams rssNewsFeedLayoutParams = new LayoutParams(30,30);
		rssNewsFeedLayoutParams.setMargins(1, 1, 1, 1);
		rssNewsFeedImageView.setLayoutParams(rssNewsFeedLayoutParams);
		if(!alarm.getRssNewFeedOption())rssNewsFeedImageView.getBackground().setAlpha(50);
		
		ImageView musicImageView = new ImageView(this);
		musicImageView.setId(2);
		musicImageView.setBackgroundResource(R.drawable.music_icon);
		LayoutParams musicLayoutParams = new LayoutParams(30,30);
		musicLayoutParams.setMargins(1, 1, 1, 1);
		musicImageView.setLayoutParams(musicLayoutParams);
		if(!alarm.getMusicOption())musicImageView.getBackground().setAlpha(50);
		
		ImageView textContactsImageView = new ImageView(this);
		textContactsImageView.setId(3);
		textContactsImageView.setBackgroundResource(R.drawable.text_contacts);
		LayoutParams textContactsLayoutParams = new LayoutParams(30,30);
		textContactsLayoutParams.setMargins(1, 1, 1, 1);
		textContactsImageView.setLayoutParams(textContactsLayoutParams);
		if(!alarm.getTextContactsOption())textContactsImageView.getBackground().setAlpha(50);
		
		ImageView shakeToWakeImageView = new ImageView(this);
		shakeToWakeImageView.setId(4);
		shakeToWakeImageView.setBackgroundResource(R.drawable.shake_to_wake_icon);
		LayoutParams shakeToWakeImageViewParams = new LayoutParams(30,30);
		shakeToWakeImageViewParams.setMargins(1, 1, 1, 1);
		shakeToWakeImageView.setLayoutParams(shakeToWakeImageViewParams);
		if(!alarm.getShakeToWakeOption())shakeToWakeImageView.getBackground().setAlpha(50);
			
		settingsLinearLayout.addView(rssNewsFeedImageView);
		settingsLinearLayout.addView(musicImageView);
		settingsLinearLayout.addView(textContactsImageView);
		settingsLinearLayout.addView(shakeToWakeImageView);
		
		return settingsLinearLayout;
	}
	
	String militaryToTwelveHour(int hour, int minute) throws ParseException
	{
		String _24HourTime;
		
		if(hour>=10)
		{
			_24HourTime = Integer.toString(hour)+":"+Integer.toString(minute);
		}
		else
		{
			_24HourTime = "0"+Integer.toString(hour)+":"+Integer.toString(minute);
		}
		
        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
        Date _24HourDt = _24HourSDF.parse(_24HourTime);
        
        String outTime = _12HourSDF.format(_24HourDt);
        if(outTime.charAt(0)=='0')
        {
        	outTime = outTime.substring(1);
        }
        
        return outTime;
	}

	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub
		
	}
	

	
	private Alarm toggleAlarm(Alarm alarm, boolean forceDelete)
	{
		if(!forceDelete)
		{
			for(int i = 0;i<alarms.size();i++)
			{
				if(alarms.get(i).getID()==alarm.getID())
				{
					if(alarms.get(i).enabled())
					{
						alarmFactory.cancelAlarm(alarms.get(i));
						alarms.get(i).disableAlarm();
					}else
					{
						alarmFactory.setAlarm(alarms.get(i));
						alarms.get(i).enableAlarm();
					}
					
					PreferencesHandler prefsHandler = new PreferencesHandler(this);
					prefsHandler.setAlarms(alarms);
					
					return alarms.get(i);
				}
			}
		}else
		{
			alarmFactory.cancelAlarm(alarm);

			removeAlarmFromList(alarm);
			
			PreferencesHandler prefsHandler = new PreferencesHandler(this);
			prefsHandler.setAlarms(alarms);
			
			alarm = null;
			
			redrawScreen();
		}
		
		return alarm;
	}
	
	private void deleteAlarm(Alarm alarm)
	{
		toggleAlarm(alarm, true);
	}
	
	private void redrawScreen()
	{
		try {
			createAlarmsViews();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void removeAlarmFromList(Alarm alarm)
	{
		
		for (int i =0 ; i<alarms.size();i++)
		{
			if(alarms.get(i).getID()==alarm.getID())
			{
				alarms.remove(i);
			}
		}
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		/*if(GlobalStaticVariables.TURN_OFF_APP) 
		{
			GlobalStaticVariables.TURN_OFF_APP =false;
			//finish();
		}*/
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		if(GlobalStaticVariables.TURN_OFF_APP) 
		{
			GlobalStaticVariables.TURN_OFF_APP=false;
			finish();
		}
	}
}