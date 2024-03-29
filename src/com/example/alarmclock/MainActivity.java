package com.example.alarmclock;

import android.net.Uri;
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
import android.view.KeyEvent;
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
import java.util.HashMap;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import com.example.alarmclock.MyTextToSpeech;

//add a options selector
public class MainActivity extends Activity implements TextToSpeech.OnInitListener,OnClickListener, OnLongClickListener {
	
	private ArrayList<Alarm> alarms;
	public static final int ALARM_ADDER_ID = -100;
	public static final String IS_NEW_ALARM = "NEWALARM";
	private int longClickedAlarmId;
	
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
	
	public RelativeLayout addButton;
	
	public boolean powersState[] = new boolean[9];
	public boolean dockMusicState[] = new boolean[9];
	public boolean dockNewsState[] = new boolean[9];
	public boolean dockTextState[] = new boolean[9];
	public boolean dockShakeState[] = new boolean[9];
	
	public HashMap<Integer,Integer> AlarmPositionToID = new HashMap<Integer,Integer>();
	
	public static Context CONTEXT = null;
	
	PreferencesHandler prefsHandler;
	Preferences prefs;
	
	//id must be in this format
	public static final String APP_ID = "com.wakeywakey.alarmclock";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		GlobalStaticVariables.TURN_OFF_APP = false;
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_main);
		
		MainActivity.CONTEXT = this;
		
		prefsHandler = new PreferencesHandler(this);
		prefs = prefsHandler.getSettings();
		
		if(prefs.getIsFirstBoot())
		{ 
			//hashim this is the first boot try not to exit this activity to show the tutorial. if you do then 
			//write prefsHandler.setIsFirstBoot(false); when the tutorial is finished.
			Log.d("bootup","first boot");
			LinearLayout bootup = (LinearLayout) findViewById(R.id.introtutorial);
			bootup.setVisibility(View.VISIBLE);
			
			//set first boot to false
			prefsHandler.setIsFirstBoot(false);
		}else{
			Log.d("bootup","not first boot");
			LinearLayout bootup = (LinearLayout) findViewById(R.id.introtutorial);
			bootup.setVisibility(View.GONE);
		}
		this.alarms = new ArrayList<Alarm>();
		this.alarms=prefs.getAlarms();
		
		if(!AlarmFactory.isInit)AlarmFactory.init();
		AlarmFactory.confirmAlarms(alarms);
		
		// change fonts for time
		mainView = (ScrollView) findViewById(R.id.mainScrollView);
		overrideFonts(this, mainView);
		mainView = (ScrollView) findViewById(R.id.mainScrollView);
		
		try {
			initializeFormComponentsWrapper();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		addOnClickListeners();
		
		if(prefs.getNumberOfAlarmsSet()%5==0&&prefs.getNumberOfAlarmsSet()>0)
		{
			showRatingDialog();
		}
		
	}
	
	private boolean startAnyActivity(Intent aIntent) {
	    try
	    {
	        startActivity(aIntent);
	        return true;
	    }
	    catch (ActivityNotFoundException e)
	    {
	        return false;
	    }
	}
	 
	//On click event for rate this app button
	public void openRatingsSetter() {
	    Intent intent = new Intent(Intent.ACTION_VIEW);
	    //Try Google play
	    intent.setData(Uri.parse("market://details?id="+APP_ID));
	    if (startAnyActivity(intent) == false) {
	        //Market (Google play) app seems not installed, let's try to open a webbrowser
	        intent.setData(Uri.parse("https://play.google.com/store/apps/details?"+APP_ID));
	        if (startAnyActivity(intent) == false) {
	            //Well if this also fails, we have run out of options, inform the user.
	            Toast.makeText(this, "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
	        }
	    }
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
		//getMenuInflater().inflate(R.menu.main, menu);
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
			
			for(int x = 1 ;x<docks.length;x++)
			{
				if(!(x==i))
				{
					docks[x].setVisibility(View.GONE);
				}
			}
		}
		
		// TOGGLES DOCK SETTINGS
		if (whichPower(v) != -1){
			int i = whichPower(v);
			if (powersState[i]){
				powersState[i] = false;
				powers[i].setImageResource(R.drawable.buttonoff);
			} else {
				powersState[i] = true;
				powers[i].setImageResource(R.drawable.buttonon);
			}
			this.toggleAlarm(this.getAlarmByID(getAlarmIdFromPosition(i)), false);
			
		} else if (whichDockMusic(v) != -1){
			int i = whichDockMusic(v);
			if (dockMusicState[i]){
				dockMusicState[i] = false;
				dockMusic[i].setImageResource(R.drawable.musicoff);
				dockNewsState[i] = true;
				dockNews[i].setImageResource(R.drawable.newsfeedon);
				//TODO: change alarm i's preferences
				Alarm tempAlrm = getAlarmByID(getAlarmIdFromPosition(i));
				tempAlrm.setMusicOption(false);
				tempAlrm.setRssNewsFeedOption(true);
				saveAlarmDockSettings(tempAlrm);
			} else {
				dockMusicState[i] = true;
				dockMusic[i].setImageResource(R.drawable.musicon);
				dockNewsState[i] = false;
				dockNews[i].setImageResource(R.drawable.newsfeedoff);
				//TODO: change alarm i's preferences
				Alarm tempAlrm = getAlarmByID(getAlarmIdFromPosition(i));
				tempAlrm.setMusicOption(true);
				tempAlrm.setRssNewsFeedOption(false);
				saveAlarmDockSettings(tempAlrm);
			}
		} else if (whichDockNews(v) != -1){
			int i = whichDockNews(v);
			if (dockNewsState[i]){
				dockNewsState[i] = false;
				dockNews[i].setImageResource(R.drawable.newsfeedoff);
				dockMusicState[i] = true;
				dockMusic[i].setImageResource(R.drawable.musicon);
				//TODO: change alarm i's preferences
				Alarm tempAlrm = getAlarmByID(getAlarmIdFromPosition(i));
				tempAlrm.setMusicOption(true);
				tempAlrm.setRssNewsFeedOption(false);
				saveAlarmDockSettings(tempAlrm);
			} else {
				dockNewsState[i] = true;
				dockNews[i].setImageResource(R.drawable.newsfeedon);
				dockMusicState[i] = false;
				dockMusic[i].setImageResource(R.drawable.musicoff);
				//TODO: change alarm i's preferences
				Alarm tempAlrm = getAlarmByID(getAlarmIdFromPosition(i));
				tempAlrm.setMusicOption(false);
				tempAlrm.setRssNewsFeedOption(true);
				saveAlarmDockSettings(tempAlrm);
			}
		} else if (whichDockText(v) != -1){
			int i = whichDockText(v);
			if (dockTextState[i]){
				dockTextState[i] = false;
				dockText[i].setImageResource(R.drawable.messagingoff);
				//TODO: change alarm i's preferences
				Alarm tempAlrm = getAlarmByID(getAlarmIdFromPosition(i));
				tempAlrm.setTextContactsOption(false);
				saveAlarmDockSettings(tempAlrm);
			} else {
				dockTextState[i] = true;
				dockText[i].setImageResource(R.drawable.messagingon);
				//TODO: change alarm i's preferences
				Alarm tempAlrm = getAlarmByID(getAlarmIdFromPosition(i));
				tempAlrm.setTextContactsOption(true);
				saveAlarmDockSettings(tempAlrm);
			}
		} else if (whichDockShake(v) != -1){
			int i = whichDockShake(v);
			if (dockShakeState[i]){
				dockShakeState[i] = false;
				dockShake[i].setImageResource(R.drawable.shakeoff);
				//TODO: change alarm i's preferences
				Alarm tempAlrm = getAlarmByID(getAlarmIdFromPosition(i));
				tempAlrm.setShakeToWakeOption(false);
				saveAlarmDockSettings(tempAlrm);
			} else {
				dockShakeState[i] = true;
				dockShake[i].setImageResource(R.drawable.shakeon);
				//TODO: change alarm i's preferences
				Alarm tempAlrm = getAlarmByID(getAlarmIdFromPosition(i));
				tempAlrm.setShakeToWakeOption(true);
				saveAlarmDockSettings(tempAlrm);
			}
		} else if (whichDockTrash(v) != -1){
			int i = whichDockTrash(v);
			deleteAlarm(getAlarmByID(getAlarmIdFromPosition(i)));
			frames[i].setVisibility(View.GONE);
		} else if (whichDockGear(v) != -1){
			int i = whichDockGear(v);
			this.startSettingsActivity(getAlarmByID(getAlarmIdFromPosition(i)),false);
		}
		
		//add alarm buton
		if(v.getId() == R.id.addButon)
		{
			v.setBackgroundColor(getResources().getColor(R.color.fontcolor));

			if(alarms.size()<8)
			{
				Time now = new Time();
				now.setToNow();
				
				Alarm newAlarm = new Alarm(now.hour, now.minute, getNewID());
				newAlarm.enableAlarm();
				alarms.add(newAlarm);
				
				PreferencesHandler prefsHandler = new PreferencesHandler(this);
				prefsHandler.setAlarms(alarms);
				
				startSettingsActivity(newAlarm,true);
			}else
			{
				Toast.makeText(getApplicationContext(), "Maximum of 8 alarms!", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	void startSettingsActivity(Alarm alarm,boolean isNewAlarm)
	{
		Intent i = new Intent(this, SettingsActivity.class);
		i.putExtra(AlarmFactory.ALARM_ID, Integer.toString(alarm.getID()));
		if(isNewAlarm)
		{
			i.putExtra(IS_NEW_ALARM, "true");
		}else{
			i.putExtra(IS_NEW_ALARM, "false");
		}
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
						Log.d("AlarmClock","MainActivity: canceling alarm");
						if(!AlarmFactory.isInit)AlarmFactory.init();
						AlarmFactory.cancelAlarm(alarms.get(i));
						alarms.get(i).disableAlarm();
					}else
					{
						Log.d("AlarmClock","MainActivity: enabling alarm");
						if(!AlarmFactory.isInit)AlarmFactory.init();
						AlarmFactory.setAlarm(alarms.get(i));
						alarms.get(i).enableAlarm();
					}
					
					PreferencesHandler prefsHandler = new PreferencesHandler(this);
					prefsHandler.setAlarms(alarms);
					
					return alarms.get(i);
				}
			}
		}else
		{
			Log.d("AlarmClock","MainActivity: canceling alarm");
			if(!AlarmFactory.isInit)AlarmFactory.init();
			AlarmFactory.cancelAlarm(alarm);

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
			Log.d("AlarmClock","Turn off app since global variable is true");
			finish();
		}
	}

