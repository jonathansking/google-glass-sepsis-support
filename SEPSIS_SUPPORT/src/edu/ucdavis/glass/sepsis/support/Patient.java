package edu.ucdavis.glass.sepsis.support;

public class Patient 
{
	    private String id;
	    private String name;
//	    picture?
//	    first loaded?

	    public Patient(String i, String n) 
	    {
	    	id = i;
	    	name = n;
	    }
	    
	    public String toString() 
	    {
	    	return name + ": " + id;
	    }
	    
	    public Patient(Patient p) 
	    {
	    	id = p.id;
	    	name = p.name;
	    }
	    
	    public String getName() 
	    {
	    	return name;
	    }
	    
	    public String getId() 
	    {
	    	return id;
	    }
}