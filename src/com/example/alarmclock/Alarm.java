package com.example.alarmclock;

import java.util.ArrayList;

public class Alarm {
	
	private int hour;
	private int minute;
	private boolean facebookOption;
	private boolean videoNewsOption;
	private boolean textContactsOption;
	private boolean shakeToWakeOption;
	private boolean musicOption;
	private boolean isAlarmOn;
	private int ID;
	private String name;
	
	public static final String ALARM_NAME="AlarmName";
	
	private ArrayList<Contact> textContactsList;
	private ArrayList<Music> musicList;
	
	Alarm(int hour, int minute, int id)
	{
		this.textContactsList = new ArrayList<Contact>();
		this.musicList = new ArrayList<Music>();
		this.hour = hour;
		this.minute = minute;
		this.ID = id;
		this.facebookOption=false;
		this.videoNewsOption=false;
		this.textContactsOption=false;
		this.musicOption=false;
		this.shakeToWakeOption=false;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setID(int id)
	{
		this.ID = id;
	}
	
	public int getID()
	{
		return this.ID;
	}
	
	public void setTime(int hour,int minute)
	{
		this.hour=hour;
		this.minute = minute;
	}
	
	public void enableAlarm()
	{
		isAlarmOn = true;
	}
	
	public boolean enabled()
	{
		return isAlarmOn;
	}
	
	public void disableAlarm()
	{
		isAlarmOn = false;
	}
	
	public int getHour()
	{
		return this.hour;
	}
	
	public int getMinute()
	{
		return this.minute;
	}
	
	public void setFacebookOption(boolean setting)
	{
		if(this.musicOption&&setting)
		{
			this.musicOption=false;
			this.facebookOption=true;
		}else
		{
			this.facebookOption=setting;
		}
	}
	
	public boolean getFacebookOption()
	{
		return this.facebookOption;
	}
	
	public void setVideoNewsOption(boolean setting)
	{
		this.videoNewsOption=setting;
	}
	
	public boolean getVideoNewsOption()
	{
		return this.videoNewsOption;
	}
	
	public void setTextContactsOption(boolean setting)
	{
		this.textContactsOption=setting;
	}
	
	public boolean getTextContactsOption()
	{
		return this.textContactsOption;
	}
	
	public void setTextContactsList(ArrayList<Contact> contacts)
	{
		this.textContactsList=contacts;
	}
	
	public ArrayList<Contact> getContactsList()
	{
		return this.textContactsList;
	}
	
	public void setMusicOption(boolean setting)
	{
		if(this.facebookOption&&setting)
		{
			this.facebookOption=false;
			this.musicOption=true;
		}else
		{
			this.musicOption=setting;
		}
	}
	
	public boolean getMusicOption()
	{
		return this.musicOption;
	}
	
	public void setMusicList(ArrayList<Music> selectedMusic) {
		this.musicList=selectedMusic;
	}
	
	public ArrayList<Music> getMusicList()
	{
		return this.musicList;
	}
	
	public void setShakeToWakeOption(boolean option)
	{
		shakeToWakeOption = option;
	}
	
	public boolean getShakeToWakeOption()
	{
		return shakeToWakeOption;
	}

	void addContactToList(Contact contact)
	{
		this.textContactsList.add(contact);
	}
	
	void addMusicToList(Music music)
	{
		this.musicList.add(music);
	}
	
	String getMusicListAsString()
	{
		if(!(musicList.size()>0))return "";
		
		String musicListString = musicList.get(0).getName();
		
		for(int i = 1 ; i < musicList.size() ; i++)
		{
			musicListString+=", "+musicList.get(i).getName();
		}
		
		return musicListString;
	}
	
	String getContactsListAsString()
	{
		if(!(textContactsList.size()>0))return "";
		
		String contactListString = textContactsList.get(0).getName();
		
		for(int i = 1 ; i < textContactsList.size() ; i++)
		{
			contactListString+=", "+textContactsList.get(i).getName();
		}
		
		return contactListString;
	}
}

