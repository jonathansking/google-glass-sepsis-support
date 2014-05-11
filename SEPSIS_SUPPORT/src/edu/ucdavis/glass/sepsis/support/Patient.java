package edu.ucdavis.glass.sepsis.support;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class Patient implements java.io.Serializable
{
		// ID
    	public String id;
    	
		// Overview
	    public String name;
	    public String dob;
	    public String gender;
	    public String admissionTimestamp;
	    public String currentState;
	    
	    // Decision Support
	    public String optimalAction;
	    public String alternativeAction;
	    public String nextProbableState;
	    
	    // Events
	    public ArrayList<Event> events;
	    
		public static class Event implements java.io.Serializable
		{
			public String timeStamp;
			public String event;
			public String attribute;
			
			public Event( String ts, String e, String a) {
				timeStamp = ts;
				event = e;
				attribute = a;
			}
		}
		
		// Vitals
	    public ArrayList<Vital> vitals;
	    
		public static class Vital implements java.io.Serializable
		{
			public String bacteriaInBlood;
			public String temperature;
			public String respiratoryRate;
			public String WBC;
			public String SBP;
			public String MAP;
			public String state;
			
			public Vital( String bib, String t, String rr, String wbc, String sbp, String map, String s) {
				bacteriaInBlood= bib;
				temperature = t;
				respiratoryRate = rr;
				WBC = wbc;
				SBP = sbp;
				MAP = map;
				state = s;
			}
		}

	    public Patient( String i, JSONObject j ) 
	    {
		    try 
		    {
		    	id = i;
		    	
				// Overview
				name = j.getString( "name" );
			    dob = j.getString( "dob" );
			    gender = j.getString( "gender" );
			    admissionTimestamp = j.getString( "admission_timestamp" );
			    currentState = j.getString( "current_state" );
			    
			    // Decision Support
			    optimalAction = j.getString( "optimal_action" );
			    alternativeAction = j.getString( "alternative_action" );
			    nextProbableState = j.getString( "next_probable_state" );
		    
			    // Events
			    events = new ArrayList<Event>();
			    
		    	JSONArray es = j.getJSONArray("events");
			    for(int k = 0; k < es.length(); k++)
				{
			    	JSONObject e = (JSONObject) es.get(k);
			    	events.add( new Event(
			    			e.getString( "time_stamp" ),
			    			e.getString( "event" ),
			    			e.getString( "attribute" )
	    			) );
				}

			    // Vitals
			    vitals = new ArrayList<Vital>();
			    
		    	JSONArray vs = j.getJSONArray("events");
			    for(int k = 0; k < vs.length(); k++)
				{
			    	JSONObject v = (JSONObject) vs.get(k);
			    	vitals.add( new Vital(
			    			v.getString( "bacteria_in_blood" ),
			    			v.getString( "temperature" ),
			    			v.getString( "respiratory_rate" ),
			    			v.getString( "wbc" ),
			    			v.getString( "sbp" ),
			    			v.getString( "map" ),
			    			v.getString( "state" )
	    			) );
				}
			} 
		    catch (JSONException e) 
		    {
				System.out.println( "Reading JSON failed." );
				e.printStackTrace();
			}
	    }
	    
	    public String toString() 
	    {
	    	return name + ", #" + id;
	    }
	    
	    @Override
	    public boolean equals(Object o)
	    {
	    	return ( this.id.equals( ((Patient)o).id ) && this.name.equals( ((Patient)o).name ) );
	    }
}