package com.example.alarmclock;

import java.util.ArrayList;

import android.content.Context;

public class Preferences {
	
	private ArrayList<Alarm>   alarms   = new ArrayList<Alarm>();
	
	void setAlarms(ArrayList<Alarm> alarms)
	{
		this.alarms=alarms;
	}
	
	ArrayList<Alarm> getAlarms()
	{
		return this.alarms;
	}
}
