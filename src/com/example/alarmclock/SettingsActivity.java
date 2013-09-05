package com.example.alarmclock;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.graphics.PorterDuff;

public class SettingsActivity extends Activity {

	ToggleButton toggle_textContacts;
	ToggleButton toggle_FBRadio;
	ToggleButton toggle_ShakeToWake;
	ToggleButton toggle_AlarmEnabled;
	
	RelativeLayout textContactsSmallFrame;
	LinearLayout fbRadioSmallFrame;
	EditText alarmNameEditText;
	Button saveButton;
	Button addContacts;
	Button musicButton;
	
	TextView contactsTextView;
	TextView musicTextView;
	
	TimePicker AlarmTime;
	static final int CONTACT_PICKER_RESULT = 1001;
	private PreferencesHandler prefsHandler;
	boolean saveButtonWasClicked = false;
	
	private ArrayList<Alarm> alarms = new ArrayList<Alarm>();
	private Alarm alarm;
	
	private AlarmFactory alarmFactory;
	// set up responses to the toggle buttons
	public void setupToggleButtons(){
		toggle_textContacts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked) {
		            // The toggle is enabled
		        	LinearLayout.LayoutParams layout_desc = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		            textContactsSmallFrame.setLayoutParams(layout_desc);
		        } else {
		        	// resize the frame to just show the top button
		        	LinearLayout.LayoutParams layout_desc = new LinearLayout.LayoutParams(0, 0);
		            textContactsSmallFrame.setLayoutParams(layout_desc);
		        }
		    }
		});
		
		toggle_FBRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (!isChecked) {
		            // The toggle is disabled
		        	// resize the frame to just show the top button
		        	LinearLayout.LayoutParams layout_desc = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		            fbRadioSmallFrame.setLayoutParams(layout_desc);
		        } else {
		        	// the toggle is enabled
		        	LinearLayout.LayoutParams layout_desc = new LinearLayout.LayoutParams(0, 0);
		            fbRadioSmallFrame.setLayoutParams(layout_desc);
		        }
		    }
		});
	}
	
	private void setupSaveButton(){
		saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				populateAlarmWithFormData();
				saveAlarm();
				Toast.makeText(getApplicationContext(), "Saved Alarm", Toast.LENGTH_LONG).show();
			}
		});
	}
	
	private void startMainMenuActivity()
	{
		Intent i = new Intent(this, MainActivity.class);
		this.startActivity(i);
		finish();
	}
	
	private void populateAlarmWithFormData()
	{
		Log.d("AlarmClock","alarm Values");
		Log.d("AlarmClock","FB radio "+toggle_FBRadio.isChecked());
		Log.d("AlarmClock","music "+!toggle_FBRadio.isChecked());
		Log.d("AlarmClock","Time "+AlarmTime.getCurrentHour()+":"+AlarmTime.getCurrentMinute());
		Log.d("AlarmClock","Name "+alarmNameEditText.getText().toString());
		
		alarm.setFacebookOption(toggle_FBRadio.isChecked());
		alarm.setMusicOption(!toggle_FBRadio.isChecked());
		alarm.setTime(AlarmTime.getCurrentHour(),AlarmTime.getCurrentMinute());
		alarm.setVideoNewsOption(false);
		alarm.setShakeToWakeOption(toggle_ShakeToWake.isChecked());
		alarm.setName(alarmNameEditText.getText().toString());
		if(toggle_AlarmEnabled.isChecked())
		{
			alarm.enableAlarm();
		}else
		{
			alarm.disableAlarm();
		}
		
		if(!toggle_FBRadio.isChecked())
		{
			alarm.setMusicList(GlobalStaticVariables.selectedMusic);
		}
		
		if(this.toggle_textContacts.isChecked())
		{
			alarm.setTextContactsOption(true);
			alarm.setTextContactsList(GlobalStaticVariables.selectedContacts);
		}else
		{
			alarm.setTextContactsOption(false);
		}
	}
	
	private void initializeComponents(){
		toggle_textContacts = (ToggleButton) findViewById(R.id.toggle_textContacts);
		toggle_textContacts.setChecked(alarm.getTextContactsOption());
		toggle_FBRadio = (ToggleButton) findViewById(R.id.fbRadioToggle);
		toggle_FBRadio.setChecked(alarm.getFacebookOption());
		toggle_ShakeToWake = (ToggleButton) findViewById(R.id.shakeToWakeToggleButton);
		toggle_ShakeToWake.setChecked(alarm.getShakeToWakeOption());
		toggle_AlarmEnabled = (ToggleButton) findViewById(R.id.AlarmEnabled);
		toggle_AlarmEnabled.setChecked(alarm.enabled());
		textContactsSmallFrame = (RelativeLayout) findViewById(R.id.textContactsSmallFrameBottom);
		fbRadioSmallFrame = (LinearLayout) findViewById(R.id.FBRadioSmallFrame);
        if (!alarm.getFacebookOption()) {
            LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            fbRadioSmallFrame.setLayoutParams(layout_params);
        } else {
        	LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(0, 0);
            fbRadioSmallFrame.setLayoutParams(layout_params);
        }
		addContacts = (Button) findViewById(R.id.addContacts);
		musicButton = (Button) findViewById(R.id.changeMusic);
		AlarmTime =  (TimePicker) findViewById(R.id.AlarmTime);
		AlarmTime.setCurrentHour(alarm.getHour());
		AlarmTime.setCurrentMinute(alarm.getMinute());
		contactsTextView = (TextView) findViewById(R.id.contactsTextView);
		contactsTextView.setText(alarm.getContactsListAsString());
		musicTextView = (TextView) findViewById(R.id.Tune);
		musicTextView.setText(alarm.getMusicListAsString());
		alarmNameEditText = (EditText)findViewById(R.id.AlarmNameEditText);
		alarmNameEditText.setText(alarm.getName());
		// start out without showing the extra settings initially
    	LinearLayout.LayoutParams layout_desc = new LinearLayout.LayoutParams(0, 0);
        textContactsSmallFrame.setLayoutParams(layout_desc);
        
	}
	
	private void setupAddContactsButton(){
		addContacts.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startContactManager();
			}
		});
	}
	
	private void startContactManager()
	{
		GlobalStaticVariables.resetContacts();
		Intent i = new Intent(this, ContactManagerActivity.class);
		startActivityForResult(i,0);
	}
	
	private void setupMusicButton(){
		musicButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startMusicManager();
			}
		});
	}
	
	private void startMusicManager()
	{
		GlobalStaticVariables.resetMusic();
		Intent i = new Intent(this, MusicManagerActivity.class);
		startActivityForResult(i,1);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_settings);
		//get the extra information from the invoking intent
		int selectedAlarmID = Integer.parseInt(getIntent().getExtras().getString(AlarmFactory.ALARM_ID));
		
		alarmFactory = new AlarmFactory(this);
		
		// populate alarm arraylist
		prefsHandler =  new PreferencesHandler(this);
		alarms = prefsHandler.getSettings().getAlarms();
		
		for(int i = 0 ; i <alarms.size() ; i++)
		{
			if(alarms.get(i).getID()==selectedAlarmID)
			{
				this.alarm = alarms.get(i);
			}
		}
		
		// initialize form components
		initializeComponents();
		
		setupToggleButtons();
		setupSaveButton();
		setupAddContactsButton();
		setupMusicButton();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
	  super.onActivityResult(requestCode, resultCode, data); 
	  	handleActivityResult(requestCode);
	}
	
	private void handleActivityResult(int requestCode)
	{
		Log.d("AlarmClock","activity result");
		  if(requestCode == 0)
		  {
			  Log.d("AlarmClock","setting selectedContacts field contacts ammount selcted was " + GlobalStaticVariables.selectedContacts.size());
			  if(GlobalStaticVariables.selectedContacts.size()>0)
				{
					String contactsToText = GlobalStaticVariables.selectedContacts.get(0).getName();
					for(int i = 1; i<GlobalStaticVariables.selectedContacts.size();i++)
					{
						contactsToText+=","+GlobalStaticVariables.selectedContacts.get(i).getName();
					}
					Log.d("AlarmClock","contacts were set");
					contactsTextView.setText(contactsToText);
				}
		  }
		  if(requestCode == 1)
		  {
			  Log.d("AlarmClock","setting selected music field music ammount selected was "+ GlobalStaticVariables.selectedMusic.size());
			  if(GlobalStaticVariables.selectedMusic.size()>0)
				{
					String musicList = GlobalStaticVariables.selectedMusic.get(0).getName();
					for(int i = 1; i<GlobalStaticVariables.selectedMusic.size();i++)
					{
						musicList+=GlobalStaticVariables.selectedMusic.get(i).getName();
					}
					Log.d("AlarmClock","music was set");
					musicTextView.setText(musicList);
				}
		  }
	}
	
	
	private void saveAlarm()
	{
		for(int i = 0 ; i< alarms.size();i++)
		{
			if(alarms.get(i).getID() == alarm.getID())
			{
				Log.d("AlarmClock","Alarm of id: "+alarms.get(i).getID()+" saved for time "+alarm.getHour()+":"+alarm.getMinute());
				alarms.set(i, alarm);	
				if(alarm.enabled())alarmFactory.refreshAlarm(alarm);
			}
		}
		prefsHandler.setAlarms(alarms);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		
		return true;
	}

	public void onToggleClicked(View view) {
	    // Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();
	    
	    if (on) {
	        // Enable vibrate
	    } else {
	        // Disable vibrate
	    }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	        Log.d(this.getClass().getName(), "back button pressed");
	        this.startMainMenuActivity();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
}
