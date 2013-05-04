package com.example.alarmclock;

import java.util.ArrayList;


import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class TextContactsAlertActivity extends Activity {

	PreferencesHandler prefsHandler;
	Preferences prefs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_contacts_alert);
		
		prefsHandler = new PreferencesHandler(this);
		prefs = prefsHandler.getSettings();
		
		textAllContacts();
		

		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_text_contacts_alert, menu);
		
		return true;
	}

	void textAllContacts() {
		ArrayList<Contact> contacts = prefs.getContactList();
	    Log.d("msg","textAllContacts");
		
		for(int i = 0; i<contacts.size();i++)
		{
			sendSMS(contacts.get(i).getNumber());
		}
		
	}
	
	void sendSMS(String number)
	{
		// make sure the fields are not empty
		String msg="I need you to wake me up! Call me now! ";
	    Log.d("msg","Sending to " + number);
		if (number.length()>0 && msg.length()>0)                
		{
			// call the sms manager
		    SmsManager sms = SmsManager.getDefault();
		    // this is the function that does all the magic
		    sms.sendTextMessage(number, null, msg, null, null);
		    Log.d("msg","Sent to" + number);
		}
		else
		{
			// display message if text fields are empty
		    Toast.makeText(getBaseContext(),"Could not send message",Toast.LENGTH_SHORT).show();
		}
	}
	
	

}
