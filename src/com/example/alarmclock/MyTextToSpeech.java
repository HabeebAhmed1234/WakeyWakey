package com.example.alarmclock;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class MyTextToSpeech implements OnInitListener {

    TextToSpeech talker;
    
    MyTextToSpeech(Context con)
    {
    	talker=new TextToSpeech(con, this);
    }
    
	public void say(String text2say){
	    talker.speak(text2say, TextToSpeech.QUEUE_FLUSH, null);
	}

    public void onInit(int status) {
            say("Initialized");
    }
}