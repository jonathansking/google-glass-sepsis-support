package edu.ucdavis.glass.sepsis.support;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

public class OptionsActivity extends Activity 
{	
	private GestureDetector mGestureDetector;
	private TextView recentPatientOptionTxtView;
	private TextView timeoutOptionTxtView;
	
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
	    
        setContentView(R.layout.options);
        mGestureDetector = createGestureDetector(this);

        timeoutOptionTxtView = (TextView) findViewById(R.id.timeoutOption);
	    timeoutOptionTxtView.setText(String.valueOf(Global.options.screenTimeout));
        
        recentPatientOptionTxtView = (TextView) findViewById(R.id.recentPatientOption);
	    recentPatientOptionTxtView.setText(String.valueOf(Global.options.numberOfRecentPatients));
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options_menu, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    switch (item.getItemId()) 
	    {
	        case R.id.set_screen_timeout:
	        	startActivityForResult(new Intent(OptionsActivity.this, ScreenTimeoutSetting.class), 0);
	            return true;
	        case R.id.set_num_recent_patients:
	        	startActivityForResult(new Intent(OptionsActivity.this, MaxPatientSetting.class), 1);
	            return true;
	        case R.id.reset_options:
	        	Global.options = new Global.Options();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
    	super.onActivityResult(requestCode, resultCode, data);

    	if(resultCode == RESULT_CANCELED)
    	{
    		Toast.makeText(OptionsActivity.this,"Settings are not saved",Toast.LENGTH_SHORT).show();
    	}
    	else
    	{
	    	if (requestCode == 0)
	    	{
	    		int screenTimeoutSetting = data.getExtras().getInt("New Screen Timeout Setting", Global.options.screenTimeout);
	    		System.out.println("here is line 77");
	    		Global.options.screenTimeout = screenTimeoutSetting;
	    		timeoutOptionTxtView.setText(String.valueOf(Global.options.screenTimeout));
	    		System.out.println("here is line 80");
	    	}
	    	else if(requestCode == 1)
	    	{
	    		int maxPatientSetting = data.getExtras().getInt("New Max Patients Setting", Global.options.numberOfRecentPatients);
	    		//System.out.println(maxPatientSetting);
	    		Global.options.numberOfRecentPatients = maxPatientSetting;
	    		recentPatientOptionTxtView.setText(String.valueOf(Global.options.numberOfRecentPatients));
	    	}
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

        
        gestureDetector.setScrollListener(new GestureDetector.ScrollListener() {
            @Override
            public boolean onScroll(float displacement, float delta, float velocity) {
                // do something on scrolling
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
