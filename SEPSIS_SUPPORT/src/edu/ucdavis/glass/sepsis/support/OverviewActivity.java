/*
 * ..Copyright (C) 2013 The Android Open Source Project

 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.ucdavis.glass.sepsis.support;
import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Intent;
import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.polysfactory.headgesturedetector.*;


public class OverviewActivity extends ListActivity implements OnHeadGestureListener, Global.AsyncTaskCompleteListener<JSONObject>
{
	private GestureDetector mGestureDetector;
	private HeadGestureDetector mHeadGestureDetector;
	private ListView mListView;

	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        mGestureDetector = createGestureDetector(this);
        mHeadGestureDetector = new HeadGestureDetector(this);
        mHeadGestureDetector.setOnHeadGestureListener(this);
        mHeadGestureDetector.start();

        // set up AsyncTask
        AsyncTask<String, Void, JSONObject> JSON = new LoadJSONAsyncTask( this, "Loading Patient's Overview...", this );
	    
	    // run AsyncTask
  	  	JSON.execute( "overview" );
	}
	
	public void onTaskComplete(JSONObject json) 
	{
		// create ListView with information from JSON
	    try {
			setListAdapter(new JSONObjectAdapter(this, json ));
			
			// add header
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        View view = inflater.inflate(R.layout.header, null);

	        TextView header = (TextView) view.findViewById(R.id.heading);
//	        header.setText("Overview");
	        header.setText( Global.recentPatients.peek().getName() );
	        
	        this.getListView().addHeaderView(view, null, false);
	        mListView = this.getListView();
	        
		} catch (Exception e) {
			// error
            System.out.println("unable to read json.");
            Global.alertUser(this, "Exception", "Unable to read JSON.");
            finish();
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
    	super.onActivityResult(requestCode, resultCode, data);

    	if(resultCode == RESULT_CANCELED)
    	{
    		Toast.makeText(OverviewActivity.this,"Viewing state was not changed.",Toast.LENGTH_SHORT).show();
    	}
    	else
    	{
	    	if (requestCode == 0)
	    	{
	    		Patient p = Global.recentPatients.peek();
	    		int v = data.getExtras().getInt("selected_value", p.getViewingState());
	    		p.setViewingState(v);
	    		
	    		System.out.println("HERE");
            	// reload overview
            	startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
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
                	Intent i = new Intent(getApplicationContext(), ValueSelectorActivity.class);
    	        	i.putExtra( "values", Global.recentPatients.peek().getStates() );
    	        	startActivityForResult(i, 0);
                    return true;
                } 
                else if (gesture == Gesture.TWO_TAP) 
                {
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_RIGHT) 
                {
                	// go to vitals view
                	startActivity( new Intent(getApplicationContext(), VitalsActivity.class) );
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_LEFT) 
                {
                	// go to events view
                	startActivity( new Intent(getApplicationContext(), EventsActivity.class) );
                    return true;
                }
                else if (gesture == Gesture.LONG_PRESS) 
                {	
                	mListView.setScrollY( mListView.getScrollY()+100 );
                	return true;
                }
                return false;
            }
        });
        
        
//        gestureDetector.setScrollListener(new GestureDetector.ScrollListener() {
//            @Override
//            public boolean onScroll(float displacement, float delta, float velocity) {
//            	//System.out.println(displacement);
//            	currentProgress += (int) (displacement);
//            	
//            	//set boundaries to make seek bar look realistic
//            	if (currentProgress < 0) {
//            		currentProgress = 0;
//            	}
//            	if (currentProgress > TOUCH_PAD_MAX_DISPLACEMENT) {
//            		currentProgress = TOUCH_PAD_MAX_DISPLACEMENT;
//            	}
//            	
//            	screenTimeoutSeekBar.setProgress(currentProgress);
//            	return true;
//            }
//        });
        
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
    
    @Override
    protected void onResume() {
        mHeadGestureDetector.start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mHeadGestureDetector.stop();
        super.onPause();
    }

    @Override
    public void onShakeToLeft() {
    	// go to events view
    	startActivity( new Intent(getApplicationContext(), EventsActivity.class) );
    }

    @Override
    public void onShakeToRight() {
    	// go to vitals view
    	startActivity( new Intent(getApplicationContext(), VitalsActivity.class) );
    }
    
    @Override
    public void onNod(){
    	// Do something
    }
}
