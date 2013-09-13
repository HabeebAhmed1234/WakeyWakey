package com.example.alarmclock;

import java.io.IOException;
import java.util.ArrayList;
import android.content.Context;
import android.media.AudioManager;
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
	
	private AudioManager audioVolume;
	private int savedVolume = 0;
	
	MusicAlertActivity (Context context, Alarm alarm)
	{
		this.context=context;
		
		prefsHandler = new PreferencesHandler(context);
		prefs = prefsHandler.getSettings();
		musicList = alarm.getMusicList();
		
		audioVolume = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}
	
	public void start()
	{
			savedVolume = audioVolume.getStreamVolume(AudioManager.STREAM_MUSIC);
			audioVolume.setStreamVolume(AudioManager.STREAM_MUSIC, audioVolume.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_SHOW_UI);
		
			player = new MediaPlayer();
			player.setLooping(true);
			
			if(!(musicList==null || musicList.size()==0))
			{
				play(musicList.get(0).getPath());
			}else{
				//play default alarm
				player = MediaPlayer.create(context,R.raw.default_alarm);
				player.setLooping(true);
                player.start();
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
		audioVolume.setStreamVolume(AudioManager.STREAM_MUSIC, this.savedVolume, AudioManager.FLAG_SHOW_UI);
	}
	
	public boolean isPlaying()
	{
		return player.isPlaying();
	}
	

}
