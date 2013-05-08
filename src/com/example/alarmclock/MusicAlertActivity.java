package com.example.alarmclock;

import java.io.IOException;
import java.util.ArrayList;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class MusicAlertActivity extends Activity {
	PreferencesHandler prefsHandler;
	Preferences prefs;
	
	MediaPlayer player;
	
	ArrayList <Music> musicList;
	
	private int playIndex;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("music","music Alert Activity Started");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_alert);
		
		prefsHandler = new PreferencesHandler(this);
		prefs = prefsHandler.getSettings();
		musicList = prefs.getMusicList();
		playIndex=0;
		
		if(musicList.size()>0)
		{	
			player = new MediaPlayer();
			play(musicList.get(playIndex).getPath());
			playIndex+=1;
			
			player.setOnCompletionListener((new OnCompletionListener(){
			    // @Override
			    public void onCompletion(MediaPlayer arg0) {
			    // File has ended, play the next one.
			    	if(playIndex<musicList.size())
			    	{
				    	play(musicList.get(playIndex).getPath());
				    	playIndex+=1; //increment the index to get the next audiofile
			    	}else
			    	{
			    		player.reset();
			    		player.stop();
			    	}   	
			    }
			}));
		}
	}
	
	void play(String file)
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_music_alert, menu);
		return true;
	}

}
