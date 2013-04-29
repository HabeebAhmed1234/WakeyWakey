package com.example.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;
import com.example.alarmclock.MyTextToSpeech;

public class AlarmReceiver extends BroadcastReceiver implements TextToSpeech.OnInitListener {

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context,  "Alarm has gone off", Toast.LENGTH_LONG).show();
		MyTextToSpeech speaker = new MyTextToSpeech(context);
		speaker.say("Wake up!");
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		
	}
	
}
