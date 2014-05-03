package edu.ucdavis.glass.sepsis.support;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

public class OptionsActivity extends Activity 
{
	// structure to save options
	public class Options 
	{
		public int screenTimeout;
		public int numberOfRecentPatients;
		
		public Options() {
			screenTimeout = 30;
			numberOfRecentPatients = 5;
		}
	}
	
	private GestureDetector mGestureDetector;

	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);

        setContentView(R.layout.options);
        mGestureDetector = createGestureDetector(this);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options_menu, menu);
	    return true;
	}

	public boolean onMenuItemClick(MenuItem item) 
	{
	    switch (item.getItemId()) 
	    {
	        case R.id.set_screen_timeout:
	        	Global.options.screenTimeout = selectValueStartingAt( Global.options.screenTimeout );
	            return true;
	        case R.id.set_num_recent_patients:
	        	Global.options.numberOfRecentPatients = selectValueStartingAt( Global.options.screenTimeout );
	            return true;
	        case R.id.reset_options:
	        	Global.options = new Options();
	            return true;
	        default:
	            return false;
	    }
	}
	
	int selectValueStartingAt( int s )
	{
		// make a bunch of cards allow the user to scroll through them and select one
		// return the selected value
		return 10; // for now
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
                	openOptionsMenu();
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
}
