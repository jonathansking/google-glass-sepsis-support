package edu.ucdavis.glass.sepsis.support;

import java.util.ArrayDeque;
import java.util.ArrayList;

import com.google.android.glass.touchpad.GestureDetector;

import android.app.Dialog;
import android.content.Context;
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
    public static ArrayDeque<Patient> recentPatients =  new ArrayDeque<Patient>();
    
    // options
    public static Options options = new Global.Options();
    
	// structure to save options
	public static class Options implements java.io.Serializable
	{
		public int screenTimeout;
		public int numberOfRecentPatients;
		
		public Options() {
			screenTimeout = 30;
			numberOfRecentPatients = 5;
		}
	}
    
    // adds patient to Queue, maintaining a max, handles duplicates
    public static void pushRecentPatient(String id, String name, ArrayList<String> states) 
    {
    	Patient p = new Patient(id, name, states);
    	recentPatients.remove( p );
    	
    	if( recentPatients.size() >= options.numberOfRecentPatients )
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
