package edu.ucdavis.glass.sepsis.support;

import java.util.ArrayDeque;

import org.json.JSONObject;

public class Global 
{
	// Constants
	public static final String PATIENT_ID = "Patient info";
	public static final String OPTIONS_FILE = "options.sav";
	public static final String PATIENTS_FILE = "patients_file.sav";
	
	// recent patient queue
    public static ArrayDeque<Patient> recentPatients =  new ArrayDeque<Patient>();
    
    // options
    public static Options options = new Global.Options();
    
	// structure to save options
	@SuppressWarnings("serial")
	public static class Options implements java.io.Serializable
	{
		public int screenTimeout;
		public int numberOfRecentPatients;
		public int eventsDisplay;
		
		public Options() {
			screenTimeout = 30;
			numberOfRecentPatients = 5;
			eventsDisplay = 10;
		}
	}
    
    // adds patient to Queue, maintaining a max, handles duplicates
    public static void pushRecentPatient(String id, JSONObject j) 
    {
    	Patient p = new Patient(id, j);
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
    
    // this is causing window leaks, we need a new solution
//    // error dialog
//    GestureDetector mGestureDetector;
//    public static void alertUser( final Context context, String title, String message ) {
//    	
//		Dialog dialog = new Dialog(context);
//		dialog.setContentView(R.layout.notification);
//		
//		TextView notificationTitle = (TextView) dialog.findViewById(R.id.notification_title);
//		notificationTitle.setText( title );
//		
//		TextView text = (TextView) dialog.findViewById(R.id.notification_message);
//		text.setText( message );
//		
//		dialog.show();
//    }
}
