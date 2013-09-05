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

public class TextContactsAlertActivity {

	PreferencesHandler prefsHandler;
	Preferences prefs;
	private int selectedAlarm;
	
	TextContactsAlertActivity(PreferencesHandler prefsHandler, Preferences prefs, int selectedAlarm){
		this.prefsHandler = prefsHandler;
		this.prefs = prefs;
		this.selectedAlarm = selectedAlarm;
	}
	
	void textAllContacts() {
		ArrayList<Contact> contacts = prefs.getAlarms().get(selectedAlarm).getContactsList();
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
	}
}
