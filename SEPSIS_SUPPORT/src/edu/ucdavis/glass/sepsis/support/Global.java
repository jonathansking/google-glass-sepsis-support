package edu.ucdavis.glass.sepsis.support;

import java.util.ArrayDeque;

import org.json.JSONObject;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    
    // error toast
    public static void toastUser( Context context, String message ) 
    {
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    	
    	View layout = new View(context.getApplicationContext());
    	layout = inflater.inflate(R.layout.toast, (ViewGroup) layout.findViewById(R.id.toast_layout_root));

    	TextView toastMessage = (TextView) layout.findViewById(R.id.message);
    	toastMessage.setText( message );

    	Toast toast = new Toast(context.getApplicationContext());
    	toast.setGravity(Gravity.FILL, 0, 0);
    	toast.setDuration(Toast.LENGTH_LONG);
    	toast.setView(layout);
    	toast.show();
    }
}
