package com.example.alarmclock;

import java.util.ArrayList;

import android.content.Context;

public class Preferences {
	
	private ArrayList<Alarm>   alarms   = new ArrayList<Alarm>();
	
	private boolean isFirstBoot = true;
	
	Preferences(ArrayList<Alarm> alarms)
	{
		this.alarms=alarms;
	}
	
	ArrayList<Alarm> getAlarms()
	{
		return this.alarms;
	}
	
	public void setIsFirstBoot(boolean setting)
	{
		this.isFirstBoot = setting;
	}
	
	public boolean getIsFirstBoot()
	{
		return isFirstBoot;
	}
}
