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
		
		String alarmID = intent.getExtras().getString(AlarmFactory.ALARM_ID);
		
		 //start activity
        Intent i = new Intent();
        i.putExtra(AlarmFactory.ALARM_ID, alarmID);
        i.setClassName("com.example.alarmclock", "com.example.alarmclock.AlarmHandler");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		
	}
	
}
