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
import org.achartengine.GraphicalView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.polysfactory.headgesturedetector.*;


public class VitalsActivity extends Activity implements OnHeadGestureListener
{
	private GestureDetector mGestureDetector;
	private HeadGestureDetector mHeadGestureDetector;
	private GraphicalView mChart;
    private AudioManager mAudioManager;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		
        mGestureDetector = createGestureDetector(this);
        mHeadGestureDetector = new HeadGestureDetector(this);
        mHeadGestureDetector.setOnHeadGestureListener(this);
        mHeadGestureDetector.start();

        setContentView(R.layout.vitals);
        loadView();
	}
	
	private void loadView()
	{
		Patient p = Global.recentPatients.peek();
		
		// set up graph
        FrameLayout layout = (FrameLayout) findViewById(R.id.vitalsLeftLayout);
    	VitalLineGraph vitalGraph = new VitalLineGraph(); 
    	mChart = vitalGraph.getGraph(this);
        layout.addView(mChart);
        
        //retrieve data field
        TextView BACInBloodView = (TextView) findViewById(R.id.bibField);    // bacteria in blood 
		TextView tempView = (TextView) findViewById(R.id.tempField);         // temperature
		TextView RRView = (TextView) findViewById(R.id.RRField);             // respiratory rate
		TextView WBCView = (TextView) findViewById(R.id.WBCField);           // WBC
		TextView MAPView = (TextView) findViewById(R.id.MAPField);           // MAP
		TextView SBPView = (TextView) findViewById(R.id.SBPField);           // SBP
		TextView stateView = (TextView) findViewById(R.id.stateField);       // state
		
		//set data
		BACInBloodView.setText(p.bacteriaInBlood);
		tempView.setText(p.vitals.get(p.vitals.size()-1).temperature);
		RRView.setText(p.vitals.get(p.vitals.size()-1).respiratoryRate);
		WBCView.setText(p.vitals.get(p.vitals.size()-1).WBC);
		MAPView.setText(p.vitals.get(p.vitals.size()-1).MAP);
		SBPView.setText(p.vitals.get(p.vitals.size()-1).SBP);
		stateView.setText(p.currentState);
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
    	            mAudioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);
                	startActivity( new Intent(getApplicationContext(), EventsActivity.class) );
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_LEFT) 
                {
    	            mAudioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);
                	startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
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
    		mHeadGestureDetector.stop();
    		startActivity( new Intent(getApplicationContext(), EventsActivity.class) );
    	}
    }

    @Override
    public void onShakeToLeft() {
    	if(Global.options.headGesture)
    	{
    		mHeadGestureDetector.stop();
    		startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
    	}
    }
    
    @Override
    public void onNod(){
    	// do nothing
    }
}
