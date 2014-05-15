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
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.ScrollView;
import android.widget.TableRow.LayoutParams;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
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

public class EventsActivity extends Activity implements OnHeadGestureListener
{
	private GestureDetector mGestureDetector;
	private HeadGestureDetector mHeadGestureDetector;
	private ListView mListView;
	private ScrollView eventsScrollView;
    private VoiceInputHelper mVoiceInputHelper;
    private VoiceConfig mVoiceConfig;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        mGestureDetector = createGestureDetector(this);
        mHeadGestureDetector = new HeadGestureDetector(this);
        mHeadGestureDetector.setOnHeadGestureListener(this);
        mHeadGestureDetector.start();
        
        //set up voice command
        String[] items = {"Vitals", "Decision Support", "Overview"};
        mVoiceConfig = new VoiceConfig("MyVoiceConfig", items);
        mVoiceInputHelper = new VoiceInputHelper(this, new MyVoiceListener(mVoiceConfig),
                VoiceInputHelper.newUserActivityObserver(this));

        /* set view for events */
        setContentView(R.layout.events);
        loadView();
        eventsScrollView = (ScrollView) findViewById(R.id.eventsScrollView);
	}
	
	private void loadView()
	{
        Patient p = Global.recentPatients.peek();
		TableLayout eventsTableLO = (TableLayout) findViewById(R.id.eventsTableLayout);
        
        //create rows
        for (int i = 0; i < Global.options.eventsDisplay; i++) //MAX_EVENT is the maximum events will be display, not yet created
        {
        	//Create Table row
        	TableRow eventRow = new TableRow(this);
        	eventRow.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        	eventRow.setGravity(Gravity.CENTER);
        	
        	// Create Text View for events
        	TextView rank = new TextView(this);// index
        	TextView time = new TextView(this);// time stamp
        	TextView event = new TextView(this);// event
        	TextView attr = new TextView(this);// attribute
        	TextView state = new TextView(this);// state
        	
        	//set style
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
        	
        	//set data
        	rank.setText(String.valueOf(i+1));// index
        	time.setText(p.events.get(i).timeStamp);// time stamp
        	event.setText(p.events.get(i).event);// event
        	attr.setText(p.events.get(i).attribute);// attribute
        	state.setText(p.events.get(i).state);// state
        	
        	//add to table row
        	eventRow.addView(rank);
        	eventRow.addView(time);
        	eventRow.addView(event);
        	eventRow.addView(attr);
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
                else if (gesture == Gesture.LONG_PRESS) 
                {	
                	//mListView.setScrollY( mListView.getScrollY()+100 );
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
    
    @Override
    protected void onResume() {
    	super.onResume();
    	mHeadGestureDetector.start();
        mVoiceInputHelper.addVoiceServiceListener();
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
    	// Do something
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
    
        }
    
        @Override
        public VoiceConfig onVoiceCommand(VoiceCommand vc) {
            String recognizedStr = vc.getLiteral();
            Log.i("VoiceMenu", "Recognized text: "+recognizedStr);
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
	        
	        else if (recognizedStr.equals("Overview"))
	        {
	        	finish();
	        	if (flag == 0)
            	{
            		startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
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
    }
}
