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
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.polysfactory.headgesturedetector.*;


public class EventsActivity extends ListActivity implements OnHeadGestureListener, Global.AsyncTaskCompleteListener<JSONObject>
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
	    AsyncTask<String, Void, JSONObject> JSON = new LoadJSONAsyncTask( this, "Loading Patient's Events...", this );
	    
	    // run AsyncTask
	    JSON.execute( "events" );
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
	        header.setText("Events");
	        
	        this.getListView().addHeaderView(view, null, false);
	        mListView = this.getListView();
	        
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
                	// go to overview
                	startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_LEFT) 
                {
                	// go to support view
                	startActivity( new Intent(getApplicationContext(), SupportActivity.class) );
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
    	// go to support view
    	startActivity( new Intent(getApplicationContext(), SupportActivity.class) );
    }

    @Override
    public void onShakeToRight() {
    	// go to overview
    	startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
    }
    
    @Override
    public void onNod(){
    	// Do something
    }
}
