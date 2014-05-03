package edu.ucdavis.glass.sepsis.support;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class ErrorActivity extends Activity 
{
	private GestureDetector mGestureDetector;
	private TextView errorMessageTxtView;
	
	protected void onCreate(Bundle savedInstanceState)
	{ 
		super.onCreate(savedInstanceState);

        mGestureDetector = createGestureDetector(this);
        
        Intent errorIntent = getIntent();
        String errorMessage = errorIntent.getStringExtra(Global.ERROR_MSG);
        setContentView(R.layout.error);
        
        errorMessageTxtView = (TextView) findViewById(R.id.errorMessageTxtView);
	    this.errorMessageTxtView.setText(errorMessage); 
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
