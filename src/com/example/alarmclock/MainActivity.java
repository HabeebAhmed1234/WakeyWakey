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
import android.view.ViewGroup;
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
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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

	private ScrollView mainView;
	
	public TextView labels[] = new TextView[9];
	public TextView repeats[] = new TextView[9];
	public TextView times[] = new TextView[9];
	public ImageView powers[] = new ImageView[9];
	public RelativeLayout innerframes[] = new RelativeLayout[9];
	public LinearLayout docks[] = new LinearLayout[9];
	public ImageView dockMusic[] = new ImageView[9];
	public ImageView dockNews[] = new ImageView[9];
	public ImageView dockText[] = new ImageView[9];
	public ImageView dockShake[] = new ImageView[9];
	public ImageView dockTrash[] = new ImageView[9];
	public ImageView dockGear[] = new ImageView[9];
	public LinearLayout frames[] = new LinearLayout[9];
	
	public boolean powersState[] = new boolean[9];
	public boolean dockMusicState[] = new boolean[9];
	public boolean dockNewsState[] = new boolean[9];
	public boolean dockTextState[] = new boolean[9];
	public boolean dockShakeState[] = new boolean[9];


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		this.setContentView(R.layout.activity_main);

		initializeFormComponentsWrapper();
		
		alarmFactory = new AlarmFactory(this);
		
		PreferencesHandler prefsHandler = new PreferencesHandler(this);
		Preferences prefs = prefsHandler.getSettings();
		
		alarms = new ArrayList();
		
		this.alarms=prefs.getAlarms();
		
		// change fonts for time
		mainView = (ScrollView) findViewById(R.id.mainScrollView);
		overrideFonts(this, mainView);
		
		addOnClickListeners();
		
		mainView = (ScrollView) findViewById(R.id.mainScrollView);
	}

	private void overrideFonts(final Context context, final View v) {
	    try {
	        if (v instanceof ViewGroup) {
	            ViewGroup vg = (ViewGroup) v;
	            for (int i = 0; i < vg.getChildCount(); i++) {
	                View child = vg.getChildAt(i);
	                overrideFonts(context, child);
	         }
	        } else if (v instanceof TextView ) {
	            if (getResources().getResourceName(v.getId()).contains("time")) ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "DS-DIGI.TTF"));
	        }
	    } catch (Exception e) {
	 }
	 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.d("debugger",v.getId()+" was clicked");
		
