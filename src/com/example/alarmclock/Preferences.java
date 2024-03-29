package com.example.alarmclock;

import java.util.ArrayList;

import android.content.Context;

public class Preferences {
	
	private ArrayList<Alarm>   alarms   = new ArrayList<Alarm>();
	
	private boolean isFirstBoot = true;
	private boolean isFirstNewsFeed = true;
	private boolean isFirstShakeToWake = true;
	private boolean isFirstTextContacts = true;
	private boolean isFirstMusic = true;
	
	private int numberOfAlarmsSet = 0;
	
	Preferences(ArrayList<Alarm> alarms)
	{
		this.alarms=alarms;
	}
	
	ArrayList<Alarm> getAlarms()
	{
		return this.alarms;
	}
	
	Alarm getAlarmById(int id)
	{
		for(int i = 0 ; i < alarms.size() ; i++)
		{
			if(alarms.get(i).getID() == id) return alarms.get(i);
		}
		return null;
	}
	
	public void setIsFirstBoot(boolean setting)
	{
		this.isFirstBoot = setting;
	}
	
	public boolean getIsFirstBoot()
	{
		return isFirstBoot;
	}
	
	public void setIsFirstNewsFeed(boolean setting)
	{
		this.isFirstNewsFeed = setting;
	}
	
	public boolean getIsFirstNewsFeed()
	{
		return this.isFirstNewsFeed;
	}
	
	public void setIsFirstShakeToWake(boolean setting)
	{
		this.isFirstShakeToWake = setting;
	}
	
	public boolean getIsFirstShakeToWake()
	{
		return isFirstShakeToWake;
	}
	
	public void setIsFirstTextContacts(boolean setting)
	{
		this.isFirstTextContacts = setting;
	}
	
	public boolean getIsFirstTextContacts()
	{
		return isFirstTextContacts;
	}
	
	public void setIsFirstMusic(boolean setting)
	{
		this.isFirstMusic = setting;
	}
	
	public boolean getIsFirstMusic()
	{
		return isFirstMusic;
	}
	
	public void setNumberOfAlarmsSet(int ammount)
	{
		this.numberOfAlarmsSet = ammount;
	}
	
	public int getNumberOfAlarmsSet()
	{
		return this.numberOfAlarmsSet;
	}
}
