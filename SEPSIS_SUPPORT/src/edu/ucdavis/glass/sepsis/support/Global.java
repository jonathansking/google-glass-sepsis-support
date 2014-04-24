package edu.ucdavis.glass.sepsis.support;

import java.util.ArrayList;

public class Global {
	
	public static final int maxRecentPatients = 5; 
    public static ArrayList<Patient> recentPatients = new ArrayList<Patient>();
    
    // adds patient to Queue, maintaining a max
    public static void pushRecentPatient(String id, String name) {
    	if( recentPatients.size() == maxRecentPatients ) {
    		recentPatients.remove(maxRecentPatients-1);
    	}
    	recentPatients.add(new Patient(id, name));
    }
    
}
