package com.example.alarmclock;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class MyTextToSpeech extends Activity implements OnInitListener {

	TextToSpeech talker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        talker = new TextToSpeech(this, this);
    }

    public void say(String text2say){
    	talker.speak(text2say, TextToSpeech.QUEUE_FLUSH, null);
    }

	@Override
	public void onInit(int status) {
		say("Hello World");

	}

	@Override
	public void onDestroy() {
		if (talker != null) {
			talker.stop();
			talker.shutdown();
		}

		super.onDestroy();
	}
}