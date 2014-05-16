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
import android.util.Log;
import android.view.MotionEvent;
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


public class SupportActivity extends Activity implements OnHeadGestureListener
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

        /* set view for support */
        setContentView(R.layout.decision_support);
        loadView();
        /* Set view done*/
	}

	private void loadView()
	{
		Patient p = Global.recentPatients.peek();
		/* set view for overview */
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
                	startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_LEFT) 
                {
                	finish();
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
    		startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
    	}
    }

    @Override
    public void onShakeToLeft() {
    	if(Global.options.headGesture)
    	{
    		finish();
    		startActivity( new Intent(getApplicationContext(), EventsActivity.class) );
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
            int flag = 0;
            if (recognizedStr.equals("Vitals"))
	        {
            	finish();
            	if (flag == 0)
            	{
            		startActivity( new Intent(getApplicationContext(), VitalsActivity.class) );
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
	        
	        else if (recognizedStr.equals("Overview"))
	        {
	        	finish();
	        	if (flag == 0)
            	{
            		startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
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
