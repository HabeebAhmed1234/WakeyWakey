package com.example.alarmclock;

import android.content.Context;

public class Preferences {
	private boolean facebook=false;
	private boolean textcontacts=false;
	private boolean videonews=false;
	private boolean music=false;
	
	void setfacebook(boolean setting)
	{
		facebook=setting;
	}
	
	void settextcontacts(boolean setting)
	{
		textcontacts=setting;
	}
	
	void setvideonews(boolean setting)
	{
		videonews=setting;
	}
	
	void setmusic(boolean setting)
	{
		music=setting;
	}
	
	boolean getfacebook()
	{
		return facebook;
	}
	
	boolean gettextcontacts()
	{
		return textcontacts;
	}
	
	boolean getvideonews()
	{
		return videonews;
	}
	
	boolean getmusic()
	{
		return music;
	}
		
}
