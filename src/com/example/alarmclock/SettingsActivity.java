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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.graphics.PorterDuff;

public class SettingsActivity extends Activity {

	ToggleButton toggle_textContacts;
	ToggleButton toggle_NFRadio;
	ToggleButton toggle_ShakeToWake;
	ToggleButton toggle_AlarmEnabled;
	
	public RadioGroup radioRepeatSetting;
	public RadioButton radioJustOnce;
	public RadioButton radioRepeat;
	
	RelativeLayout textContactsSmallFrame;
	LinearLayout NFRadioSmallFrame;
	EditText alarmNameEditText;
	Button saveButton;
	Button cancelButton;
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
	
	public boolean isNewAlarm = false;
	
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
		
		toggle_NFRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (!isChecked) {
		            // The toggle is disabled
		        	// resize the frame to just show the top button
		        	LinearLayout.LayoutParams layout_desc = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		            NFRadioSmallFrame.setLayoutParams(layout_desc);
		        } else {
		        	// the toggle is enabled
		        	LinearLayout.LayoutParams layout_desc = new LinearLayout.LayoutParams(0, 0);
		            NFRadioSmallFrame.setLayoutParams(layout_desc);
		        }
		    }
		});
	}
	
	private void setupSaveButton(){
		saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				Toast.makeText(getApplicationContext(), "Saved Alarm", Toast.LENGTH_SHORT).show();
				populateAlarmWithFormData();
				saveAlarm();
				startMainMenuActivity();
			}
		});
	}
	
	private void setupCancelButton(){
		cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isNewAlarm)deleteAlarm();
				startMainMenuActivity();
				Toast.makeText(getApplicationContext(), "Canceled Alarm", Toast.LENGTH_SHORT).show();
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
		alarm.setRssNewsFeedOption(toggle_NFRadio.isChecked());
		alarm.setMusicOption(!toggle_NFRadio.isChecked());
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
		
		if(!toggle_NFRadio.isChecked())
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
		
		int checkedRadioButtonId = radioRepeatSetting.getCheckedRadioButtonId();
		if(checkedRadioButtonId==R.id.justOnce)
		{
			alarm.setRepeatedDaily(false);
		}else
		{
			alarm.setRepeatedDaily(true);
		}
	}
	
	private void initializeComponents(){
		toggle_textContacts = (ToggleButton) findViewById(R.id.toggle_textContacts);
		toggle_textContacts.setChecked(alarm.getTextContactsOption());
		toggle_NFRadio = (ToggleButton) findViewById(R.id.NFRadioToggle);
		toggle_NFRadio.setChecked(alarm.getRssNewFeedOption());
		toggle_ShakeToWake = (ToggleButton) findViewById(R.id.shakeToWakeToggleButton);
		toggle_ShakeToWake.setChecked(alarm.getShakeToWakeOption());
		toggle_AlarmEnabled = (ToggleButton) findViewById(R.id.AlarmEnabled); 
		toggle_AlarmEnabled.setChecked(alarm.enabled());
		textContactsSmallFrame = (RelativeLayout) findViewById(R.id.textContactsSmallFrameBottom);
		NFRadioSmallFrame = (LinearLayout) findViewById(R.id.NFRadioSmallFrame);
        if (!alarm.getRssNewFeedOption()) {
            LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            NFRadioSmallFrame.setLayoutParams(layout_params);
        } else {
        	LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(0, 0);
            NFRadioSmallFrame.setLayoutParams(layout_params);
        }
        
        radioJustOnce = (RadioButton) findViewById(R.id.justOnce);
        radioRepeat = (RadioButton) findViewById(R.id.daily);
	    radioRepeatSetting = (RadioGroup) findViewById(R.id.radioRepeat);
	    if(!alarm.isRepeatedDaily())
    	{
    		radioJustOnce.setChecked(true);
    		radioRepeat.setChecked(false);
    	}else
    	{
    		radioRepeat.setChecked(true);
    		radioJustOnce.setChecked(false);
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
		if(alarm.getMusicListAsString()==null||alarm.getMusicListAsString().compareTo("")==0)musicTextView.setText("Default");
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
		this.isNewAlarm = Boolean.parseBoolean(getIntent().getExtras().getString(MainActivity.IS_NEW_ALARM));
		
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
		setupCancelButton();
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
		  if(requestCode == 0)
		  {
			  if(GlobalStaticVariables.selectedContacts.size()>0)
				{
					String contactsToText = GlobalStaticVariables.selectedContacts.get(0).getName();
					for(int i = 1; i<GlobalStaticVariables.selectedContacts.size();i++)
					{
						contactsToText+=","+GlobalStaticVariables.selectedContacts.get(i).getName();
					}
					contactsTextView.setText(contactsToText);
				}
		  }
		  if(requestCode == 1)
		  {
			  if(GlobalStaticVariables.selectedMusic.size()>0)
				{
					String musicList = GlobalStaticVariables.selectedMusic.get(0).getName();
					for(int i = 1; i<GlobalStaticVariables.selectedMusic.size();i++)
					{
						musicList+=GlobalStaticVariables.selectedMusic.get(i).getName();
					}
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
				alarms.set(i, alarm);
			}
		}
		prefsHandler.setAlarms(alarms);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.settings, menu);
		
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
	        Toast.makeText(getApplicationContext(), "Canceled Alarm", Toast.LENGTH_SHORT).show();
	        if(isNewAlarm)deleteAlarm();
	        startMainMenuActivity();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	private void deleteAlarm()
	{
		for(int i = 0 ; i< alarms.size();i++)
		{
			if(alarms.get(i).getID() == alarm.getID())
			{
				if(!AlarmFactory.isInit)AlarmFactory.init();
				AlarmFactory.cancelAlarm(alarms.get(i));
				alarms.remove(i);	
			}
		}
		prefsHandler.setAlarms(alarms);
	}
	
}
