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
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.graphics.PorterDuff;

public class SettingsActivity extends Activity {

	ToggleButton toggle_textContacts;
	ToggleButton toggle_FBRadio;
	RelativeLayout textContactsSmallFrame;
	LinearLayout fbRadioSmallFrame;
	Button saveButton;
	Button addContacts;
	Button musicButton;
	static final int CONTACT_PICKER_RESULT = 1001;
	
	private ArrayList<Alarm> alarms = new ArrayList<Alarm>();
	private Alarm alarm;
	
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
	
	private void recolorSaveButton(){
		saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.DARKEN);
	}
	
	private void initializeComponents(){
		toggle_textContacts = (ToggleButton) findViewById(R.id.toggle_textContacts);
		toggle_FBRadio = (ToggleButton) findViewById(R.id.fbRadioToggle);
		textContactsSmallFrame = (RelativeLayout) findViewById(R.id.textContactsSmallFrameBottom);
		fbRadioSmallFrame = (LinearLayout) findViewById(R.id.FBRadioSmallFrame);
		addContacts = (Button) findViewById(R.id.addContacts);
		musicButton = (Button) findViewById(R.id.changeMusic);
		
		// start out without showing the extra settings initially
    	LinearLayout.LayoutParams layout_desc = new LinearLayout.LayoutParams(0, 0);
        textContactsSmallFrame.setLayoutParams(layout_desc);
	}
	
	private void setupAddContactsButton(){
		addContacts.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
				startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);*/

				Intent pickContactIntent = new Intent( Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI );
			    pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
			    startActivityForResult(pickContactIntent, CONTACT_PICKER_RESULT);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == RESULT_OK) {
	        switch (requestCode) {
	        case CONTACT_PICKER_RESULT:
	            Cursor cursor = null;
	            String email = "";
	            try {
	                //Uri result = data.getData();
	                //Log.v("DEBUG_TAG", "Got a contact result: "
	                //        + result.toString());
	                // get the contact id from the Uri
	                //String id = result.getLastPathSegment();
	                
	                Uri contactData = data.getData();
	                Cursor c =  this.managedQuery(contactData, null, null, null, null);
	                
	                String contactId = "";
	                
	                if (c.moveToFirst()) {
	                    contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
	                }
	                
	                // query for everything email
	                /*cursor = getContentResolver().query(Email.CONTENT_URI,
	                        null, Email.CONTACT_ID + "=?", new String[] { id },
	                        null);*/
	                
	                Cursor c1 = this.getContentResolver().query(Data.CONTENT_URI,
	                	    new String[] {Data._ID, Phone.NUMBER, Phone.TYPE, Phone.LABEL},
	                	    Data.CONTACT_ID + "=?" + " AND "
	                	    + Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'",
	                	    new String[] {String.valueOf(contactId)}, null);
	                
	                c1.moveToFirst();
	                
	                //cursor.moveToFirst();
	                
	                Log.v("DEBUG_TAG", "cursor query passed");
	        
	                
	                Log.v("DEBUG_TAG", "number "+c1.getString(1));
	                
	                /*String columns[] = c1.getColumnNames();
	                for (String column : columns) {
		                Log.v("DEBUG_TAG", "cursor query passed" + column.toString());
	                    int index = cursor.getColumnIndex(column);
	                    Log.v("DEBUG_TAG", "Column: " + column + " == ["
	                            + cursor.getString(index) + "]");
	                }
	                
	                int emailIdx = cursor.getColumnIndex(Email.DATA);
	                // let's just get the first email
	                if (cursor.moveToFirst()) {
	                    email = cursor.getString(emailIdx);
	                    Log.v("DEBUG_TAG", "Got email: " + email);
	                } else {
	                    Log.w("DEBUG_TAG", "No results");
	                }*/
	            } catch (Exception e) {
	                Log.e("DEBUG_TAG", "Failed to get email data", e);
	            } finally {
	            	Log.e("DEBUG_TAG", "other error");
	                if (cursor != null) {
	                    cursor.close();
	                }
	                //EditText emailEntry = (EditText) findViewById(R.id.invite_email);
	                //emailEntry.setText(email);
	                //if (email.length() == 0) {
	                 //   Toast.makeText(this, "No email found for contact.",
	                  //          Toast.LENGTH_LONG).show();
	                //}
	            }
	            break;
	        }
	    } else {
	        Log.w("DEBUG_TAG" , "Warning: activity result not ok");
	    }
	}
	
	private void setupMusicButton(){
		musicButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

			}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		//get the extra information from the invoking intent
		
		// populate alarm arraylist
		PreferencesHandler prefsHandler =  new PreferencesHandler(this);
		alarms = prefsHandler.getSettings().getAlarms();
		
		for(int i = 0 ; i <alarms.size() ; i++)
		{
			//if(alarms.get(i)==selectedAlarmID)
			//{
				
			//}
		}
		
		// initialize form components
		initializeComponents();
		
		setupToggleButtons();
		recolorSaveButton();
		setupAddContactsButton();
		setupMusicButton();
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
	
}
