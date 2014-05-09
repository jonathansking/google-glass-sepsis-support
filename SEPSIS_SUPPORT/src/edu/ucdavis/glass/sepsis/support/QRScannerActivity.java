package edu.ucdavis.glass.sepsis.support;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class QRScannerActivity extends Activity implements Global.AsyncTaskCompleteListener<JSONObject> 
{
	protected void onCreate(Bundle savedInstanceState)
	{ 
	    super.onCreate(savedInstanceState);
	    
	    // start zxing scanner
	    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
	    startActivityForResult(intent, 0); 
	}
	
	// when zxing scanner returns
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	if (requestCode == 0)
    	{
	    	if (resultCode == RESULT_OK)
	    	{	
	    	    // push dummy patient to run AsycTask
	    		ArrayList<Integer> l = new ArrayList<Integer>();
	    		l.add(-1);
	    	    Global.pushRecentPatient( data.getStringExtra("SCAN_RESULT"), "dummy", l );

	    	    // run AsyncTask
	    	    new LoadJSONAsyncTask( this, "Checking for patient in database...", this ).execute( "patient" );
	   
	    	}
	    	else if (resultCode == RESULT_CANCELED) 
	    	{
	    		// error
		    	System.out.println("ERROR_2");
	            System.out.println("unable to read json.");
//	            Global.alertUser(this, "Notification", "QR scan canceled.");
	        }
    	}
    }

	public void onTaskComplete(JSONObject json) 
	{
		// add patient 
	    try {
    	    
    	    // pop dummy
    	    Global.recentPatients.pop();
			System.out.println(json.get("result_status").toString());
			if ( (json.get("result_status").toString()).equals("success") )
		    {	
		    	// get states from json
		    	JSONArray jsonStates = json.getJSONArray("state_ids");
		    	ArrayList<Integer> patientStates = new ArrayList<Integer>();

		    	for(int i = 0; i < jsonStates.length(); i++)
				{
				    patientStates.add( Integer.parseInt(jsonStates.get(i).toString()) );
				}

				// create patient
			    Global.pushRecentPatient( json.getString("patient_id"), json.getString("name"), patientStates );

            	// go to overview
            	startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
		    }
		    else 
		    {
		    	// error
		    	System.out.println("ERROR_0");
		    	System.out.println("No patient with that id exists");
//		    	Global.alertUser(this, "Notification", "No patient with that id exists");
		    }
			
	    } catch (Exception e) {
			// error
	    	System.out.println("ERROR_1");
            System.out.println("unable to read json.");
//            Global.alertUser(this, "Exception", "Unable to read JSON.");
		}
	    finish();
	}
}