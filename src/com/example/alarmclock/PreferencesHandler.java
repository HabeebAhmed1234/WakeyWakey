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
	
	void setFacebookOption(boolean setting)
	{
		if(setting)
		{
			set(FACEBOOK_KEY,"true");
		}else
		{
			set(FACEBOOK_KEY,"false");
		}
	}
	
	void setVideoNewsOption(boolean setting)
	{
		if(setting)
		{
			set(VIDEONEWS_KEY,"true");
		}else
		{
			set(VIDEONEWS_KEY,"false");
		}
	}
	
	void setTextContactsOption(boolean setting)
	{
		if(setting)
		{
			set(TEXTCONTACTS_KEY,"true");
		}else
		{
			set(TEXTCONTACTS_KEY,"false");
		}
	}
	
	void setTextContactsList(ArrayList<Contact> contacts)
	{
		//shows the number of contacts in total
		this.set(CONTACTS_AMMOUNT_KEY,Integer.toString(contacts.size()));
		
		//Log.d("debuggings","set contacts ammount " + Integer.toString(contacts.size()));
		
		for(int i=0;i<contacts.size();i++)
		{
			this.set("CONTACT_NUMBER_"+i,contacts.get(i).getNumber());
			this.set("CONTACT_NAME_"+i,contacts.get(i).getName());
		}
	}
	
	void setMusicOption(boolean setting)
	{
		if(setting)
		{
			set(MUSIC_KEY,"true");
		}else
		{
			set(MUSIC_KEY,"false");
		}
	}
	
	void setMusicList(ArrayList<Music> selectedMusic) {
		//shows the number of contacts in total
		this.set(MUSICS_AMMOUNT_KEY,Integer.toString(selectedMusic.size()));
		
		//Log.d("debuggings","set musics ammount " + Integer.toString(selectedMusic.size()));
		
		for(int i=0;i<selectedMusic.size();i++)
		{
			this.set("MUSIC_NUMBER_"+i,selectedMusic.get(i).getPath());
			this.set("MUSIC_NAME_"+i,selectedMusic.get(i).getName());
		}
	}
	
	Preferences getSettings()
	{
		Preferences prefs=new Preferences();
		
		if(get(this.FACEBOOK_KEY)=="true")
		{
			prefs.setfacebook(true);
		}else{
			prefs.setfacebook(false);
		}
		
		if(get(this.VIDEONEWS_KEY)=="true")
		{
			prefs.setvideonews(true);
		}else{
			prefs.setvideonews(false);
		}
		
		if(get(this.TEXTCONTACTS_KEY)=="true")
		{
			prefs.settextcontacts(true);
		}else{
			prefs.settextcontacts(false);
		}
		
		if(get(this.MUSIC_KEY)=="true")
		{
			prefs.setmusic(true);
		}else{
			prefs.setmusic(false);
		}
		
		//add in contacts
		String ammountOfContacts=get(CONTACTS_AMMOUNT_KEY);
		if(ammountOfContacts.compareTo("NULL")==0)
		{
			return prefs;
		}else
		{
			for(int i = 0; i<Integer.parseInt(ammountOfContacts);i++)
			{
				Contact cont = new Contact(get("CONTACT_NUMBER_"+i),get("CONTACT_NAME_"+i));
				cont.select();
				prefs.addContactToList(cont);
			}
		}
		//Log.d("debuggings","get contacts ammount " + Integer.toString(prefs.getContactList().size()));
		
		//add in music
		String ammountOfMusic=get(MUSICS_AMMOUNT_KEY);
		if(ammountOfMusic.compareTo("NULL")==0)
		{
			return prefs;
		}else
		{
			for(int i = 0; i<Integer.parseInt(ammountOfMusic);i++)
			{
				Music mus = new Music(get("MUSIC_NUMBER_"+i),get("MUSIC_NAME_"+i));
				mus.select();
				prefs.addMusicToList(mus);
			}
		}
		//Log.d("debuggings","get musics ammount " + Integer.toString(prefs.getMusicList().size()));
		return prefs;
	}
}
