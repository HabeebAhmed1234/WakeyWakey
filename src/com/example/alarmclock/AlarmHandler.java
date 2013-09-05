package com.example.alarmclock;

import java.util.ArrayList;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AlarmHandler extends Activity implements AlarmHandlerInterface {
	
	public TextView fbPost;
	public RelativeLayout fbFrame;
	public LinearLayout screen;
	public LinearLayout snoozeButton;
	public LinearLayout offButton;
	public TextView snoozeText;
	public TextView offText;
	public LinearLayout shakeScreen;
	public CustomImageView battery;
	
	private PreferencesHandler prefsHandler;
	private Preferences prefs;
	private Alarm selectedAlarm;
	
	public ShakeToWakeActivity shaker;
	private TextContactsAlertActivity texter;
	private MusicAlertActivity player;
	private FacebookAlertActivity fbProfile;
	
	private boolean facebookRadio;
	private boolean textContacts;
	private boolean shakeToWake;
	
	private boolean playerAlreadyStarted = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_handler);
		//fbProfile = new FacebookAlertActivity(this, this);
		
		//Intent intent = new Intent(this, FacebookAlertActivity.class);
		//startActivity(intent);
		int selectedAlarmID = Integer.parseInt(getIntent().getExtras().getString(MainActivity.ALARM_ID));
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
		fbPost = (TextView) findViewById(R.id.fbPost);
		fbPost.setText("John Doe\nThe quick brown fox jumps over the lazy dog.");
		fbFrame = (RelativeLayout) findViewById(R.id.fbFrame);
		screen = (LinearLayout) findViewById(R.id.screen);
		offButton = (LinearLayout) findViewById(R.id.off);
	    snoozeButton = (LinearLayout) findViewById(R.id.snoozeTxt);
	    offText = (TextView) findViewById(R.id.stopText);
	    snoozeText = (TextView) findViewById(R.id.snoozeText);
	       
	    getAlarmSettings();
	    setupScreen();
	    
	    //if (facebookRadio) fbPost.setText(fbProfile.getStory());
	}
	
	private void getAlarmSettings(){
		facebookRadio = selectedAlarm.getFacebookOption();
		shakeToWake = selectedAlarm.getShakeToWakeOption();
		textContacts = selectedAlarm.getTextContactsOption();
	}
	
	private void setupScreen(){
	    if (!facebookRadio) fbFrame.setVisibility(View.GONE); // if facebook radio = OFF (else do nothing)
	    if (shakeToWake){
	    	offText.setText("STOP button disabled - shake to turn off"); // if shaketowake = ON (else do nothing)
	    	offButton.setClickable(false);
	    }
	    if (textContacts) snoozeText.setText("SNOOZE\nWarning: hitting snooze will text your contacts"); // if textContacts = ON (else do nothing)
	    Log.v("DEBUG_TAG", Integer.toString(fbFrame.getHeight()));
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
		
		if (!facebookRadio&&!playerAlreadyStarted) {
			player = new MusicAlertActivity(this, selectedAlarm);
			player.start();
			playerAlreadyStarted = true;
		}
		else {
			player = null;
		}
		
		if (textContacts) texter = new TextContactsAlertActivity(prefsHandler, prefs, selectedAlarm);
		else texter = null;
	}
	
	private void resizeButtons(){
		int spacing = (int) (getResources().getDimension(R.dimen.activity_vertical_margin)*3); 
		Log.d("DEBUG_TAG", Integer.toString(spacing));
		Log.d("DEBUG_TAG", Integer.toString(screen.getHeight()));
		
		int fbFrameHeight = fbFrame.getHeight();
		if (fbFrame.getVisibility() == View.GONE) fbFrameHeight = 0;
		
		int newHeight = (screen.getHeight() - fbFrameHeight - spacing) / 2;
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
		if(!facebookRadio)
		{
			if(player.isPlaying())player.stop();
		}
		
		offText.setText("oh hi there");
		
		GlobalStaticVariables.TURN_OFF_APP = true;
		finish();
	}
	
	public void performSnoozeActivity(final View v) {
		if (texter != null) texter.textAllContacts();
		
		// do other stuff for snooze later...
		// like setting a new alarm all over again, for 5 mins from now with all the same settings as current alarm...
	}

}