// OPENS/CLOSES DOCK
		if (whichInnerframe(v) != -1){
			int i = whichInnerframe(v);
			if (docks[i].getVisibility() == View.GONE) docks[i].setVisibility(View.VISIBLE);
			else docks[i].setVisibility(View.GONE);
		}
		
		// TOGGLES DOCK SETTINGS
		if (whichPower(v) != -1){
			int i = whichPower(v);
			if (powersState[i]){
				powersState[i] = false;
				powers[i].setImageResource(R.drawable.buttonoff);
				//TODO: change alarm i's preferences
			} else {
				powersState[i] = true;
				powers[i].setImageResource(R.drawable.buttonon);
				//TODO: change alarm i's preferences
			}
		} else if (whichDockMusic(v) != -1){
			int i = whichDockMusic(v);
			if (dockMusicState[i]){
				dockMusicState[i] = false;
				dockMusic[i].setImageResource(R.drawable.musicoff);
				dockNewsState[i] = true;
				dockNews[i].setImageResource(R.drawable.newsfeedon);
				//TODO: change alarm i's preferences
			} else {
				dockMusicState[i] = true;
				dockMusic[i].setImageResource(R.drawable.musicon);
				dockNewsState[i] = false;
				dockNews[i].setImageResource(R.drawable.newsfeedoff);
				//TODO: change alarm i's preferences
			}
		} else if (whichDockNews(v) != -1){
			int i = whichDockNews(v);
			if (dockNewsState[i]){
				dockNewsState[i] = false;
				dockNews[i].setImageResource(R.drawable.newsfeedoff);
				dockMusicState[i] = true;
				dockMusic[i].setImageResource(R.drawable.musicon);
				//TODO: change alarm i's preferences
			} else {
				dockNewsState[i] = true;
				dockNews[i].setImageResource(R.drawable.newsfeedon);
				dockMusicState[i] = false;
				dockMusic[i].setImageResource(R.drawable.musicoff);
				//TODO: change alarm i's preferences
			}
		} else if (whichDockText(v) != -1){
			int i = whichDockText(v);
			if (dockTextState[i]){
				dockTextState[i] = false;
				dockText[i].setImageResource(R.drawable.messagingoff);
				//TODO: change alarm i's preferences
			} else {
				dockTextState[i] = true;
				dockText[i].setImageResource(R.drawable.messagingon);
				//TODO: change alarm i's preferences
			}
		} else if (whichDockShake(v) != -1){
			int i = whichDockShake(v);
			if (dockShakeState[i]){
				dockShakeState[i] = false;
				dockShake[i].setImageResource(R.drawable.shakeoff);
				//TODO: change alarm i's preferences
			} else {
				dockShakeState[i] = true;
				dockShake[i].setImageResource(R.drawable.shakeon);
				//TODO: change alarm i's preferences
			}
		} else if (whichDockTrash(v) != -1){
			int i = whichDockTrash(v);
			// TODO: DELETE ALARM i
		} else if (whichDockGear(v) != -1){
			int i = whichDockGear(v);
			// TODO: GO TO SETTINGS FOR ALARM i
		}




		/*int ID = v.getId();
		
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
		v.startAnimation(alpha);*/
	}
	
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

