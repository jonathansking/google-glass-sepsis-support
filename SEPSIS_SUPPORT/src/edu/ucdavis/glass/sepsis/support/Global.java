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
	
	interface AsyncTaskCompleteListener<T> 
	{
		   public void onTaskComplete(T result);
	}
	
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
    public static void pushRecentPatient(String id, String name, String currentState) 
    {
    	Patient p = new Patient(id, name, currentState);
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
    public static void alertUser( final Context context, String title, String message ) {
    	
		Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.notification);
		
		TextView notificationTitle = (TextView) dialog.findViewById(R.id.notification_title);
		notificationTitle.setText( title );
		
		TextView text = (TextView) dialog.findViewById(R.id.notification_message);
		text.setText( message );
		
		dialog.show();
    }
}
