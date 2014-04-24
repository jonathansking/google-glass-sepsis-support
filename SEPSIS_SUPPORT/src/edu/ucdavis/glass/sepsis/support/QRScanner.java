package edu.ucdavis.glass.sepsis.support;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class QRScanner extends Activity {
	
	public final static String PATIENT_ID = "Patient info";
	
	protected void onCreate(Bundle savedInstanceState){ 
	    super.onCreate(savedInstanceState);
	    
	    try{ 
		    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		    startActivityForResult(intent, 0); 
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// when a QR code is read, it will send a result code 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == 0){
	    	if (resultCode == RESULT_OK){
	
				// pass QR message to the overview activity
				Intent overviewIntent = new Intent(getApplicationContext(), OverviewActivity.class);
				overviewIntent.putExtra(PATIENT_ID, data.getStringExtra("SCAN_RESULT") ); 
				startActivity( overviewIntent );
	            
	    	}
	    	else if (resultCode == RESULT_CANCELED) {
//	            Handle?
	        }
    	}
    	super.onActivityResult(requestCode, resultCode, data);
    }
}