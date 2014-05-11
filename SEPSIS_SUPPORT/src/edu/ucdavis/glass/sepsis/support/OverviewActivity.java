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

import android.content.Intent;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ListView;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.polysfactory.headgesturedetector.*;


public class OverviewActivity extends Activity implements OnHeadGestureListener, AsyncTaskCompleteListener<JSONObject>
{
	private GestureDetector mGestureDetector;
	private HeadGestureDetector mHeadGestureDetector;
	private ListView mListView;

	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		System.out.println("OnCreate Overview");
		
		/* set view for overview */
        setContentView(R.layout.overview);
		
        mGestureDetector = createGestureDetector(this);
        mHeadGestureDetector = new HeadGestureDetector(this);
        mHeadGestureDetector.setOnHeadGestureListener(this);
        mHeadGestureDetector.start();
        
        
        /* Set view done*/
	}
	
	public void onTaskComplete(JSONObject json) 
	{
    	// reload overview
    	startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
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
//                    // set up AsyncTask
//                    AsyncTask<String, Void, JSONObject> JSON = new LoadJSONAsyncTask( this, "Updating all patient's info...", this );
//            	    
//            	    // run AsyncTask
//              	  	JSON.execute( "overview" );
              	  	
                    return true;
                } 
                else if (gesture == Gesture.TWO_TAP) 
                {
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_RIGHT) 
                {
                	startActivity( new Intent(getApplicationContext(), VitalsActivity.class) );
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_LEFT) 
                {
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

    // headgestures
    @Override
    public void onShakeToRight() {
    	startActivity( new Intent(getApplicationContext(), VitalsActivity.class) );
    }

    @Override
    public void onShakeToLeft() {
    	startActivity( new Intent(getApplicationContext(), SupportActivity.class) );
    }
    
    @Override
    public void onNod(){
    	// Do something
    }
}
