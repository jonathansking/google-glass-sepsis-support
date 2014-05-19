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
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.ScrollView;
import android.widget.TableRow.LayoutParams;
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
	private ScrollView eventsScrollView;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        mGestureDetector = createGestureDetector(this);
        mHeadGestureDetector = new HeadGestureDetector(this);
        mHeadGestureDetector.setOnHeadGestureListener(this);
        mHeadGestureDetector.start();

        setContentView(R.layout.events);
        loadView();
        eventsScrollView = (ScrollView) findViewById(R.id.eventsScrollView);
	}
	
	private void loadView()
	{
        Patient p = Global.recentPatients.peek();
		TableLayout eventsTableLO = (TableLayout) findViewById(R.id.eventsTableLayout);
        
        // create rows
        for (int i = 0; i < Global.options.eventsDisplay; i++) // MAX_EVENT is the maximum events will be display, not yet created
        {
        	// create table rows
        	TableRow eventRow = new TableRow(this);
        	eventRow.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        	eventRow.setGravity(Gravity.CENTER);
        	
        	// create text views
        	TextView rank = new TextView(this);// index
        	TextView time = new TextView(this);// time stamp
        	TextView event = new TextView(this);// event
        	TextView attr = new TextView(this);// attribute
        	TextView state = new TextView(this);// state
        	
        	// set style
        	rank.setTextAppearance(this, R.style.GlassText_XXSmall);
        	time.setTextAppearance(this, R.style.GlassText_XXSmall);
        	event.setTextAppearance(this, R.style.GlassText_XXSmall);
        	attr.setTextAppearance(this, R.style.GlassText_XXSmall);
        	state.setTextAppearance(this, R.style.GlassText_XXSmall);
        	rank.setGravity(Gravity.CENTER);
        	time.setGravity(Gravity.CENTER);
        	event.setGravity(Gravity.CENTER);
        	attr.setGravity(Gravity.CENTER);
        	state.setGravity(Gravity.CENTER);
        	
        	// set data
        	rank.setText(String.valueOf(i+1));// index
        	time.setText(p.events.get(i).timeStamp);// time stamp
        	event.setText(p.events.get(i).event);// event
        	attr.setText(p.events.get(i).attribute);// attribute
        	state.setText(p.events.get(i).state);// state
        	
        	// add to table row
        	eventRow.addView(rank);
        	eventRow.addView(time);
        	eventRow.addView(event);
        	eventRow.addView(attr);
        	eventRow.addView(state);
        	
        	// add row to table
        	eventsTableLO.addView(eventRow, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
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
                if (gesture == Gesture.SWIPE_RIGHT) 
                {
                	finish();
                	startActivity( new Intent(getApplicationContext(), SupportActivity.class) );
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_LEFT) 
                {
                	finish();
                	startActivity( new Intent(getApplicationContext(), VitalsActivity.class) );
                    return true;
                }
                return false;
            }
        });
        gestureDetector.setScrollListener(new GestureDetector.ScrollListener() {
            @Override
            public boolean onScroll(float displacement, float delta, float velocity) {
				if(displacement > 0)
					eventsScrollView.smoothScrollTo(0,100);
				else
					eventsScrollView.smoothScrollTo(0,0);
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

    // headgestures
    @Override
    public void onShakeToRight() {
    	if(Global.options.headGesture)
    	{
    		finish();
    		startActivity( new Intent(getApplicationContext(), SupportActivity.class) );
    	}
    }

    @Override
    public void onShakeToLeft() {
    	if(Global.options.headGesture)
    	{
    		finish();
    		startActivity( new Intent(getApplicationContext(), VitalsActivity.class) );
    	}
    }
    
    @Override
    public void onNod(){
    	// do nothing
    }
}