public void initializeFormComponentsWrapper() throws ParseException{
		initializeFormComponents();
		populateFramesWithAlarms();
		addButton = (RelativeLayout)findViewById(R.id.addButon);
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
	
	public void populateFramesWithAlarms() throws ParseException
	{
		int ammountOfAlarms = alarms.size();
		
		for(int i =1 ;i<9;i++)
		{
			if(i<=ammountOfAlarms)
			{
				Alarm tempAlarm = alarms.get(i-1);
				
				times[i].setText(militaryToTwelveHour(tempAlarm.getHour(),tempAlarm.getMinute()));
				labels[i].setText(tempAlarm.getName());
				
				if(tempAlarm.isRepeatedDaily())
				{
					repeats[i].setText("Repeat: Daily");
				}else
				{
					repeats[i].setText("Repeat: Single");
				}
				
				if(tempAlarm.enabled())
				{
					powersState[i] = true;
					powers[i].setImageResource(R.drawable.buttonon);
				}else
				{
					powersState[i] = false;
					powers[i].setImageResource(R.drawable.buttonoff);
				}
				
				if(tempAlarm.getRssNewFeedOption())
				{
					dockMusicState[i] = false;
					dockMusic[i].setImageResource(R.drawable.musicoff);
					
					dockNewsState[i] = true;
					dockNews[i].setImageResource(R.drawable.newsfeedon);
				}else
				{
					dockMusicState[i] = true;
					dockMusic[i].setImageResource(R.drawable.musicon);
					
					dockNewsState[i] = false;
					dockNews[i].setImageResource(R.drawable.newsfeedoff);
				}
				
				if(tempAlarm.getTextContactsOption())
				{
					dockTextState[i] = true;
					dockText[i].setImageResource(R.drawable.messagingon);
				}else
				{
					dockTextState[i] = false;
					dockText[i].setImageResource(R.drawable.messagingoff);
				}
				
				if(tempAlarm.getShakeToWakeOption())
				{
					dockShakeState[i] = true;
					dockShake[i].setImageResource(R.drawable.shakeon);
				}else
				{
					dockShakeState[i] = false;
					dockShake[i].setImageResource(R.drawable.shakeoff);
				}
				
				docks[i].setVisibility(View.GONE);
				
				AlarmPositionToID.put(i, tempAlarm.getID());
			}else
			{
				frames[i].setVisibility(View.GONE);
			}
		}
	}
	
	private int getAlarmIdFromPosition(int position)
	{
		return AlarmPositionToID.get(position);
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
		addButton.setOnClickListener(this);
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

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void saveAlarmDockSettings(Alarm alarm)
	{
		for(int i = 0 ; i< alarms.size();i++)
		{
			if(alarms.get(i).getID() == alarm.getID())
			{
				alarms.set(i, alarm);	
			}
		}
		PreferencesHandler prefsHandler = new PreferencesHandler(this);
		prefsHandler.setAlarms(alarms);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	       GlobalStaticVariables.TURN_OFF_APP = false;
	       finish();
	    }
	    if ((keyCode == KeyEvent.KEYCODE_HOME)) {
	    	GlobalStaticVariables.TURN_OFF_APP = false;
	        finish();
	    }
	    if ((keyCode == KeyEvent.KEYCODE_APP_SWITCH)) {
	    	GlobalStaticVariables.TURN_OFF_APP = false;
	        finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	private void showRatingDialog()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
 
			// set title
			alertDialogBuilder.setTitle("Rate!");
 
			// set dialog message
			alertDialogBuilder
				.setMessage("Would you like to rate this App?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						prefsHandler.disableIncrementNumberOfAlarmsSet();
						openRatingsSetter();
						dialog.cancel();
					}
				  })
				.setNegativeButton("Don't Show Again",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						prefsHandler.disableIncrementNumberOfAlarmsSet();
						dialog.cancel();
					}
				});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
	}
}