package com.example.alarmclock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.os.PowerManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmHandler extends Activity implements AlarmHandlerInterface {
	
	public RelativeLayout nfFrame;
	public LinearLayout screen;
	public LinearLayout snoozeButton;
	public LinearLayout offButton;
	public TextView snoozeText;
	public TextView offText;
	public LinearLayout shakeScreen;
	public CustomImageView battery;
	public TextView nfpost;
	
	private PreferencesHandler prefsHandler;
	private Preferences prefs;
	private Alarm selectedAlarm;
		
	private MessageList newsfeed;
	public ShakeToWakeActivity shaker;
	private TextContactsAlertActivity texter;
	private MusicAlertActivity player;
	private MyTextToSpeech speaker;
	
	private String finalMessage;
	private boolean rssNewsFeed;
	private boolean textContacts;
	private boolean shakeToWake;
	
	private boolean playerAlreadyStarted = false;
	
	public static int SNOOZE_TIME_IN_MINUTES = 5;
	
	private PowerManager.WakeLock wl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 getWindow().addFlags(
			        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
			        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON|
			        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		 
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
         
		 
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_alarm_handler);

		int selectedAlarmID = Integer.parseInt(getIntent().getExtras().getString(AlarmFactory.ALARM_ID));
		prefsHandler=new PreferencesHandler(this);
		prefs=prefsHandler.getSettings();
		ArrayList <Alarm> allAlarms = prefs.getAlarms();
		for(int i =0 ;i<allAlarms.size();i++)
		{
			if(allAlarms.get(i).getID()==selectedAlarmID)
			{
				selectedAlarm = allAlarms.get(i);
			}
		}
	}

	private void initializeFormComponents(){
		nfFrame = (RelativeLayout) findViewById(R.id.NFFrame);
		screen = (LinearLayout) findViewById(R.id.screen);
		offButton = (LinearLayout) findViewById(R.id.off);
	    snoozeButton = (LinearLayout) findViewById(R.id.snoozeTxt);
	    offText = (TextView) findViewById(R.id.stopText);
	    snoozeText = (TextView) findViewById(R.id.snoozeText);
	    nfpost = (TextView) findViewById(R.id.nfpost);
	    
        getAlarmSettings();
        setupScreen();
        
	    if (rssNewsFeed) {
	    	newsfeed = new MessageList();
	    	speaker = new MyTextToSpeech(this);
	    	updateNewsFeed();	
	    }
	}
	
	private void updateNewsFeed(){
    	// set text for radio
		List<Message> messages = newsfeed.messages;
		finalMessage = "";
		for (int i=0; i<messages.size(); i++){
			 finalMessage += Integer.toString(i+1) + ". " + messages.get(i).getTitle() + "\n" + messages.get(i).getDescription() + "\n\n";
		}
		
		nfpost.setText(finalMessage);
	}
	
	private void getAlarmSettings(){
		rssNewsFeed = selectedAlarm.getRssNewFeedOption();
		Log.d("AlarmClock", Boolean.toString(rssNewsFeed));
		shakeToWake = selectedAlarm.getShakeToWakeOption();
		textContacts = selectedAlarm.getTextContactsOption();
	}
	
	private void setupScreen(){
		Log.d("AlarmClock","NFRadio is "+Boolean.toString(rssNewsFeed));
	    if (!rssNewsFeed) nfFrame.setVisibility(View.GONE); // if rssNewsFeed radio = OFF (else do nothing)
	    if (shakeToWake){
	    	offText.setText("STOP button disabled - shake to turn off"); // if shaketowake = ON (else do nothing)
	    	offButton.setClickable(false);
	    }
	    if (textContacts) snoozeText.setText("SNOOZE\nWarning: hitting snooze will text your contacts"); // if textContacts = ON (else do nothing)
	    Log.v("DEBUG_TAG", Integer.toString(nfFrame.getHeight()));
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		initializeFormComponents();
		resizeButtons();
		
		shaker = null;
		
		if (shakeToWake){
			shaker = new ShakeToWakeActivity(this, this);
			addShakeToWakeScreen();
		}
		
		if (!rssNewsFeed&&!playerAlreadyStarted) {
			player = new MusicAlertActivity(this, selectedAlarm);
			player.start();
			playerAlreadyStarted = true;
		}
		else {
			player = null;
		}
		
		if(textContacts) texter = new TextContactsAlertActivity(prefsHandler, prefs, selectedAlarm);
		else texter = null;
		
		if (rssNewsFeed) startReadingNewsFeed();
	}
	
	public void startReadingNewsFeed(){
		if(speaker==null)return;
		List<Message> messages = newsfeed.messages;
		for (int i=0; i<messages.size(); i++){
			String finalText = messages.get(i).getTitle() + "\n" + messages.get(i).getDescription();
			speaker.say(finalText);
		}
	}
	
	private void resizeButtons(){
		int spacing = (int) (getResources().getDimension(R.dimen.activity_vertical_margin)*3); 
		Log.d("DEBUG_TAG", Integer.toString(spacing));
		Log.d("DEBUG_TAG", Integer.toString(screen.getHeight()));
		
		int nfFrameHeight = nfFrame.getHeight();
		if (nfFrame.getVisibility() == View.GONE) nfFrameHeight = 0;
		
		int newHeight = (screen.getHeight() - nfFrameHeight - spacing) / 2;
		Log.d("DEBUG_TAG", Integer.toString(newHeight));
		LayoutParams offButtonParams = offButton.getLayoutParams();
		LayoutParams snoozeButtonParams = snoozeButton.getLayoutParams();
		
		offButtonParams.height = newHeight;
		snoozeButtonParams.height = newHeight;
	}

	private void addShakeToWakeScreen(){
		shakeScreen = new LinearLayout(this);
		shakeScreen.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		shakeScreen.setBackgroundColor(getResources().getColor(R.color.batteryBG));
		shakeScreen.setOrientation(LinearLayout.VERTICAL);
		shakeScreen.setGravity(Gravity.CENTER);
		screen.addView(shakeScreen, 0);
		
		TextView instructions = new TextView(this);
		instructions.setText("Shake until the battery is full\nto turn off alarm.\n\n");
		instructions.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		instructions.setTextColor(getResources().getColor(R.color.White));
		instructions.setTextSize(getResources().getDimension(R.dimen.activity_small_text));
		instructions.setGravity(Gravity.CENTER);
		shakeScreen.addView(instructions);
		
		battery = new CustomImageView(this, shaker);
		battery.setImageResource(R.drawable.battery_shell);
		battery.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, screen.getHeight()/3));
		battery.setScaleType(ScaleType.CENTER_INSIDE);
		shakeScreen.addView(battery);
		
		// hide shakeToWakeScreen at first (until a vibration has been detected)
		hideShakeToWakeScreen();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_alarm_handler, menu);
		return true;
	}

	@Override
	public void showShakeToWakeScreen() {
		shakeScreen.setVisibility(View.VISIBLE);
		battery.invalidate();
	}

	@Override
	public void hideShakeToWakeScreen() {
		shakeScreen.setVisibility(View.GONE);
	}
	
	@Override
	public void stopAlarm(){
		performStopActivity(null);
	}
	
	public void performStopActivity(final View v) {
		if(!rssNewsFeed)
		{
			if(player.isPlaying())player.stop();
		}
		
		if(!(speaker==null))speaker.stop();
		
		offText.setText("oh hi there");
		
		GlobalStaticVariables.TURN_OFF_APP = true;
		finish();
	}
	
	public void performSnoozeActivity(final View v) {
		if (texter != null) texter.textAllContacts();
			
		Calendar rightNow = Calendar.getInstance();
		
		int snoozeAlarmInMinutes = rightNow.get(Calendar.HOUR_OF_DAY)*60+rightNow.get(Calendar.MINUTE)+SNOOZE_TIME_IN_MINUTES;

		Log.d("AlarmClock","Snooze time minutes is : "+ snoozeAlarmInMinutes);
		int snoozeHour = (snoozeAlarmInMinutes - snoozeAlarmInMinutes%60)/60 ;
		int snoozeMinute = snoozeAlarmInMinutes%60;
		Alarm snoozeAlarm = selectedAlarm;
		snoozeAlarm.setTime(snoozeHour, snoozeMinute);
		AlarmFactory AF = new AlarmFactory(this);
		AF.setAlarm(snoozeAlarm);
		Log.d("AlarmClock","Snooze time is : "+ snoozeAlarm.getHour()+":"+snoozeAlarm.getMinute());
		finish();
		performStopActivity(null);
		// do other stuff for snooze later...
		// like setting a new alarm all over again, for 5 mins from now with all the same settings as current alarm...
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	        Log.d(this.getClass().getName(), "back button pressed");
	        performStopActivity(null);
	    }
	    if ((keyCode == KeyEvent.KEYCODE_HOME)) {
	        Log.d(this.getClass().getName(), "home button pressed");
	        performStopActivity(null);
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
