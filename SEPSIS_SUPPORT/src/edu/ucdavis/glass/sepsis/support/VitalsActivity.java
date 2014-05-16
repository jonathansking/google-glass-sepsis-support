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
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.polysfactory.headgesturedetector.*;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.polysfactory.headgesturedetector.*;
import com.google.glass.input.VoiceInputHelper;
import com.google.glass.input.VoiceListener;
import com.google.glass.logging.FormattingLogger;
import com.google.glass.logging.FormattingLoggers;
import com.google.glass.voice.VoiceCommand;
import com.google.glass.voice.VoiceConfig;


public class VitalsActivity extends Activity implements OnHeadGestureListener
{
	private GestureDetector mGestureDetector;
	private HeadGestureDetector mHeadGestureDetector;
	private ListView mListView;
	private GraphicalView mChart; //graphs
    private VoiceInputHelper mVoiceInputHelper;
    private VoiceConfig mVoiceConfig;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		System.out.println("OnCreate Vitals");
		
        mGestureDetector = createGestureDetector(this);
        mHeadGestureDetector = new HeadGestureDetector(this);
        mHeadGestureDetector.setOnHeadGestureListener(this);
        mHeadGestureDetector.start();
        
//        //set up voice command
//        String[] items = {"Overview", "Decision Support", "Events"};
//        mVoiceConfig = new VoiceConfig("MyVoiceConfig", items);
//        mVoiceInputHelper = new VoiceInputHelper(this, new MyVoiceListener(mVoiceConfig),
//                VoiceInputHelper.newUserActivityObserver(this));

        setContentView(R.layout.vitals);
        loadView();
	}
	
	private void loadView()
	{
		Patient p = Global.recentPatients.peek();
		/* set up graph */
        FrameLayout layout = (FrameLayout) findViewById(R.id.vitalsLeftLayout);
    	VitalLineGraph vitalGraph = new VitalLineGraph(); 
    	mChart = vitalGraph.getGraph(this);
        layout.addView(mChart);
        /****************/
        
        /* set data */
        //retrieve data field
        TextView BACInBloodView = (TextView) findViewById(R.id.bibField);// bacteria in blood 
		TextView tempView = (TextView) findViewById(R.id.tempField);// temperature
		TextView RRView = (TextView) findViewById(R.id.RRField);// respiratory rate
		TextView WBCView = (TextView) findViewById(R.id.WBCField);// WBC
		TextView MAPView = (TextView) findViewById(R.id.MAPField);// MAP
		TextView SBPView = (TextView) findViewById(R.id.SBPField);//SBP
		TextView stateView = (TextView) findViewById(R.id.stateField);// State
		
		//set data
		BACInBloodView.setText(p.bacteriaInBlood);
		tempView.setText(p.vitals.get(p.vitals.size()-1).temperature);
		RRView.setText(p.vitals.get(p.vitals.size()-1).respiratoryRate);
		WBCView.setText(p.vitals.get(p.vitals.size()-1).WBC);
		MAPView.setText(p.vitals.get(p.vitals.size()-1).MAP);
		SBPView.setText(p.vitals.get(p.vitals.size()-1).SBP);
		stateView.setText(p.currentState);
    	/************/
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
                	finish();
                	startActivity( new Intent(getApplicationContext(), EventsActivity.class) );
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_LEFT) 
                {
                	// go to overview
                	finish();
                	startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
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
    	super.onResume();
        mHeadGestureDetector.start();
//        mVoiceInputHelper.addVoiceServiceListener();
    }

    @Override
    protected void onPause() {
        mHeadGestureDetector.stop();
        super.onPause();
    }
    
    // headgestures
    @Override
    public void onShakeToRight() {
    	if(Global.options.headGesture)
    	{
    		finish();
    		startActivity( new Intent(getApplicationContext(), EventsActivity.class) );
    	}
    }

    @Override
    public void onShakeToLeft() {
    	if(Global.options.headGesture)
    	{
    		finish();
    		startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
    	}
    }
    
    @Override
    public void onNod(){
    	// Do something
    }
/*
    public class MyVoiceListener implements VoiceListener {
        protected final VoiceConfig voiceConfig;
    
        public MyVoiceListener(VoiceConfig voiceConfig) {
            this.voiceConfig = voiceConfig;
        }
    
        @Override
        public void onVoiceServiceConnected() {
            mVoiceInputHelper.setVoiceConfig(mVoiceConfig, false);
        }
    
        @Override
        public void onVoiceServiceDisconnected() {
    
        }
    
        @Override
        public VoiceConfig onVoiceCommand(VoiceCommand vc) {
            String recognizedStr = vc.getLiteral();
            Log.i("VoiceMenu", "Recognized text: "+recognizedStr);
            int flag = 0;
            if (recognizedStr.equals("Overview"))
	        {
            	finish();
            	if (flag == 0)
            	{
            		startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
            	}
	        	flag = flag + 1;
	        }
	        
	        else if (recognizedStr.equals("Events"))
	        {
	        	finish();
	        	if (flag == 0)
            	{
            		startActivity( new Intent(getApplicationContext(), EventsActivity.class) );
            	}
	        	flag = flag + 1;
	        }
	        
	        else if (recognizedStr.equals("Decision Support"))
	        {
	        	finish();
	        	if (flag == 0)
            	{
            		startActivity( new Intent(getApplicationContext(), SupportActivity.class) );
            	}
	        	flag = flag + 1;
	        }
            
            return null;
        }
    
        @Override
        public FormattingLogger getLogger() {
            return FormattingLoggers.getContextLogger();
        }
    
        @Override
        public boolean isRunning() {
            return true;
        }
    
        @Override
        public boolean onResampledAudioData(byte[] arg0, int arg1, int arg2) {
            return false;
        }
    
        @Override
        public boolean onVoiceAmplitudeChanged(double arg0) {
            return false;
        }
    
        @Override
        public void onVoiceConfigChanged(VoiceConfig arg0, boolean arg1) {
    
        }
    }*/
}
