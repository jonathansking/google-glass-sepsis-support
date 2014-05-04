package edu.ucdavis.glass.sepsis.support;

import java.util.ArrayDeque;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.widget.TextView;

public class Global 
{
	// Constants
	public static final String PATIENT_ID = "Patient info";
	public static final String OPTIONS_FILE = "options.sav";
	public static final String PATIENTS_FILE = "patients_file.sav";
	
	// recent patient queue
	public static int maxRecentPatients = 5; 
    public static ArrayDeque<Patient> recentPatients;
    
    // options
    public static Options options;
    
	// structure to save options
	public static class Options 
	{
		public Integer screenTimeout;
		public Integer numberOfRecentPatients;
		
		public Options() {
			screenTimeout = 30;
			numberOfRecentPatients = 5;
		}
	}
    
    // adds patient to Queue, maintaining a max, handles duplicates
    public static void pushRecentPatient(String id, String name) 
    {
    	Patient p = new Patient(id, name);
    	recentPatients.remove( p );
    	
    	if( recentPatients.size() >= maxRecentPatients )
    		recentPatients.removeLast();

		recentPatients.addFirst( p );
    }
    
    // debugging purposes
    public static void printPatients() 
    {
    	for( Patient p : recentPatients )
    		System.out.println( p );
    }
    
    // error dialog
    GestureDetector mGestureDetector;
    public static void alertUser( Context context, String title, String message ) {
    	
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.notification);
 
//		// set the custom dialog components - text, image and button
//		dialog.setTitle( title );
		
		TextView notificationTitle = (TextView) dialog.findViewById(R.id.notification_title);
		notificationTitle.setText( title );
		
		TextView text = (TextView) dialog.findViewById(R.id.notification_message);
		text.setText( message );
		
//		// this won't work since we don't have buttons
//		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
//		// if button is clicked, close the custom dialog
//		dialogButton.setOnClickListener(new OnClickListener() 
//		{
//			@Override
//			public void onClick(View v) 
//			{
//				dialog.dismiss();
//			}
//		});
		
		GestureDetector gestureDetector = new GestureDetector(context);
		
        // create a base listener for generic gestures
        gestureDetector.setBaseListener( new GestureDetector.BaseListener() 
        {
            @Override
            public boolean onGesture(Gesture gesture) 
            {
                if (gesture == Gesture.TAP) 
                {
                	dialog.dismiss();
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
 
		dialog.show();
    }
    
    public boolean onGenericMotionEvent(MotionEvent event) 
    {
        if (mGestureDetector != null)
            return mGestureDetector.onMotionEvent(event);
        
        return false;
    }
}
