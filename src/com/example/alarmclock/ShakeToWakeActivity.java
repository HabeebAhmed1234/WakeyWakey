package com.example.alarmclock;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class ShakeToWakeActivity {
	  private SensorManager mSensorManager;
	  private float mAccel; // acceleration apart from gravity
	  private float mAccelCurrent; // current acceleration including gravity
	  private float mAccelLast; // last acceleration including gravity
	  private Context context;
	  private int barfill;
	  
	  
 private final SensorEventListener mSensorListener = new SensorEventListener() {

	    public void onSensorChanged(SensorEvent se) {
	      float x = se.values[0];
	      float y = se.values[1];
	      float z = se.values[2];
	      mAccelLast = mAccelCurrent;
	      mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
	      float delta = mAccelCurrent - mAccelLast;
	      mAccel = mAccel * 0.9f + delta; // perform low-cut filter
	      
	      	 if (mAccel >= 2) {
	          Log.d("accel", "accel");
	          barfill+=1;
	         }
	         else 
	         {
	        	 Log.d("accel", "stagnant");
	        	 barfill-=1;
	         }
	    }

	    public void onAccuracyChanged(Sensor sensor, int accuracy) {

	    }
	  };
	 
	ShakeToWakeActivity (Context context)
	{
		this.context=context;
		/* do this in onCreate */
	    mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	    mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	    mAccel = 0.00f;
	    mAccelCurrent = SensorManager.GRAVITY_EARTH;
	    mAccelLast = SensorManager.GRAVITY_EARTH;
	    barfill=0;
	}
	
	int getBarFill()
	{
		return this.barfill;
	}
	
	

}
