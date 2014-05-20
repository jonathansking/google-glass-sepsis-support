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
import android.widget.TextView;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.polysfactory.headgesturedetector.*;


public class SupportActivity extends Activity implements OnHeadGestureListener
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

        setContentView(R.layout.decision_support);
        loadView();
	}

	private void loadView()
	{
		Patient p = Global.recentPatients.peek();
		
		//Retrieve data field
		TextView DSCurrentStateView = (TextView) findViewById(R.id.DSCurrentStateField);// current state
		TextView DSOptActView = (TextView) findViewById(R.id.DSOptActField);// optimal option
		TextView DSAltActView = (TextView) findViewById(R.id.DSAltActField);// alternative action
		TextView DSNextStateView = (TextView) findViewById(R.id.DSNextStateField);// Next state
		
		//set data
		DSCurrentStateView.setText(p.currentState);
		DSOptActView.setText(p.optimalAction);
		DSAltActView.setText(p.alternativeAction);
		DSNextStateView.setText(p.nextProbableState);
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
                	startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_LEFT) 
                {
                	startActivity( new Intent(getApplicationContext(), EventsActivity.class) );
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

    // headgestures
    @Override
    public void onShakeToRight() {
    	if(Global.options.headGesture)
    	{
    		startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
    	}
    }

    @Override
    public void onShakeToLeft() {
    	if(Global.options.headGesture)
    	{
    		startActivity( new Intent(getApplicationContext(), EventsActivity.class) );
    	}
    }
    
    @Override
    public void onNod(){
    	// do nothing
    }
}
