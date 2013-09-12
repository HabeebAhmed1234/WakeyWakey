package com.example.alarmclock;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

public class MyTextToSpeech implements OnInitListener {

    TextToSpeech talker;
    
    private boolean initialized = false;
    
    ArrayList<String> pendingSayings = new ArrayList<String>();
    
    boolean isfirstsaying = true;
    MediaPlayer player;
    Context context;
    
    MyTextToSpeech(Context con)
    {
    	context = con;
    	talker=new TextToSpeech(con, this);
    }
    
	public void say(String text2say){
		if(isfirstsaying)
		{

			player = MediaPlayer.create(context,R.raw.platinum);
			player.setLooping(true);
            player.start();
            
			isfirstsaying = false;
		}
		if(initialized)
		{
			try{
				Log.d("AlarmClock", "tts saying "+text2say);
				talker.speak(text2say, TextToSpeech.QUEUE_ADD, null);
			}
			catch (Exception e){
				Log.d("AlarmClock", "failed");
				throw new RuntimeException(e);
			}
		}else
		{
			pendingSayings.add(text2say);
		}
	}
	
	public void stop()
	{
		if(talker==null) return;
		Log.d("AlarmClock","Turning off tts");
		talker.speak("Alarm Off", TextToSpeech.QUEUE_FLUSH, null);
		talker.stop();
		
		if(player.isPlaying())
		{
			player.stop();
		}
	}

    public void onInit(int status) {
    	Log.d("AlarmClock", "tts initialized");
        initialized = true;
        
        if(pendingSayings.size()>0)
        {
        	for(int i = 0 ; i<pendingSayings.size() ; i++)
        	{
        		say(pendingSayings.get(i));
        	}
        	pendingSayings.clear();
        }
    }
    
    public void shutDown()
    {
    	Log.d("AlarmClock","Shutting down TTS");
    	talker.shutdown();
    }
}