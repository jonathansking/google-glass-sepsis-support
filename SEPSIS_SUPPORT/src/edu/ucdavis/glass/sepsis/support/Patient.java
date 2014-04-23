package edu.ucdavis.glass.sepsis.support;

public class Patient {
	    private String id;
	    private String name;
//	    private picture
	    
	    public Patient(String i, String n) {
	    	id = i;
	    	name = n;
	    }
	    
	    public String getName() {
	    	return name;
	    }
	    
	    public String getId() {
	    	return id;
	    }
}