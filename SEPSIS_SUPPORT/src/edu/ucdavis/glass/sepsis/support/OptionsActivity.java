package edu.ucdavis.glass.sepsis.support;

import java.util.ArrayList;

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
	private TextView headGestureTxtView;
	
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
	    
        setContentView(R.layout.options);
        mGestureDetector = createGestureDetector(this);

        // timeout option
        timeoutOptionTxtView = (TextView) findViewById(R.id.timeoutOption);
	    timeoutOptionTxtView.setText(String.valueOf(Global.options.screenTimeout));
        
	    // number of recent patient option
        recentPatientOptionTxtView = (TextView) findViewById(R.id.recentPatientOption);
	    recentPatientOptionTxtView.setText(String.valueOf(Global.options.numberOfRecentPatients));
	    
	    // headgesture option
	    headGestureTxtView = (TextView) findViewById(R.id.headGesture);
	    if(Global.options.headGesture)
	    	headGestureTxtView.setText("ON");
	    else
	    	headGestureTxtView.setText("OFF");
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
	        	Intent intent_st = new Intent(this, ValueSelectorActivity.class);
	        	ArrayList<Integer> valueList = makeIntegerArrayList(15, 200, 5);
	        	valueList.add(-1);
	        	intent_st.putExtra("values", valueList);
	        	intent_st.putExtra("requestCode", 0);
	        	startActivityForResult(intent_st, 0);
	            return true;
	        case R.id.set_num_recent_patients:
	        	Intent intent_nrp = new Intent(this, ValueSelectorActivity.class);
	        	intent_nrp.putExtra("values", makeIntegerArrayList(1, 21, 1));
	        	intent_nrp.putExtra("requestCode",1);
	        	startActivityForResult(intent_nrp, 1);
	            return true;
	        case R.id.set_head_gesture:
	        	Intent intent_hg = new Intent(this, ValueSelectorActivity.class);
	        	intent_hg.putExtra("values", makeIntegerArrayList(0, 2, 1));
	        	intent_hg.putExtra("requestCode",2);
	        	startActivityForResult(intent_hg, 2);
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
    		Toast.makeText(OptionsActivity.this,"Settings were not saved.",Toast.LENGTH_SHORT).show();
    	}
    	else
    	{
	    	if (requestCode == 0)
	    	{
	    		int screenTimeoutSetting = data.getExtras().getInt("selected_value", Global.options.screenTimeout);
	    		Global.options.screenTimeout = screenTimeoutSetting;
	    		timeoutOptionTxtView.setText(String.valueOf(Global.options.screenTimeout));

	    		android.provider.Settings.System.putInt(getContentResolver(),android.provider.Settings.System.SCREEN_OFF_TIMEOUT, Global.options.screenTimeout*1000);
	    	}
	    	else if(requestCode == 1)
	    	{
	    		int maxPatientSetting = data.getExtras().getInt("selected_value", Global.options.numberOfRecentPatients);
	    		Global.options.numberOfRecentPatients = maxPatientSetting;
	    		recentPatientOptionTxtView.setText(String.valueOf(Global.options.numberOfRecentPatients));
	    	}
	    	else if(requestCode == 2)
	    	{
	    		boolean headGestureSetting = data.getExtras().getBoolean("selected_value", Global.options.headGesture);
	    		Global.options.headGesture = headGestureSetting;
	    		if(Global.options.headGesture)
	    	    	headGestureTxtView.setText("ON");
	    	    else
	    	    	headGestureTxtView.setText("OFF");
	    	}
    	}
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
                else if (gesture == Gesture.SWIPE_RIGHT) 
                {
                	// go back to welcome screen
                	finish();
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

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) 
    {
        if (mGestureDetector != null) 
            return mGestureDetector.onMotionEvent(event);
        
        return false;
    }
    
    private ArrayList<Integer> makeIntegerArrayList(int min, int max, int increment) 
    {
	    ArrayList<Integer> l = new ArrayList<Integer>();
	
	    for( Integer i=min; i<max; i+=increment )
	    {
	    	l.add(i);
	    }
	    
	    return l;
    }
}
