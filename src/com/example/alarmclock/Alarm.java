package com.example.alarmclock;

import java.util.ArrayList;

public class Alarm {
	
	private String time;
	private boolean facebookOption;
	private boolean videoNewsOption;
	private boolean textContactsOption;
	private boolean musicOption;
	public static final String ALARM_NAME="AlarmName";
	
	private ArrayList<Contact> textContactsList;
	private ArrayList<Music> musicList;
	
	void Alarm(String id)
	{
		this.textContactsList = new ArrayList<Contact>();
		this.musicList = new ArrayList<Music>();
	}
	
	public void setTime(String time)
	{
		this.time=time;
	}
	
	public String getTime()
	{
		return this.time;
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
	

	void addContactToList(Contact contact)
	{
		this.textContactsList.add(contact);
	}
	
	void addMusicToList(Music music)
	{
		this.musicList.add(music);
	}
}
