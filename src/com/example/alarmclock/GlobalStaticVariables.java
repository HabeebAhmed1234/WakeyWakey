package com.example.alarmclock;

import java.util.ArrayList;

public class GlobalStaticVariables {
	public static ArrayList <Contact> selectedContacts = new ArrayList<Contact>();
	public static ArrayList <Music> selectedMusic = new ArrayList<Music>();
	
	public static void resetContacts()
	{
		selectedContacts.clear();
	}
	
	public static void resetMusic()
	{
		selectedMusic.clear();
	}
}
