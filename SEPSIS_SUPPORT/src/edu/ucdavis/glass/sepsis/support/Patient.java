package edu.ucdavis.glass.sepsis.support;

import java.util.ArrayList;

public class Patient implements java.io.Serializable
{
	    private String id;
	    private String name;
	    private ArrayList<Integer> states;
	    private Integer currentState;
	    private Integer viewingState;
//	    picture?
//	    first loaded?

	    public Patient(String i, String n, ArrayList<Integer> ss) 
	    {
	    	id = i;
	    	name = n;
	    	states = ss;
	    	currentState = states.get(0);

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
	    
	    public ArrayList<Integer> getStates() 
	    {
	    	return states;
	    }
	    
	    public Integer getCurrentState() 
	    {
	    	return currentState;
	    }
	    
	    public Integer getViewingState() 
	    {
	    	return viewingState;
	    }
	    
	    public void setViewingState( int v ) 
	    {
	    	viewingState = v;
	    }
}