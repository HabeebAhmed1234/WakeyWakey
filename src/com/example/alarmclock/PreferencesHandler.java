package com.example.alarmclock;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHandler {

	private Context context;
	private static final String PREFS_NAME="CONFIG";
	public String FACEBOOK_KEY="facebookison";
	public String VIDEONEWS_KEY="videonewsison";
	public String TEXTCONTACTS_KEY="textcontactsison";
	public String MUSIC_KEY="musicison";
	PreferencesHandler(Context con)
	{
		context=con;
	}
	
	private void set(String settingname, String settingvalue)
	{
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(settingname, settingvalue);
        editor.commit();
	}
	
	private String get(String key)
	{
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
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
		
		return prefs;
	}
}
