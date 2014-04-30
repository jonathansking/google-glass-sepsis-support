package edu.ucdavis.glass.sepsis.support;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

public class OptionsActivity extends Activity 
{
	private GestureDetector mGestureDetector;

	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);

        setContentView(R.layout.options);
        mGestureDetector = createGestureDetector(this);
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
                	// bring up menu
                    return true;
                } 
                else if (gesture == Gesture.TWO_TAP) 
                {
                	return true;
                } 
                else if (gesture == Gesture.SWIPE_RIGHT) 
                {
                	// go back to welcome screen
                	finish();
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_LEFT) 
                {
                	return true;
                }
                
                return false;
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
	
	public class Options 
	{
		public int screenTimeout;
		public int numberOfRecentPatients;
		
		public Options() {
			screenTimeout = 30;
			numberOfRecentPatients = 5;
		}
	}
}
