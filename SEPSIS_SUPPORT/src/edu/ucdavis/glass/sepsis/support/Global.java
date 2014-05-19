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
    
    // Voice commands
    public static int overviewCreated = 0;
    public static int overview = 0;
    
	// structure to save options
	@SuppressWarnings("serial")
	public static class Options implements java.io.Serializable
	{
		public int screenTimeout;
		public int numberOfRecentPatients;
		public int eventsDisplay;
		public boolean headGesture;
		public Options() {
			screenTimeout = 60;
			numberOfRecentPatients = 3;
			eventsDisplay = 10;
			headGesture = false;
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
