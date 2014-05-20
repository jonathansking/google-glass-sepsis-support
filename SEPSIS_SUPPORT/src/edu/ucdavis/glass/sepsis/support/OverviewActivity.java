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
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.polysfactory.headgesturedetector.*;
import com.google.glass.input.VoiceInputHelper;
import com.google.glass.input.VoiceListener;
import com.google.glass.logging.FormattingLogger;
import com.google.glass.logging.FormattingLoggers;
import com.google.glass.voice.VoiceCommand;
import com.google.glass.voice.VoiceConfig;


public class OverviewActivity extends Activity implements OnHeadGestureListener, AsyncTaskCompleteListener<JSONObject>
{
	private GestureDetector mGestureDetector;
	private HeadGestureDetector mHeadGestureDetector;
	public static VoiceInputHelper mVoiceInputHelper;
    private VoiceConfig mVoiceConfig;
    private AudioManager mAudioManager;
    private AsyncTaskCompleteListener<JSONObject> mContext;

	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		Global.overview = 1;
		
		mContext = this;
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		
        mGestureDetector = createGestureDetector(this);
        mHeadGestureDetector = new HeadGestureDetector(this);
        mHeadGestureDetector.setOnHeadGestureListener(this);
        mHeadGestureDetector.start();
        
        // set up voice command
        if (Global.overviewCreated == 0)
        {
	        String[] items = {"Vitals", "Decision Support", "Events", "Overview"};
	        mVoiceConfig = new VoiceConfig("MyVoiceConfig", items);
	        mVoiceInputHelper = new VoiceInputHelper(this, new MyVoiceListener(mVoiceConfig),
	                VoiceInputHelper.newUserActivityObserver(this));
	        mVoiceInputHelper.addVoiceServiceListener();
	        Global.overviewCreated = 1;
        }
        
        setContentView(R.layout.overview);
		loadView();
	}
	
	private void loadView()
	{
		Patient p = Global.recentPatients.peek();
		
		// retrieve data fields
		TextView pNameView = (TextView) findViewById(R.id.patientName);               
		TextView pIdView = (TextView) findViewById(R.id.patientId);                   
		TextView pDOBView = (TextView) findViewById(R.id.overviewDOBField);           
		TextView pGenderView = (TextView) findViewById(R.id.overviewGenderField);     
		TextView pHospAdmView = (TextView) findViewById(R.id.overviewHospAdmField);   
		TextView pStateView = (TextView) findViewById(R.id.overviewCurentStateField);
		TextView sumRRView = (TextView) findViewById(R.id.summaryRRField);           
		TextView sumMAPView = (TextView) findViewById(R.id.summaryMAPField);          
		TextView sumSBPView = (TextView) findViewById(R.id.summarySBPField);          
		TextView sumWBCView = (TextView) findViewById(R.id.summaryWBCField);         
		
		// set data
		pNameView.setText(p.name);
    	pIdView.setText(p.id);
    	pDOBView.setText(p.dob);
    	pGenderView.setText(p.gender);
    	pHospAdmView.setText(p.admissionTimestamp);
    	pStateView.setText(p.currentState);
    	sumRRView.setText(p.vitals.get(p.vitals.size()-1).respiratoryRate);
    	sumMAPView.setText(p.vitals.get(p.vitals.size()-1).MAP);
    	sumSBPView.setText(p.vitals.get(p.vitals.size()-1).SBP);
    	sumWBCView.setText(p.vitals.get(p.vitals.size()-1).WBC);
    	
	}
	
	public void onTaskComplete(JSONObject json) 
	{
		// reload view 
	    try 
	    {	
			if ( (json.get("result_status").toString()).equals("success") )
		    {	
				// create patient
			    Global.pushRecentPatient( Global.recentPatients.peek().id, json );

            	// go to overview
            	startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
		    }
			else
			{
	            System.out.println("No internet access");
	        	Global.toastUser(this, "No internet access");
			}
			
	    } catch (Exception e) {
			// error
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
                    // set up AsyncTask
                    AsyncTask<String, Void, JSONObject> JSON = new LoadJSONAsyncTask( (Context) mContext, "Updating all patient's info...", mContext );
            	    
                    // run AsyncTask
              	  	JSON.execute( "overview" );
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_RIGHT) 
                {
                	finish();
                	startActivity( new Intent(getApplicationContext(), VitalsActivity.class) );
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_LEFT) 
                {
                	finish();
                	startActivity( new Intent(getApplicationContext(), SupportActivity.class) );
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
    		finish();
    		startActivity( new Intent(getApplicationContext(), VitalsActivity.class) );
    	}
    }

    @Override
    public void onShakeToLeft() {
    	if(Global.options.headGesture)
    	{
    		finish();
    		startActivity( new Intent(getApplicationContext(), SupportActivity.class) );
    	}
    }
    
    @Override
    public void onNod(){
    	// do nothing
    }
    
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
        	System.out.println("onVoiceServiceDisconnnect called");
        }
    
        @Override
        public VoiceConfig onVoiceCommand(VoiceCommand vc) {
        	String recognizedStr = vc.getLiteral();
            Log.i("VoiceMenu", "Recognized text: "+recognizedStr);
            if (recognizedStr.equals("Vitals"))
	        {
            	finish();
            	startActivity( new Intent(getApplicationContext(), VitalsActivity.class) );
	        }
	        else if (recognizedStr.equals("Decision Support"))
	        {
	        	finish();
	            mAudioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);
	        	startActivity( new Intent(getApplicationContext(), SupportActivity.class) );
	        }
	        else if (recognizedStr.equals("Events"))
	        {
	        	finish();
	        	startActivity( new Intent(getApplicationContext(), EventsActivity.class) );
	        }
	        else if (recognizedStr.equals("Overview"))
	        {
	        	finish();
	        	startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
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
    }
}
