package com.example.alarmclock;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PreferencesHandler {

	private Context context;
	private static final String PREFS_NAME="CONFIG";
	
	public String NEWSFEED_KEY="newsfeedison";
	public String VIDEONEWS_KEY="videonewsison";
	public String TEXTCONTACTS_KEY="textcontactsison";
	public String MUSIC_KEY="musicison";
	public String CONTACTS_AMMOUNT_KEY="CONTACTS_AMMOUNT";
	public String MUSICS_AMMOUNT_KEY="MUSICS_AMMOUNT";
	public String ALARMS_AMMOUNT_KEY="ALARMS_AMMOUNT";
	public String TIME_HOUR_KEY="TIME_HOUR";
	public String TIME_MINUTE_KEY = "TIME_MINUTE";
	public String ALARM_ID_KEY = "ALARM_ID";
	public String ALARM_NAME_KEY = "ALARM_NAME";
	public String SHAKETOWAKE_KEY = "SHAKETOWAKE";
	public String IS_REPEATED_DAILY_KEY = "ISREPEATEDDAILY";
	public String ALARM_ENABLED_KEY = "ALARM_ENABLED";
	
	public String IS_FIRST_BOOT_KEY = "ISFIRSTBOOT";
	public String IS_FIRST_TEXT_CONTACTS_KEY = "ISFIRSTTEXTCONTACTS";
	public String IS_FIRST_NEWS_FEED_KEY = "ISFIRSTNEWSFEED";
	public String IS_FIRST_SHAKE_TO_WAKE_KEY = "ISFIRSTSHAKETOWAKE";
	public String IS_FIRST_MUSIC_KEY = "ISFIRSTMUSIC";
	
	public String NUMBER_OF_ALARMS_SET = "ALARMS_SET";
	
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
	
	private void setRssNewsFeedOption(boolean setting, int AlarmNumber)
	{
		if(setting)
		{
			set(NEWSFEED_KEY+AlarmNumber,"true");
		}else
		{
			set(NEWSFEED_KEY+AlarmNumber,"false");
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
	
	private void setShakeToWakeOption(boolean setting,int AlarmNumber)
	{
		if(setting)
		{
			set(SHAKETOWAKE_KEY+AlarmNumber,"true");
		}else
		{
			set(SHAKETOWAKE_KEY+AlarmNumber,"false");
		}
	}
	
	private void setRepeatedDailyOption(boolean setting, int AlarmNumber)
	{
		if(setting)
		{
			set(IS_REPEATED_DAILY_KEY+AlarmNumber,"true");
		}else
		{
			set(IS_REPEATED_DAILY_KEY+AlarmNumber,"false");
		}
	}
	
	private void setAlarmEnabled(boolean setting,int AlarmNumber)
	{
		if(setting)
		{
			set(ALARM_ENABLED_KEY+AlarmNumber,"true");
		}else
		{
			set(ALARM_ENABLED_KEY+AlarmNumber,"false");
		}
	}
	
	public void setIsFirstBoot(boolean setting)
	{
		if(setting)
		{
			set(IS_FIRST_BOOT_KEY,"true");
		}else
		{
			set(IS_FIRST_BOOT_KEY,"false");
		}
	}
	
	private boolean getIsFirstBoot()
	{
		if(get(IS_FIRST_BOOT_KEY).compareTo("false")==0)
		{
			return false;
		}else{
			return true;
		}
	}
	
	public void setIsFirsTextContacts(boolean setting)
	{
		if(setting)
		{
			set(IS_FIRST_TEXT_CONTACTS_KEY,"true");
		}else
		{
			set(IS_FIRST_TEXT_CONTACTS_KEY,"false");
		}
	}
	
	private boolean getIsFirsTextContacts()
	{
		if(get(IS_FIRST_TEXT_CONTACTS_KEY).compareTo("false")==0)
		{
			return false;
		}else{
			return true;
		}
	}
	
	public void setIsFirstShakeToWake(boolean setting)
	{
		if(setting)
		{
			set(this.IS_FIRST_SHAKE_TO_WAKE_KEY,"true");
		}else
		{
			set(IS_FIRST_SHAKE_TO_WAKE_KEY,"false");
		}
	}
	
	private boolean getIsFirstShakeToWake()
	{
		if(get(IS_FIRST_SHAKE_TO_WAKE_KEY).compareTo("false")==0)
		{
			return false;
		}else{
			return true;
		}
	}
	
	public void setIsFirstMusic(boolean setting)
	{
		if(setting)
		{
			set(this.IS_FIRST_MUSIC_KEY,"true");
		}else
		{
			set(IS_FIRST_MUSIC_KEY,"false");
		}
	}
	
	public void incrementNumberOfAlarmsSet(int increment)
	{
		int curval = 0;
		
		if(get(this.NUMBER_OF_ALARMS_SET).compareTo("NULL")!=0)
		{
			curval = Integer.parseInt(get(this.NUMBER_OF_ALARMS_SET));
		}
		
		set(this.NUMBER_OF_ALARMS_SET,Integer.toString(curval+increment));
	}
	
	private int getNumberOfAlarmsSet()
	{
		if(get(this.NUMBER_OF_ALARMS_SET).compareTo("NULL")!=0)
		{
			return Integer.parseInt(get(this.NUMBER_OF_ALARMS_SET));
		}
		return 0;
	}
	
	private boolean getIsFirstMusic()
	{
		if(get(IS_FIRST_MUSIC_KEY).compareTo("false")==0)
		{
			return false;
		}else{
			return true;
		}
	}
	
	public void setIsFirstNewsFeed(boolean setting)
	{
		if(setting)
		{
			set(this.IS_FIRST_NEWS_FEED_KEY,"true");
		}else
		{
			set(IS_FIRST_NEWS_FEED_KEY,"false");
		}
	}
	
	private boolean getIsFirstNewsFeed()
	{
		if(get(IS_FIRST_NEWS_FEED_KEY).compareTo("false")==0)
		{
			return false;
		}else{
			return true;
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
		Log.d("AlarmClock","PreferenceHandler: time is "+hour+":"+minute);
		this.set(this.TIME_MINUTE_KEY+AlarmNumber,Integer.toString(minute));
	}
	
	private void setAlarmID(int id,int AlarmNumber)
	{
		this.set(this.ALARM_ID_KEY+AlarmNumber,Integer.toString(id));
	}
	
	private void setAlarmName(String name, int AlarmNumber)
	{
		this.set(this.ALARM_NAME_KEY+AlarmNumber,name);
	}
	
	public void setAlarms(ArrayList<Alarm> alarms)
	{
		this.set(ALARMS_AMMOUNT_KEY,Integer.toString(alarms.size()));
		
		for(int i=0;i<alarms.size();i++)
		{
			this.setAlarmTime(alarms.get(i).getHour(),alarms.get(i).getMinute(), i);
			this.setRssNewsFeedOption(alarms.get(i).getRssNewFeedOption(), i);
			this.setMusicOption(alarms.get(i).getMusicOption(), i);
			this.setTextContactsOption(alarms.get(i).getTextContactsOption(), i);
			this.setVideoNewsOption(alarms.get(i).getVideoNewsOption(), i);
			this.setShakeToWakeOption(alarms.get(i).getShakeToWakeOption(), i);
			this.setRepeatedDailyOption(alarms.get(i).isRepeatedDaily(), i);
			this.setMusicList(alarms.get(i).getMusicList(), i);
			this.setTextContactsList(alarms.get(i).getContactsList(), i);
			this.setAlarmID(alarms.get(i).getID(),i);
			this.setAlarmName(alarms.get(i).getName(),i);
			this.setAlarmEnabled(alarms.get(i).enabled(), i);
		}
	}
	
	Preferences getSettings()
	{
		ArrayList <Alarm> alarms = new ArrayList<Alarm>();
		
		if(!(get(this.ALARMS_AMMOUNT_KEY).compareTo("NULL")==0))
		{
			for(int i = 0 ; i < Integer.parseInt(get(this.ALARMS_AMMOUNT_KEY)) ; i++)
			{
				Alarm newAlarm = new Alarm(0,0,0);
				
				if(get(this.NEWSFEED_KEY+i).compareTo("true")==0)
				{
					newAlarm.setRssNewsFeedOption(true);
				}else{
					newAlarm.setRssNewsFeedOption(false);
				}
				
				if(get(this.VIDEONEWS_KEY+i).compareTo("true")==0)
				{
					newAlarm.setVideoNewsOption(true);
				}else{
					newAlarm.setVideoNewsOption(false);
				}
				
				if(get(this.TEXTCONTACTS_KEY+i).compareTo("true")==0)
				{
					newAlarm.setTextContactsOption(true);
				}else{
					newAlarm.setTextContactsOption(false);
				}
				
				if(get(this.MUSIC_KEY+i).compareTo("true")==0)
				{
					newAlarm.setMusicOption(true);
				}else{
					newAlarm.setMusicOption(false);
				}
				
				if(get(this.SHAKETOWAKE_KEY+i).compareTo("true")==0)
				{
					newAlarm.setShakeToWakeOption(true);
				}else{
					newAlarm.setShakeToWakeOption(false);
				}
				
				if(get(this.IS_REPEATED_DAILY_KEY+i).compareTo("true")==0)
				{
					newAlarm.setRepeatedDaily(true);
				}else{
					newAlarm.setRepeatedDaily(false);
				}
				
				if(get(this.ALARM_ENABLED_KEY+i).compareTo("true")==0)
				{
					newAlarm.enableAlarm();
				}else{
					newAlarm.disableAlarm();
				}
				
				//add in time
				String hour = get(TIME_HOUR_KEY+i);
				String minute = get(TIME_MINUTE_KEY+i);
				
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
				
				//add in name
				String name = this.get(this.ALARM_NAME_KEY+i);
				if(!(name.compareTo("NULL")==0))
				{
					newAlarm.setName(name);
				}else
				{
					newAlarm.setName("No Name");
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
		
		Preferences prefs=new Preferences(alarms);
		prefs.setIsFirstBoot(getIsFirstBoot());
		prefs.setIsFirstNewsFeed(this.getIsFirstNewsFeed());
		prefs.setIsFirstShakeToWake(this.getIsFirstShakeToWake());
		prefs.setIsFirstTextContacts(this.getIsFirsTextContacts());
		prefs.setIsFirstMusic(this.getIsFirstMusic());
		
		prefs.setNumberOfAlarmsSet(getNumberOfAlarmsSet());
		
		return prefs;
	}
}

