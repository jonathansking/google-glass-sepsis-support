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

public class MaxPatientSetting extends Activity 
{	
	private static final int TOUCH_PAD_MAX_DISPLACEMENT = 1000;
	private static final int MIN_PATIENT_SETTING = 5;
	private SeekBar maxPatientSeekBar;
	private TextView progressStatus;
	private int currentProgress = Global.options.numberOfRecentPatients;
	private GestureDetector mGestureDetector;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.max_patient_setting);
		mGestureDetector = createGestureDetector(this);
		maxPatientSeekBar = (SeekBar) findViewById(R.id.maxPatientSeekBar);
		progressStatus = (TextView) findViewById(R.id.maxPatientProgressStatus);
		maxPatientSeekBar.setMax(TOUCH_PAD_MAX_DISPLACEMENT); //scale to touch pad displacement
		progressStatus.setText(String.valueOf(currentProgress));
		currentProgress *= 100;//scale to touch pad
		maxPatientSeekBar.setProgress(currentProgress);//scale to touch pad
		
	}
//
//	protected void onDestroy()
//	{
//		super.onDestroy();
//		setResult(RESULT_CANCELED, getIntent());
//	}
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
                	//rescale to 5-15
            		currentProgress /= 100;
            		currentProgress += MIN_PATIENT_SETTING;
            		//displace Current Progress
					Toast.makeText(MaxPatientSetting.this,"Max Recient Patient:"+currentProgress,Toast.LENGTH_SHORT).show();
            		Intent resultIntent = getIntent(); //resultItent will be returned to the caller, OptionsActivity
            		resultIntent.putExtra("New Max Patients Setting", currentProgress);
            		setResult(RESULT_OK, resultIntent);
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
					//rescale to 5-15
					progressDisplay /= 100;
					progressDisplay += MIN_PATIENT_SETTING; 
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
            	
            	maxPatientSeekBar.setProgress(currentProgress);
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