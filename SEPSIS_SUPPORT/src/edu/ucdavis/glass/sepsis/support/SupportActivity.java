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
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.polysfactory.headgesturedetector.*;


public class SupportActivity extends ListActivity implements OnHeadGestureListener
{
	
	private GestureDetector mGestureDetector;
	private HeadGestureDetector mHeadGestureDetector;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        mGestureDetector = createGestureDetector(this);
        mHeadGestureDetector = new HeadGestureDetector(this);
        mHeadGestureDetector.setOnHeadGestureListener(this);
        mHeadGestureDetector.start();
        
        // set up AsyncTask
	    AsyncTask<String, Void, JSONObject> JSON = new LoadJSONAsyncTask( SupportActivity.this, "Loading Support..." );
	    
	    // run AsyncTask
	    JSON.execute( Global.recentPatients.peek().getId(), "support" );
		
	    // create ListView with information from JSON
	    try {
			setListAdapter(new JSONObjectAdapter(this, JSON.get() ));
			
			// add header
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        View view = inflater.inflate(R.layout.header, null);

	        TextView header = (TextView) view.findViewById(R.id.heading);
	        header.setText("Support");
	        
	        this.getListView().addHeaderView(view, null, false);
	        
		} catch (Exception e) {
			// error
            System.out.println("unable to read json.");
            Global.alertUser(this, "Exception", "Unable to read JSON.");
            finish();
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
                    return true;
                } 
                else if (gesture == Gesture.TWO_TAP) 
                {
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_RIGHT) 
                {
                	// go to events
                	startActivity( new Intent(getApplicationContext(), EventsActivity.class) );
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_LEFT) 
                {
                	// go to policy
                	startActivity( new Intent(getApplicationContext(), PolicyActivity.class) );
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
    	// go to policy
    	startActivity( new Intent(getApplicationContext(), PolicyActivity.class) );
    }

    @Override
    public void onShakeToRight() {
    	// go to events
    	startActivity( new Intent(getApplicationContext(), EventsActivity.class) );
    }
    
    @Override
    public void onNod(){
    	// Do something
    }
}
