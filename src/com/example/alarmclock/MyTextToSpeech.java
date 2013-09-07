package com.example.alarmclock;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

public class MyTextToSpeech implements OnInitListener {

    TextToSpeech talker;
    
    MyTextToSpeech(Context con)
    {
    	talker=new TextToSpeech(con, this);
    }
    
	public void say(String text2say){
		
		try{
			talker.speak(text2say, TextToSpeech.QUEUE_FLUSH, null);
		}
		catch (Exception e){
			Log.d("AndroidNews", "failed");
			throw new RuntimeException(e);
		}
	}

    public void onInit(int status) {
        say("Initialized");
    }
}