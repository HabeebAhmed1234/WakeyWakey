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
        
        Calendar nowTime = Calendar.getInstance();
        
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(ALARM_ID, Integer.toString(alarm.getID()));
        PendingIntent sender = PendingIntent.getBroadcast(context, alarm.getID(), intent, 0);
        
        long firstTriggerTimeInMillis = cal.getTimeInMillis();
        
        if(firstTriggerTimeInMillis<(nowTime.getTimeInMillis())-60000)
        {
        	firstTriggerTimeInMillis+=AlarmManager.INTERVAL_DAY;
        }
        
        Log.d("AlarmClock", " Alarm set at "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+ " on The day "+cal.get(Calendar.DAY_OF_YEAR) +"first trigger time is __days from now"+(firstTriggerTimeInMillis-cal.getTimeInMillis())/1000/60/60/24);
        
        if(alarm.isRepeatedDaily())
    	{
    		am.setRepeating(AlarmManager.RTC_WAKEUP, firstTriggerTimeInMillis, AlarmManager.INTERVAL_DAY, sender);
    	}else{
    		am.set(AlarmManager.RTC_WAKEUP, firstTriggerTimeInMillis, sender);
    	}
        
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
