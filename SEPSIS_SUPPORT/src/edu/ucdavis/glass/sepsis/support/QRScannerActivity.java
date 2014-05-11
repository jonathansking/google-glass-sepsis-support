package edu.ucdavis.glass.sepsis.support;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class QRScannerActivity extends Activity implements AsyncTaskCompleteListener<JSONObject> 
{
	private String scannedID;
	
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
	    		// get result id
	    		scannedID = data.getStringExtra("SCAN_RESULT");
	    		
	    	    // run AsyncTask
	    	    new LoadJSONAsyncTask( this, "Checking for patient in database...", this ).execute( scannedID );
	   
	    	}
	    	else if (resultCode == RESULT_CANCELED) 
	    	{
	    		// error
		    	System.out.println("ERROR_2");
	            System.out.println("unable to read json.");
	        }
    	}
    }

	public void onTaskComplete(JSONObject json) 
	{
		// add patient 
	    try {
	    	
			if ( (json.get("result_status").toString()).equals("success") )
		    {	
				// create patient
			    Global.pushRecentPatient( scannedID, json );

            	// go to overview
            	startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
		    }
		    else 
		    {
		    	// error
		    	System.out.println("ERROR_0");
		    	System.out.println("No patient with that id exists");
		    }
			
	    } catch (Exception e) {
			// error
	    	System.out.println("ERROR_1");
            System.out.println("unable to read json.");
		}
	    finish();
	}
}