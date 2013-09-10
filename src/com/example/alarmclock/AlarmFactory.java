package com.example.alarmclock;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class AlarmFactory {
	public static final String ALARM_ID = "ALARM_ID";
	public static final String IS_REPEATED = "ISREPEATED";
	public static  AlarmManager am;
	public static boolean isInit = false;
	
	public static void init()
	{
		AlarmFactory.am = (AlarmManager) MainActivity.CONTEXT.getSystemService(MainActivity.CONTEXT.ALARM_SERVICE);
		AlarmFactory.isInit = true;
	}
	
	public static void setAlarm(Alarm alarm)
	{	
		Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, alarm.getMinute());
        cal.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        Calendar nowTime = Calendar.getInstance();
        
        Intent intent = new Intent(MainActivity.CONTEXT, AlarmReceiver.class);
        intent.putExtra(ALARM_ID, Integer.toString(alarm.getID()));
        if(alarm.isRepeatedDaily())
        {
        	intent.putExtra(IS_REPEATED, "true");
        }
        intent.setData(Uri.parse("myalarms://" + alarm.getID()));
        PendingIntent sender = PendingIntent.getBroadcast(MainActivity.CONTEXT, alarm.getID(), intent, 0);

        long firstTriggerTimeInMillis = cal.getTimeInMillis();
        
        if(firstTriggerTimeInMillis<(nowTime.getTimeInMillis())-60000)
        {
        	firstTriggerTimeInMillis+=AlarmManager.INTERVAL_DAY;
        }
        
        Log.d("AlarmClock", "AlarmFactory: Alarm set at "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+ " on The day "+cal.get(Calendar.DAY_OF_YEAR) +"first trigger time is __days from now"+(firstTriggerTimeInMillis-cal.getTimeInMillis())/1000/60/60/24);
        
        if(alarm.isRepeatedDaily())
    	{
    		am.setRepeating(AlarmManager.RTC_WAKEUP, firstTriggerTimeInMillis, AlarmManager.INTERVAL_DAY, sender);
    	}else{
    		am.set(AlarmManager.RTC_WAKEUP, firstTriggerTimeInMillis, sender);
    	}
        
	}
	
	public static void cancelAlarm(Alarm alarm)
	{
		Intent intent = new Intent(MainActivity.CONTEXT, AlarmReceiver.class);
		intent.putExtra(ALARM_ID, Integer.toString(alarm.getID()));
        if(alarm.isRepeatedDaily())
        {
        	intent.putExtra(IS_REPEATED, "true");
        }
		intent.setData(Uri.parse("myalarms://" + alarm.getID()));
		PendingIntent displayIntent = PendingIntent.getBroadcast(MainActivity.CONTEXT, alarm.getID(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
	
		Log.d("AlarmClock", "AlarmFactory : Canceling Alarm id: "+alarm.getID());
		if(displayIntent != null) {
			am.cancel(displayIntent);
			displayIntent.cancel();  
		}else{
			Log.d("AlarmClock", "AlarmFactory: Canceling Alarm id: "+alarm.getID()+"failed because alarm is not set");
		}
	}
	
	public static void refreshAlarm(Alarm alarm)
	{
		cancelAlarm(alarm);
		setAlarm(alarm);
	}
	
	public static void confirmAlarms(ArrayList<Alarm> alarms)
	{
		for(int i = 0 ; i < alarms.size() ; i++)
		{
			if(AlarmFactory.isAlarmActive(alarms.get(i)))
			{
				if(!alarms.get(i).enabled())
				{
					Log.d("AlarmClock","AlarmFactory: Canceling alarm of id : "+alarms.get(i).getID());
					if(!AlarmFactory.isInit)AlarmFactory.init();
					AlarmFactory.cancelAlarm(alarms.get(i));
				}else
				{
					if(!AlarmFactory.isInit)AlarmFactory.init();
					AlarmFactory.refreshAlarm(alarms.get(i));
				}
			}else if (alarms.get(i).enabled())
			{
				Log.d("AlarmClock","AlarmFactory: Refreshing alarm of id : "+alarms.get(i).getID());
				if(!AlarmFactory.isInit)AlarmFactory.init();
				AlarmFactory.setAlarm(alarms.get(i));
			}
		}
	}
	
	private static boolean isAlarmActive(Alarm alarm)
	{
		Intent intent = new Intent(MainActivity.CONTEXT, AlarmReceiver.class);
		intent.putExtra(ALARM_ID, Integer.toString(alarm.getID()));
        if(alarm.isRepeatedDaily())
        {
        	intent.putExtra(IS_REPEATED, "true");
        }
		intent.setData(Uri.parse("myalarms://" + alarm.getID()));
		PendingIntent displayIntent = PendingIntent.getBroadcast(MainActivity.CONTEXT, alarm.getID(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
	
		if(displayIntent != null) {
			return false; 
		}else{
			return true;
		}
	}
}
