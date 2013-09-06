package com.example.alarmclock;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmFactory {
	private Context context;
	public static final String ALARM_ID = "ALARM_ID";
	private AlarmManager am;
	
	AlarmFactory(Context context)
	{
		this.context = context;
		am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
	}
	
	public void setAlarm(Alarm alarm)
	{
		Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, alarm.getMinute());
        cal.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Log.d("AlarmClock", " Alarm set at "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE));
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(ALARM_ID, Integer.toString(alarm.getID()));
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
	}
	
	public void cancelAlarm(Alarm alarm)
	{
		Intent intent = new Intent(context, AlarmReceiver.class);
		PendingIntent displayIntent = PendingIntent.getBroadcast(context, alarm.getID(), intent, PendingIntent.FLAG_NO_CREATE);
	
		if(displayIntent != null) {
			am.cancel(displayIntent);
			displayIntent.cancel();  
		}
	}
	
	public void refreshAlarm(Alarm alarm)
	{
		cancelAlarm(alarm);
		setAlarm(alarm);
	}
}
