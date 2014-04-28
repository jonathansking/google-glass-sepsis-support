package edu.ucdavis.glass.sepsis.support;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

public class Options extends Activity {
	
	private final String optionsFile = "options.sav";
	private GestureDetector mGestureDetector;
	
	//OPTIONS
	public int screentimeout;
	public int numberofrecentpatients;

	@SuppressWarnings("unchecked")
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        FileInputStream fis;
        try 
        {
            fis = openFileInput(optionsFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Global.options = (Options) ois.readObject();
            ois.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        setContentView(R.layout.welcome_screen);
        mGestureDetector = createGestureDetector(this);
    }
	
	@Override
    protected void onDestroy() 
	{
        super.onDestroy();
        try 
        {
        	FileOutputStream fos = openFileOutput(optionsFile, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Global.options);
            oos.close();
        } 
        catch (Exception e) 
        {
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
                	
                    return true;
                } 
                else if (gesture == Gesture.TWO_TAP) 
                {
                	return true;
                } 
                else if (gesture == Gesture.SWIPE_RIGHT) 
                {
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_LEFT) 
                {
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
