package edu.ucdavis.glass.sepsis.support;

import java.util.ArrayList;

import com.google.android.glass.app.Card;

public class global {
		public final static int maxRecentPatients = 5; 
	    private static ArrayList<Patient> recentPatients = new ArrayList<Patient>();
	    
	    // adds patient to Queue, maintaining a max
	    public static void pushRecentPatient(String id, String name) {
	    	if( recentPatients.size() == maxRecentPatients ) {
	    		recentPatients.remove(maxRecentPatients-1);
	    	}
	    	recentPatients.add(new Patient(id, name));
	    }
	    
	    public static int getRecentPatientSize() {
	    	return recentPatients.size();
	    }
	    
	    public static ArrayList<Patient> getPatients() {
	    	return (ArrayList<Patient>) recentPatients.clone();
	    }
	    
	    public static Patient getPatientAtPosition( int index ) {
	    	return recentPatients.get(index);
	    }
}