public void initializeFormComponentsWrapper(){
		initializeFormComponents();
		
		initializeToTrue(dockMusicState);
		
		initializeToFalse(powersState);
		initializeToFalse(dockNewsState);
		initializeToFalse(dockTextState);
		initializeToFalse(dockShakeState);
	}
	
	public void initializeFormComponents(){
		// labels
		labels[1] = (TextView) findViewById(R.id.label1);
		labels[2] = (TextView) findViewById(R.id.label2);
		labels[3] = (TextView) findViewById(R.id.label3);
		labels[4] = (TextView) findViewById(R.id.label4);
		labels[5] = (TextView) findViewById(R.id.label5);
		labels[6] = (TextView) findViewById(R.id.label6);
		labels[7] = (TextView) findViewById(R.id.label7);
		labels[8] = (TextView) findViewById(R.id.label8);
		
		// repeats
		repeats[1] = (TextView) findViewById(R.id.repeat1);
		repeats[2] = (TextView) findViewById(R.id.repeat2);
		repeats[3] = (TextView) findViewById(R.id.repeat3);
		repeats[4] = (TextView) findViewById(R.id.repeat4);
		repeats[5] = (TextView) findViewById(R.id.repeat5);
		repeats[6] = (TextView) findViewById(R.id.repeat6);
		repeats[7] = (TextView) findViewById(R.id.repeat7);
		repeats[8] = (TextView) findViewById(R.id.repeat8);

		// times
		times[1] = (TextView) findViewById(R.id.time1);
		times[2] = (TextView) findViewById(R.id.time2);
		times[3] = (TextView) findViewById(R.id.time3);
		times[4] = (TextView) findViewById(R.id.time4);
		times[5] = (TextView) findViewById(R.id.time5);
		times[6] = (TextView) findViewById(R.id.time6);
		times[7] = (TextView) findViewById(R.id.time7);
		times[8] = (TextView) findViewById(R.id.time8);

		// power
		powers[1] = (ImageView) findViewById(R.id.power1);
		powers[2] = (ImageView) findViewById(R.id.power2);
		powers[3] = (ImageView) findViewById(R.id.power3);
		powers[4] = (ImageView) findViewById(R.id.power4);
		powers[5] = (ImageView) findViewById(R.id.power5);
		powers[6] = (ImageView) findViewById(R.id.power6);
		powers[7] = (ImageView) findViewById(R.id.power7);
		powers[8] = (ImageView) findViewById(R.id.power8);

		// innerframes
		innerframes[1] = (RelativeLayout) findViewById(R.id.innerframe1);
		innerframes[2] = (RelativeLayout) findViewById(R.id.innerframe2);
		innerframes[3] = (RelativeLayout) findViewById(R.id.innerframe3);
		innerframes[4] = (RelativeLayout) findViewById(R.id.innerframe4);
		innerframes[5] = (RelativeLayout) findViewById(R.id.innerframe5);
		innerframes[6] = (RelativeLayout) findViewById(R.id.innerframe6);
		innerframes[7] = (RelativeLayout) findViewById(R.id.innerframe7);
		innerframes[8] = (RelativeLayout) findViewById(R.id.innerframe8);

		// docks
		docks[1] = (LinearLayout) findViewById(R.id.dock1);
		docks[2] = (LinearLayout) findViewById(R.id.dock2);
		docks[3] = (LinearLayout) findViewById(R.id.dock3);
		docks[4] = (LinearLayout) findViewById(R.id.dock4);
		docks[5] = (LinearLayout) findViewById(R.id.dock5);
		docks[6] = (LinearLayout) findViewById(R.id.dock6);
		docks[7] = (LinearLayout) findViewById(R.id.dock7);
		docks[8] = (LinearLayout) findViewById(R.id.dock8);

		// dockMusic
		dockMusic[1] = (ImageView) findViewById(R.id.dockMusic1);
		dockMusic[2] = (ImageView) findViewById(R.id.dockMusic2);
		dockMusic[3] = (ImageView) findViewById(R.id.dockMusic3);
		dockMusic[4] = (ImageView) findViewById(R.id.dockMusic4);
		dockMusic[5] = (ImageView) findViewById(R.id.dockMusic5);
		dockMusic[6] = (ImageView) findViewById(R.id.dockMusic6);
		dockMusic[7] = (ImageView) findViewById(R.id.dockMusic7);
		dockMusic[8] = (ImageView) findViewById(R.id.dockMusic8);

		// dockNews
		dockNews[1] = (ImageView) findViewById(R.id.dockNews1);
		dockNews[2] = (ImageView) findViewById(R.id.dockNews2);
		dockNews[3] = (ImageView) findViewById(R.id.dockNews3);
		dockNews[4] = (ImageView) findViewById(R.id.dockNews4);
		dockNews[5] = (ImageView) findViewById(R.id.dockNews5);
		dockNews[6] = (ImageView) findViewById(R.id.dockNews6);
		dockNews[7] = (ImageView) findViewById(R.id.dockNews7);
		dockNews[8] = (ImageView) findViewById(R.id.dockNews8);
		
		// dockText
		dockText[1] = (ImageView) findViewById(R.id.dockText1);
		dockText[2] = (ImageView) findViewById(R.id.dockText2);
		dockText[3] = (ImageView) findViewById(R.id.dockText3);
		dockText[4] = (ImageView) findViewById(R.id.dockText4);
		dockText[5] = (ImageView) findViewById(R.id.dockText5);
		dockText[6] = (ImageView) findViewById(R.id.dockText6);
		dockText[7] = (ImageView) findViewById(R.id.dockText7);
		dockText[8] = (ImageView) findViewById(R.id.dockText8);

		// dockShake
		dockShake[1] = (ImageView) findViewById(R.id.dockShake1);
		dockShake[2] = (ImageView) findViewById(R.id.dockShake2);
		dockShake[3] = (ImageView) findViewById(R.id.dockShake3);
		dockShake[4] = (ImageView) findViewById(R.id.dockShake4);
		dockShake[5] = (ImageView) findViewById(R.id.dockShake5);
		dockShake[6] = (ImageView) findViewById(R.id.dockShake6);
		dockShake[7] = (ImageView) findViewById(R.id.dockShake7);
		dockShake[8] = (ImageView) findViewById(R.id.dockShake8);

		// dockTrash
		dockTrash[1] = (ImageView) findViewById(R.id.dockTrash1);
		dockTrash[2] = (ImageView) findViewById(R.id.dockTrash2);
		dockTrash[3] = (ImageView) findViewById(R.id.dockTrash3);
		dockTrash[4] = (ImageView) findViewById(R.id.dockTrash4);
		dockTrash[5] = (ImageView) findViewById(R.id.dockTrash5);
		dockTrash[6] = (ImageView) findViewById(R.id.dockTrash6);
		dockTrash[7] = (ImageView) findViewById(R.id.dockTrash7);
		dockTrash[8] = (ImageView) findViewById(R.id.dockTrash8);

		// dockGear
		dockGear[1] = (ImageView) findViewById(R.id.dockGear1);
		dockGear[2] = (ImageView) findViewById(R.id.dockGear2);
		dockGear[3] = (ImageView) findViewById(R.id.dockGear3);
		dockGear[4] = (ImageView) findViewById(R.id.dockGear4);
		dockGear[5] = (ImageView) findViewById(R.id.dockGear5);
		dockGear[6] = (ImageView) findViewById(R.id.dockGear6);
		dockGear[7] = (ImageView) findViewById(R.id.dockGear7);
		dockGear[8] = (ImageView) findViewById(R.id.dockGear8);

		// frames
		frames[1] = (LinearLayout) findViewById(R.id.frame1);
		frames[2] = (LinearLayout) findViewById(R.id.frame2);
		frames[3] = (LinearLayout) findViewById(R.id.frame3);
		frames[4] = (LinearLayout) findViewById(R.id.frame4);
		frames[5] = (LinearLayout) findViewById(R.id.frame5);
		frames[6] = (LinearLayout) findViewById(R.id.frame6);
		frames[7] = (LinearLayout) findViewById(R.id.frame7);
		frames[8] = (LinearLayout) findViewById(R.id.frame8);
	}

	public void addOnClickListeners(){
		for (int i=1; i<=8; i++){
			powers[i].setOnClickListener(this);
			dockMusic[i].setOnClickListener(this);
			dockNews[i].setOnClickListener(this);
			dockText[i].setOnClickListener(this);
			dockShake[i].setOnClickListener(this);
			dockTrash[i].setOnClickListener(this);
			dockGear[i].setOnClickListener(this);
			innerframes[i].setOnClickListener(this);
		}
	}
	
	public int whichPower(View v){
		for (int i=1; i<=8; i++){
			if (v == powers[i]) return i;
		}
		return -1;
	}
	
	public int whichInnerframe(View v){
		for (int i=1; i<=8; i++){
			if (v == innerframes[i]) return i;
		}
		return -1;
	}

	public int whichDockMusic(View v){
		for (int i=1; i<=8; i++){
			if (v == dockMusic[i]) return i;
		}
		return -1;
	}
	
	public int whichDockNews(View v){
		for (int i=1; i<=8; i++){
			if (v == dockNews[i]) return i;
		}
		return -1;
	}
	
	public int whichDockText(View v){
		for (int i=1; i<=8; i++){
			if (v == dockText[i]) return i;
		}
		return -1;
	}
	
	public int whichDockShake(View v){
		for (int i=1; i<=8; i++){
			if (v == dockShake[i]) return i;
		}
		return -1;
	}
	
	public int whichDockTrash(View v){
		for (int i=1; i<=8; i++){
			if (v == dockTrash[i]) return i;
		}
		return -1;
	}
	
	public int whichDockGear(View v){
		for (int i=1; i<=8; i++){
			if (v == dockGear[i]) return i;
		}
		return -1;
	}
	
	public void initializeToTrue(boolean arr[]){
		for (int i=0; i<arr.length; i++){
			arr[i] = true;
		}
	}
	
	public void initializeToFalse(boolean arr[]){
		for (int i=0; i<arr.length; i++){
			arr[i] = false;
		}
	}
}