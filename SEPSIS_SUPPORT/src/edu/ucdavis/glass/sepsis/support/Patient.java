package edu.ucdavis.glass.sepsis.support;

public class Patient implements java.io.Serializable
{
	    private String id;
	    private String name;
	    private ArrayList<String> states;
	    private String currentState;
	    private String viewingState;
//	    picture?
//	    first loaded?

	    public Patient(String i, String n, ArrayList<String> ss) 
	    {
	    	id = i;
	    	name = n;
	    	states = cs;
	    	currentState = ss.get(0);

	    	// by default so current state
	    	viewingState = currentState;
	    }
	    
	    public String toString() 
	    {
	    	return name + ", #" + id + ": " + currentState;
	    }
	    
	    public Patient(Patient p) 
	    {
	    	id = p.id;
	    	name = p.name;
	    }
	    
	    @Override
	    public boolean equals(Object o)
	    {
	    	return ( this.id.equals( ((Patient)o).id ) && this.name.equals( ((Patient)o).name ) );
	    }
	    
	    public String getName() 
	    {
	    	return name;
	    }
	    
	    public String getId() 
	    {
	    	return id;
	    }
	    
	    public String getCurrentState() 
	    {
	    	return currentState;
	    }
	    
	    public String getViewingState() 
	    {
	    	return viewingState;
	    }
}