package com.example.alarmclock;

import java.io.IOException;
import java.util.ArrayList;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import android.widget.Toast;

public class MusicAlertActivity {

	Context context;
	
	PreferencesHandler prefsHandler;
	Preferences prefs;
	
	MediaPlayer player;
	
	ArrayList <Music> musicList;
	
	MusicAlertActivity (Context context, Alarm alarm)
	{
		this.context=context;
		
		prefsHandler = new PreferencesHandler(context);
		prefs = prefsHandler.getSettings();
		musicList = alarm.getMusicList();
	}
	
	public void start()
	{
			player = new MediaPlayer();
			if(!(musicList==null || musicList.size()==0))
			{
				play(musicList.get(0).getPath());
			}else{
				Toast.makeText(this.context, "Please set a ringtone!", Toast.LENGTH_LONG).show();
			}
	}
		
	private void play(String file)
	{
		player.stop();
	  	player.reset();
	  	
        try {
                player.setDataSource(file);
                player.prepare();
                player.start();
        } catch (IllegalArgumentException e) {
                e.printStackTrace();
        } catch (IllegalStateException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
	}
	
	public void stop()
	{
		if(player.isPlaying())
		{
			player.stop();
		}
	}
	
	public boolean isPlaying()
	{
		return player.isPlaying();
	}
	

}
