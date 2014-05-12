package edu.ucdavis.glass.sepsis.support;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class Patient implements java.io.Serializable
{
		public String id;
		public JSONObject json;
    	
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
			public String state;
			
			public Event( String ts, String e, String a, String s) {
				timeStamp = ts;
				event = e;
				attribute = a;
			}
		}
		
		// Vitals
		public String bacteriaInBlood;
	    public ArrayList<Vital> vitals;
	    
		public static class Vital implements java.io.Serializable
		{
			public String temperature;
			public String respiratoryRate;
			public String WBC;
			public String SBP;
			public String MAP;
			
			public Vital( String t, String rr, String wbc, String sbp, String map) {
				temperature = t;
				respiratoryRate = rr;
				WBC = wbc;
				SBP = sbp;
				MAP = map;
			}
		}

	    public Patient( String i, JSONObject j ) 
	    {
		    try 
		    {
		    	id = i;
		    	json = j;
		    	
				// Overview
		    	JSONObject o = j.getJSONObject("Overview");
				name = o.getString( "name" );
			    dob = o.getString( "dob" );
			    gender = o.getString( "gender" );
			    admissionTimestamp = o.getString( "admission_time_stamp" );
			    currentState = o.getString( "current_state" );
			    
			    // Decision Support
		    	JSONObject d = j.getJSONObject("Decision_Support");
			    optimalAction = d.getString( "optimal_action" );
			    alternativeAction = d.getString( "alternative_action" );
			    nextProbableState = d.getString( "next_probable_state" );
		    
			    // Events
			    events = new ArrayList<Event>();
			    
		    	JSONArray es = j.getJSONArray("Events");
			    for(int k = 0; k < es.length(); k++)
				{
			    	JSONObject e = (JSONObject) es.get(k);
			    	events.add( new Event(
			    			e.getString( "time_stamp" ),
			    			e.getString( "event" ),
			    			e.getString( "attribute" ),
			    			e.getString( "state" )
	    			) );
				}

			    // Vitals
		    	JSONObject vs = j.getJSONObject("Vitals");
		    	bacteriaInBlood = vs.getString( "bacteria_in_blood" );
		    	
		    	JSONArray ovs = vs.getJSONArray("other_vitals");
			    vitals = new ArrayList<Vital>();
			    
			    for(int k = 0; k < ovs.length(); k++)
				{
			    	JSONObject v = (JSONObject) ovs.get(k);
			    	vitals.add( new Vital(
			    			v.getString( "temperature" ),
			    			v.getString( "respiratory_rate" ),
			    			v.getString( "WBC" ),
			    			v.getString( "SBP" ),
			    			v.getString( "MAP" )
	    			) );
				}
			} 
		    catch (JSONException e) 
		    {
				System.out.println( "Reading JSON failed." );
				e.printStackTrace();
			}
	    }
	    
		// make dummy patient for testing purposes
	    public Patient( ) 
	    {
	    	id = "999";
	    	json = new JSONObject();
	    	
			// Overview
			name = "name";
		    dob = "dob";
		    gender = "gender";
		    admissionTimestamp = "admission_timestamp";
		    currentState = "current_state";
		    
		    // Decision Support
		    optimalAction = "optimal_action";
		    alternativeAction = "alternative_action";
		    nextProbableState = "next_probable_state";
	    
		    // Events
		    events = new ArrayList<Event>();
		    
		    for(int k = 0; k < 10; k++)
			{
		    	events.add( new Event(
		    			"time_stamp",
		    			"event",
		    			"attribute",
		    			"state"
    			) );
			}

		    // Vitals
		    vitals = new ArrayList<Vital>();
		    bacteriaInBlood = "bacteria_in_blood";
		    for(int k = 0; k < 10; k++)
			{
		    	vitals.add( new Vital(
		    			"temperature",
		    			"respiratory_rate",
		    			"wbc",
		    			"sbp",
		    			"map"
    			) );
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