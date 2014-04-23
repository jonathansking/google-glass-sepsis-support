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
import java.util.ArrayList;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class WelcomeActivity extends Activity {
	
	private String recentPatientID = "recentPatientID";
	private String recentPatientName = "recentPatientName";
	private GestureDetector mGestureDetector;
	@SuppressWarnings("unchecked")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FileInputStream fis;
        try {
            fis = openFileInput(recentPatientID);
            ObjectInputStream ois = new ObjectInputStream(fis);
            global.id = (ArrayList<String>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            fis = openFileInput(recentPatientName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            global.name = (ArrayList<String>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
//        for(int i = 0; i < 4; i++)
//        {
//        	global.id.add(String.valueOf(i));
//        	global.id.add(String.valueOf(i));
//        }
        setContentView(R.layout.welcome_screen);
        mGestureDetector = createGestureDetector(this);
    }
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
        try {
        	FileOutputStream fos = openFileOutput(recentPatientID,MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(global.id);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
        	FileOutputStream fos = openFileOutput(recentPatientName,MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(global.name);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	private GestureDetector createGestureDetector(Context context) {
    GestureDetector gestureDetector = new GestureDetector(context);
        // create a base listener for generic gestures
        gestureDetector.setBaseListener( new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                if (gesture == Gesture.TAP) {
                	startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
                    return true;
                } else if (gesture == Gesture.TWO_TAP) {
                    return true;
                } else if (gesture == Gesture.SWIPE_RIGHT) {
                	startActivity( new Intent(getApplicationContext(), RecentPatientActivity.class) );
                    return true;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                	startActivity( new Intent(getApplicationContext(), RecentPatientActivity.class) );
                    return true;
                }
                return false;
            }
        });
        
        return gestureDetector;
    }

    // send generic motion events to the gesture detector
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            return mGestureDetector.onMotionEvent(event);
        }
        return false;
    }
}
