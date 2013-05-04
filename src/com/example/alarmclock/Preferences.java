package com.example.alarmclock;

import java.util.ArrayList;

import android.content.Context;

public class Preferences {
	private boolean facebook=false;
	private boolean textcontacts=false;
	private boolean videonews=false;
	private boolean music=false;
	private ArrayList<Contact> contactList = new ArrayList<Contact>();
	
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
	
	void addContactToList(Contact contact)
	{
		contactList.add(contact);
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
	
	ArrayList <Contact> getContactList()
	{
		return contactList;
	}
		
}
