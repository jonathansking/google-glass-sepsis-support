/*
 * Copyright (C) 2013 The Android Open Source Project
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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import edu.ucdavis.glass.sepsis.support.Global.Options;

public class WelcomeActivity extends Activity 
{
	private GestureDetector mGestureDetector;
	
	@SuppressWarnings("unchecked")
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        mGestureDetector = createGestureDetector(this);	
        setContentView(R.layout.welcome_screen);
        
        //=============================================================================
        // add dummy patient for now
        // disabled qrscanner for now as well
        Global.recentPatients.add( new Patient() );
        
        // load options
        FileInputStream fis;
        try 
        {
            System.out.println("loading options...");
            fis = openFileInput(Global.OPTIONS_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Global.options = (Options) ois.readObject();
            ois.close();
            System.out.println("loading options complete");
            
            // set screen timeout
    		android.provider.Settings.System.putInt(getContentResolver(),android.provider.Settings.System.SCREEN_OFF_TIMEOUT, Global.options.screenTimeout*1000);
        } 
        catch (Exception e) 
        {
        	// error
            System.out.println("loading options failed");
            e.printStackTrace();
        }
        
        // load recent patients
        try 
        {
            System.out.println("loading patients...");
            fis = openFileInput(Global.PATIENTS_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Global.recentPatients = (ArrayDeque<Patient>) ois.readObject();
            ois.close();
            System.out.println("loading patients complete");
        } 
        catch (Exception e) 
        {
        	// error
            System.out.println("loading patients failed");
            e.printStackTrace();
        }
    }
	
	@Override
    protected void onDestroy() 
	{
        super.onDestroy();
        
        // write out recent patients
        try 
        {
            System.out.println("saving patients...");
        	FileOutputStream fos = openFileOutput(Global.PATIENTS_FILE, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Global.recentPatients);
            oos.close();
            System.out.println("saving patients completed");
        } 
        catch (Exception e) 
        {
        	// error        	
            System.out.println("saving patients failed");
            e.printStackTrace();
        }
        
        // write out options
        try 
        {
            System.out.println("saving options...");
        	FileOutputStream fos = openFileOutput(Global.OPTIONS_FILE, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Global.options);
            oos.close();
            System.out.println("saving options completed");
        } 
        catch (Exception e) 
        {
        	// error
            System.out.println("saving options failed");
            e.printStackTrace();
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
//                	startActivity( new Intent(getApplicationContext(), QRScannerActivity.class) );
                	startActivity( new Intent(getApplicationContext(), VitalsActivity.class) );
                    return true;
                } 
                else if (gesture == Gesture.TWO_TAP) 
                {
                	return true;
                } 
                else if (gesture == Gesture.SWIPE_RIGHT) 
                {
                	// go to recent patient view
                	startActivity( new Intent(getApplicationContext(), RecentPatientActivity.class) );
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_LEFT) 
                {
                	// go to options view
                    startActivity( new Intent(getApplicationContext(), OptionsActivity.class));
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
}
