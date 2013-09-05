package com.example.alarmclock;

import com.example.alarmclock.R.color;
import com.example.alarmclock.PercentageUpdater;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;

public class CustomImageView extends ImageView {
	
	Paint paint = new Paint();
	public PercentageUpdater alhi;
	
	public CustomImageView(Context context) {
		super(context);
	}
	
	public CustomImageView(Context context, PercentageUpdater alhi){
		super(context);
		this.alhi = alhi;
	}

	public void onDraw(Canvas canvas) {
	    super.onDraw(canvas);
	    
	    paint.setColor(getResources().getColor(R.color.batteryJuice));
	    paint.setStrokeWidth(0);
	    int midX = canvas.getWidth()/2;
	    float width = (float) (canvas.getWidth()*0.075);
	    float base = (float) (canvas.getHeight()*0.92);
	    float fullHeight = (float) (canvas.getHeight()*0.1 - base);
	    float percentage = alhi.getPercentage();
	    // left, top, right, bottom
	    canvas.drawRect(midX - width, (float) (base+percentage*fullHeight), midX + width, base, paint);
	}
	
}