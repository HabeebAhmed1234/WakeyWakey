package com.example.alarmclock;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



//limitation
//if a contact has more than one number this software will take the first number available which is not necessarily a mobile number
public final class ContactManagerActivity extends Activity
{

    public static final String TAG = "ContactManager";
    
	Button saveButton;
	Button clearButton;
	
    // Data and List Adapter
    ContactsListAdapter contactsListAdapter;
    ArrayList<Contact> contacts = new ArrayList<Contact>();
    ArrayList<Contact> selectedContacts;
    
    PreferencesHandler prefsHandler;
    Preferences prefs;
    
    ArrayList<Alarm> alarms;
    Alarm selectedAlarm;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.v(TAG, "Activity State: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_manager);
        
        prefsHandler = new PreferencesHandler(this);
        prefs= prefsHandler.getSettings();
        alarms = prefs.getAlarms();
        
        		
        if(getIntent().getExtras()!=null)
        {
        	int selectedId = Integer.parseInt(getIntent().getExtras().getString(AlarmFactory.ALARM_ID));
        	selectedAlarm = prefsHandler.getSettings().getAlarmById(selectedId);
        	if(selectedAlarm.getContactsList()!=null)
        	{
        		selectedContacts = selectedAlarm.getContactsList();
        	}else{
        		selectedContacts = new ArrayList<Contact>();
        	}
        }else{
        	selectedContacts = new ArrayList<Contact>();
        }
        		
        ListView lvContactsContent = (ListView) findViewById(R.id.contactList);
		
        contacts=getContacts();
        
        Collections.sort(contacts, new CompareClass());
        
        contactsListAdapter = new ContactsListAdapter(this, contacts);
		lvContactsContent.setAdapter(contactsListAdapter);
		
		lvContactsContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		      @Override
		      public void onItemClick(AdapterView<?> parent,  final View view,int position, long id) {
		    	    String name=(String) ((TextView) view.findViewById(R.id.tvLAYOUTEVENTSName)).getText();
		            Contact selectedContact = (Contact)parent.getAdapter().getItem(position);
		            selectedContact.toggleSelected();
		            contactsListAdapter.notifyDataSetChanged();
		            view.refreshDrawableState();
		      }

		    });
		
		setupSaveButton();
		setupClearButton();
    }
    
    private ArrayList <Contact> getContacts()
    {
    	ContentResolver cr = getContentResolver();
    	Cursor people = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
    	
    	ArrayList <Contact> contactsList = new ArrayList<Contact>();
    	if(people.getCount()>0)
    	{
			while(people.moveToNext()) {
				
			   String name = people.getString(people.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			   String id = people.getString(people.getColumnIndex(ContactsContract.Contacts._ID));
			   if (Integer.parseInt(people.getString(people.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
				   // Query phone here. Covered next
	                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null); 
	                while (phones.moveToNext()) 
	                { 
                       String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                       Contact newContact = new Contact(phoneNumber, name);
                       if(isInArray(contactsList,newContact)==false)
	      			   {
	      				 contactsList.add(newContact);
	      			   }
	                } 
	                phones.close();
			   }
			}
    	}
    	people.close();
    	
    	return filter(contactsList);
    }
    
    //only checks for names 
    boolean isInArray (ArrayList <Contact> cons, Contact isin)
    {
    	for(int i=0;i<cons.size();i++)
    	{
    		if(cons.get(i).getName().compareTo(isin.getName())==0)
    		{
    			return true;
    		}
    	}
    	return false;
    }
   
    private ArrayList <Contact> filter (ArrayList <Contact> unfiltered)
    {
    	ArrayList <Contact> filtered = unfiltered;
    	for(int i=0;i<filtered.size();i++)
    	{
    		for(int x=0;x<selectedContacts.size();x++)
    		{
    			if(filtered.get(i).equals(selectedContacts.get(x)))
    			{
    				filtered.get(i).select();
    			}
    			
    		}
    	}
    	return filtered;
    }
    
    private void setupSaveButton(){
		saveButton = (Button) findViewById(R.id.ContactsSaveButton);
		saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				ArrayList<Contact> selectedContacts = new ArrayList<Contact>();
		    	
		    	for(int i=0;i<contacts.size();i++)
		    	{
		    		if(contacts.get(i).isSelected())
		    		{
		    			selectedContacts.add(contacts.get(i));
		    			Log.d("AlarmClock","contact was selected");
		    		}
		    	}
				
		    	selectedAlarm.setTextContactsList(selectedContacts);
		    	
				saveAlarm(selectedAlarm);
				Toast.makeText(getApplicationContext(), "Saved Contacts", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}
	
	private void setupClearButton(){
		clearButton = (Button) findViewById(R.id.ContactsClearButton);
		clearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				for(int i = 0 ; i <contacts.size();i++)
				{
					contacts.get(i).unselect();
				}
				
	            contactsListAdapter.notifyDataSetChanged();
	            
				Toast.makeText(getApplicationContext(), "Cleared Contacts", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void saveAlarm(Alarm alarm)
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
}
