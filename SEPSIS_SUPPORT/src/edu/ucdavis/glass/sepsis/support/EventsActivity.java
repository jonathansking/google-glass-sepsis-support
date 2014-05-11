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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TableRow.LayoutParams;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.polysfactory.headgesturedetector.*;


public class EventsActivity extends Activity implements OnHeadGestureListener
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
        
        /* set view for events */
        setContentView(R.layout.events);
        TableLayout eventsTableLO = (TableLayout) findViewById(R.id.eventsTableLayout);
        
        //create rows
        for (int i = 0; i < Global.options.eventsDisplay; i++) //MAX_EVENT is the maximum events will be display, not yet created
        {
        	//Create Table row
        	TableRow eventRow = new TableRow(this);
        	eventRow.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        	
        	// Create Text View for RANK and add to table row
        	TextView rank = new TextView(this);
        	rank.setTextAppearance(this, R.style.GlassText_XSmall);
        	rank.setText(String.valueOf(i+1));
        	eventRow.addView(rank);
        	
        	// Create Text View for TIME and add to table row
        	TextView time = new TextView(this);
        	time.setTextAppearance(this, R.style.GlassText_Small);
        	time.setText("5/11 00:00");
        	eventRow.addView(time);
        	
        	// Create Text View for EVENT and add to table row
        	TextView event = new TextView(this);
        	event.setTextAppearance(this, R.style.GlassText_Small);
        	event.setText("Event #" + String.valueOf(i+1));
        	eventRow.addView(event);
        	
        	// Create Text View for ATTRIBUTE and add to table row
        	TextView attr = new TextView(this);
        	attr.setTextAppearance(this, R.style.GlassText_Small);
        	attr.setText("Attribute #" + String.valueOf(i+1));
        	eventRow.addView(attr);
        	
        	// Create Text View for STATES and add to table row
        	TextView state = new TextView(this);
        	state.setTextAppearance(this, R.style.GlassText_Small);
        	state.setText("BS");
        	eventRow.addView(state);
        	
        	// Add row to table
        	eventsTableLO.addView(eventRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        }
        
        /* Set view done*/
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
                	startActivity( new Intent(getApplicationContext(), SupportActivity.class) );
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_LEFT) 
                {
                	startActivity( new Intent(getApplicationContext(), VitalsActivity.class) );
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

    // headgestures
    @Override
    public void onShakeToRight() {
    	startActivity( new Intent(getApplicationContext(), SupportActivity.class) );
    }

    @Override
    public void onShakeToLeft() {
    	startActivity( new Intent(getApplicationContext(), VitalsActivity.class) );
    }
    
    @Override
    public void onNod(){
    	// Do something
    }
}
