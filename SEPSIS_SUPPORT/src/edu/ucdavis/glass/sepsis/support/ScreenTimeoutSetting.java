package edu.ucdavis.glass.sepsis.support;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

public class ScreenTimeoutSetting extends Activity 
{	
	private static final int TOUCH_PAD_MAX_DISPLACEMENT = 1000;
	private static final int MIN_TIMEOUT_SETTING = 30;
	private SeekBar screenTimeoutSeekBar;
	private TextView progressStatus;
	private int currentProgress = Global.options.screenTimeout;
	private GestureDetector mGestureDetector;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_timeout_setting);
		mGestureDetector = createGestureDetector(this);
		screenTimeoutSeekBar = (SeekBar) findViewById(R.id.screenTimeoutSeekBar);
		progressStatus = (TextView) findViewById(R.id.screenTimeoutProgressStatus);
		screenTimeoutSeekBar.setMax(TOUCH_PAD_MAX_DISPLACEMENT); //scale to touch pad displacement
		progressStatus.setText(String.valueOf(currentProgress));
		currentProgress *= 10;//scale to touch pad
		screenTimeoutSeekBar.setProgress(currentProgress);//scale to touch pad
		
	}
	
	private GestureDetector createGestureDetector(Context context) 
	{
    GestureDetector gestureDetector = new GestureDetector(context);
        // create a base listener for generic gestures
        gestureDetector.setBaseListener( new GestureDetector.BaseListener() 
        {
            @Override
            public boolean onGesture(Gesture gesture) 
            {
                if (gesture == Gesture.TAP) 
                {
                	//rescale to 30-130
            		currentProgress /= 10;
            		currentProgress += MIN_TIMEOUT_SETTING;
            		
            		//displace Current Progress
					Toast.makeText(ScreenTimeoutSetting.this,"Screen Timeout:"+currentProgress,Toast.LENGTH_SHORT).show();
					
            		Intent resultScreenTimeoutIntent = getIntent(); //result Intent will be returned to the caller, OptionsActivity
            		resultScreenTimeoutIntent.putExtra("New Screen Timeout Setting", currentProgress);
            		setResult(RESULT_OK, resultScreenTimeoutIntent);
            		System.out.println("here is line 58 in SreentimeoutSetting.java");
            		finish();
                    return true;
                } 
                else if (gesture == Gesture.TWO_TAP) 
                {
                	return true;
                } 
                else if (gesture == Gesture.SWIPE_RIGHT) 
                {
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_LEFT) 
                {
                	return true;
                }
                return false;
            }
        });
        
        gestureDetector.setFingerListener(new GestureDetector.FingerListener() {
			
			@Override
			public void onFingerCountChanged(int previousCount, int currentCount) {
				int progressDisplay = currentProgress;
				if (currentCount == 0)
				{
					//rescale to 30-130
					progressDisplay /= 10;
					progressDisplay += MIN_TIMEOUT_SETTING; 
					progressStatus.setText(String.valueOf(progressDisplay));
	                
				}
			}
		});
        
        gestureDetector.setScrollListener(new GestureDetector.ScrollListener() {
            @Override
            public boolean onScroll(float displacement, float delta, float velocity) {
            	//System.out.println(displacement);
            	currentProgress += (int) (displacement);
            	
            	//set boundaries to make seek bar look realistic
            	if (currentProgress < 0) {
            		currentProgress = 0;
            	}
            	if (currentProgress > TOUCH_PAD_MAX_DISPLACEMENT) {
            		currentProgress = TOUCH_PAD_MAX_DISPLACEMENT;
            	}
            	
            	screenTimeoutSeekBar.setProgress(currentProgress);
            	return true;
            }
        });
        
        return gestureDetector;
    }

    // send generic motion events to the gesture detector
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) 
    {
        if (mGestureDetector != null) 
            return mGestureDetector.onMotionEvent(event);
        
        return false;
    }

}