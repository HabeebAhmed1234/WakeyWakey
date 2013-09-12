package com.example.alarmclock;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import com.example.alarmclock.IntValueStore.IntValueStoreListener;

public class ShakeToWakeActivity implements IntValueStoreListener, PercentageUpdater {
	  private SensorManager mSensorManager;
	  private float mAccel; // acceleration apart from gravity
	  private float mAccelCurrent; // current acceleration including gravity
	  private float mAccelLast; // last acceleration including gravity
	  private Context context;
	  private static float percentageBarfill;
	  private AlarmHandlerInterface alhi; 
	  private IntValueStore stagnantCount;
	  
	  private final SensorEventListener mSensorListener = new SensorEventListener() {

	    public void onSensorChanged(SensorEvent se) {
	      float x = se.values[0];
	      float y = se.values[1];
	      float z = se.values[2];
	      mAccelLast = mAccelCurrent;
	      mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
	      float delta = mAccelCurrent - mAccelLast;
	      mAccel = mAccel * 0.9f + delta; // perform low-cut filter
	      
	      	 if (mAccel >= 10) {
	          Log.d("accel", "accel");
	          if (percentageBarfill >= 1){
	        	  alhi.hideShakeToWakeScreen();
	        	  alhi.stopAlarm();
	          }
	          if (!(percentageBarfill >= 1)) percentageBarfill+=0.02;
	          stagnantCount.setValue(0);
	          alhi.showShakeToWakeScreen();
	         }
	         else 
	         {
        		 stagnantCount.setValue(stagnantCount.getValue() + 1);
	        	 Log.d("accel", "stagnant");
	        	 Log.d("accel", Integer.toString(stagnantCount.getValue()));
	         }
	    }

	    public void onAccuracyChanged(Sensor sensor, int accuracy) {

	    }
	  };
	 
	ShakeToWakeActivity (Context context, AlarmHandlerInterface alhi)
	{
		this.context=context;
		/* do this in onCreate */
	    mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	    mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	    mAccel = 0.00f;
	    mAccelCurrent = SensorManager.GRAVITY_EARTH;
	    mAccelLast = SensorManager.GRAVITY_EARTH;
	    percentageBarfill=0;
	    this.alhi = alhi;
	    stagnantCount = new IntValueStore(0);
	    stagnantCount.setListener(this);
	}
	
	@Override
	public void onValueChanged(int newValue) {
		alhi.hideShakeToWakeScreen();
	}
	
	@Override
	public float getPercentage() {
		return percentageBarfill;		
	}

}