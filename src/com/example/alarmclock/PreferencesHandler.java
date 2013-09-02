package com.example.alarmclock;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PreferencesHandler {

	private Context context;
	private static final String PREFS_NAME="CONFIG";
	
	public String FACEBOOK_KEY="facebookison";
	public String VIDEONEWS_KEY="videonewsison";
	public String TEXTCONTACTS_KEY="textcontactsison";
	public String MUSIC_KEY="musicison";
	public String CONTACTS_AMMOUNT_KEY="CONTACTS_AMMOUNT";
	public String MUSICS_AMMOUNT_KEY="MUSICS_AMMOUNT";
	public String ALARMS_AMMOUNT_KEY="ALARMS_AMMOUNT";
	public String TIME_HOUR_KEY="TIME_HOUR";
	public String TIME_MINUTE_KEY = "TIME_MINUTE";
	public String ALARM_ID_KEY = "ALARM_ID";
	
	private SharedPreferences settings ;
	
	PreferencesHandler(Context con)
	{
		context=con;
		settings = context.getSharedPreferences(PREFS_NAME, 0);
	}
	
	private void set(String settingname, String settingvalue)
	{
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(settingname, settingvalue);
        editor.commit();
	}
	
	private String get(String key)
	{
        return settings.getString(key, "NULL");
	}
	
	private void setFacebookOption(boolean setting, int AlarmNumber)
	{
		if(setting)
		{
			set(FACEBOOK_KEY+AlarmNumber,"true");
		}else
		{
			set(FACEBOOK_KEY+AlarmNumber,"false");
		}
	}
	
	private void setVideoNewsOption(boolean setting,int AlarmNumber)
	{
		if(setting)
		{
			set(VIDEONEWS_KEY+AlarmNumber,"true");
		}else
		{
			set(VIDEONEWS_KEY+AlarmNumber,"false");
		}
	}
	
	private void setTextContactsOption(boolean setting,int AlarmNumber)
	{
		if(setting)
		{
			set(TEXTCONTACTS_KEY+AlarmNumber,"true");
		}else
		{
			set(TEXTCONTACTS_KEY+AlarmNumber,"false");
		}
	}
	
	private void setTextContactsList(ArrayList<Contact> contacts,int AlarmNumber)
	{
		//shows the number of contacts in total
		this.set(CONTACTS_AMMOUNT_KEY+AlarmNumber,Integer.toString(contacts.size()));
		
		//Log.d("debuggings","set contacts ammount " + Integer.toString(contacts.size()));
		
		for(int i=0;i<contacts.size();i++)
		{
			this.set("CONTACT_NUMBER_"+i+"_"+AlarmNumber,contacts.get(i).getNumber());
			this.set("CONTACT_NAME_"+i+"_"+AlarmNumber,contacts.get(i).getName());
		}
	}
	
	private void setMusicOption(boolean setting,int AlarmNumber)
	{
		if(setting)
		{
			set(MUSIC_KEY+AlarmNumber,"true");
		}else
		{
			set(MUSIC_KEY+AlarmNumber,"false");
		}
	}
	
	private void setMusicList(ArrayList<Music> selectedMusic,int AlarmNumber) {
		//shows the number of contacts in total
		this.set(MUSICS_AMMOUNT_KEY+AlarmNumber,Integer.toString(selectedMusic.size()));
		
		for(int i=0;i<selectedMusic.size();i++)
		{
			this.set("MUSIC_NUMBER_"+i+"_"+AlarmNumber,selectedMusic.get(i).getPath());
			this.set("MUSIC_NAME_"+i+"_"+AlarmNumber,selectedMusic.get(i).getName());
		}
	}
	
	private void setAlarmTime(int hour,int minute, int AlarmNumber)
	{
		this.set(this.TIME_HOUR_KEY+AlarmNumber,Integer.toString(hour));
		this.set(this.TIME_MINUTE_KEY+AlarmNumber,Integer.toString(minute));
	}
	
	private void setAlarmID(int id,int AlarmNumber)
	{
		this.set(this.ALARM_ID_KEY+AlarmNumber,Integer.toString(id));
	}
	
	public void setAlarms(ArrayList<Alarm> alarms)
	{
		this.set(ALARMS_AMMOUNT_KEY,Integer.toString(alarms.size()));
		
		for(int i=0;i<alarms.size();i++)
		{
			this.setAlarmTime(alarms.get(i).getHour(),alarms.get(i).getMinute(), i);
			this.setFacebookOption(alarms.get(i).getFacebookOption(), i);
			this.setMusicOption(alarms.get(i).getMusicOption(), i);
			this.setTextContactsOption(alarms.get(i).getTextContactsOption(), i);
			this.setVideoNewsOption(alarms.get(i).getVideoNewsOption(), i);
			this.setMusicList(alarms.get(i).getMusicList(), i);
			this.setTextContactsList(alarms.get(i).getContactsList(), i);
			this.setAlarmID(alarms.get(i).getID(),i);
		}
	}
	
	Preferences getSettings()
	{
		Preferences prefs=new Preferences();
		ArrayList <Alarm> alarms = new ArrayList<Alarm>();
		
		if(!(get(this.ALARMS_AMMOUNT_KEY).compareTo("NULL")==0))
		{
			for(int i = 0 ; i < Integer.parseInt(get(this.ALARMS_AMMOUNT_KEY)) ; i++)
			{
				Alarm newAlarm = new Alarm(0,0,0);
				
				if(get(this.FACEBOOK_KEY+i)=="true")
				{
					newAlarm.setFacebookOption(true);
				}else{
					newAlarm.setFacebookOption(false);
				}
				
				if(get(this.VIDEONEWS_KEY+i)=="true")
				{
					newAlarm.setVideoNewsOption(true);
				}else{
					newAlarm.setVideoNewsOption(false);
				}
				
				if(get(this.TEXTCONTACTS_KEY+i)=="true")
				{
					newAlarm.setTextContactsOption(true);
				}else{
					newAlarm.setTextContactsOption(false);
				}
				
				if(get(this.MUSIC_KEY+i)=="true")
				{
					newAlarm.setMusicOption(true);
				}else{
					newAlarm.setMusicOption(false);
				}
				
				//add in time
				String hour = get(TIME_HOUR_KEY+i);
				String minute = get(TIME_HOUR_KEY+i);
				
				if(!(hour.compareTo("NULL")==0)&&!(minute.compareTo("NULL")==0))
				{
					newAlarm.setTime(Integer.parseInt(hour), Integer.parseInt(minute));
				}
				else
				{
					newAlarm.setTime(0,0);
				}
				
				//add in ID
				String id = this.get(this.ALARM_ID_KEY+i);
				if(!(id.compareTo("NULL")==0))
				{
					newAlarm.setID(Integer.parseInt(id));
				}else
				{
					newAlarm.setID(0);
				}
				
				//add in contacts
				String ammountOfContacts=get(CONTACTS_AMMOUNT_KEY+i);
				
				if(!(ammountOfContacts.compareTo("NULL")==0))
				{
					for(int x = 0; x<Integer.parseInt(ammountOfContacts);x++)
					{
						Contact cont = new Contact(get("CONTACT_NUMBER_"+x+"_"+i),get("CONTACT_NAME_"+x+"_"+i));
						cont.select();
						newAlarm.addContactToList(cont);
					}
				}
				//Log.d("debuggings","get contacts ammount " + Integer.toString(prefs.getContactList().size()));
				
				//add in music
				String ammountOfMusic=get(MUSICS_AMMOUNT_KEY+i);
				if(!(ammountOfMusic.compareTo("NULL")==0))
				{
					for(int x = 0; x<Integer.parseInt(ammountOfMusic);x++)
					{
						Music mus = new Music(get("MUSIC_NUMBER_"+x+"_"+i),get("MUSIC_NAME_"+x+"_"+i));
						mus.select();
						newAlarm.addMusicToList(mus);
					}
				}
				
				alarms.add(newAlarm);
			}
		}
		
		prefs.setAlarms(alarms);
		//Log.d("debuggings","get musics ammount " + Integer.toString(prefs.getMusicList().size()));
		return prefs;
	}
}

