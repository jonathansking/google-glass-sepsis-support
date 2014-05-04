/*
 * ..Copyright (C) 2013 The Android Open Source Project

 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.ucdavis.glass.sepsis.support;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;


public class VitalsActivity extends Activity 
{
	private GestureDetector mGestureDetector;
	private TextView patientIdTxtView, patientName, patientWBC, patientMAP,
						patientBacteria, patientRespRate, patientSBP, patientTemp, patientState;
	private ProgressDialog pDialog;

	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);

        String patient_id = Global.recentPatients.peek().getId();
        setContentView(R.layout.vitals_layout);

        mGestureDetector = createGestureDetector(this);
        
        patientIdTxtView = (TextView) findViewById(R.id.patientId);
        patientName = (TextView) findViewById(R.id.patientName);
        patientBacteria = (TextView) findViewById(R.id.patientBacteria);
        patientWBC = (TextView) findViewById(R.id.patientWBC);
        patientMAP = (TextView) findViewById(R.id.patientMAP);    
        patientRespRate = (TextView) findViewById(R.id.patientRespRate); 
        patientSBP = (TextView) findViewById(R.id.patientSBP); 
        patientTemp = (TextView) findViewById(R.id.patientTemp);
        patientState = (TextView) findViewById(R.id.patientState);
        new LoadVitalsData(patientIdTxtView, patientName, 
        						patientBacteria, patientWBC, patientMAP, patientRespRate, patientSBP, patientTemp, patientState ).execute(patient_id);
	}
	
	private class LoadVitalsData extends AsyncTask<String,Void,String> 
	{
		private TextView patientIdTxtView, patientName, patientWBC, patientMAP,
		patientBacteria, patientRespRate, patientSBP, patientTemp, patientState;
		
		private String result_status;
		public LoadVitalsData( TextView patientIdTxtView, TextView patientName, TextView patientBacteria,
								TextView patientWBC, TextView patientMAP,TextView patientRespRate,TextView patientSBP,
								TextView patientTemp,TextView patientState ) 
		{
			this.patientIdTxtView = patientIdTxtView;
			this.patientName = patientName;
			this.patientWBC = patientWBC;
			this.patientMAP = patientMAP;
			this.patientBacteria = patientBacteria;
			this.patientRespRate = patientRespRate;
			this.patientSBP = patientSBP;
			this.patientTemp = patientTemp;
			this.patientState = patientState;
		}
		
		protected void onPreExecute()
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(VitalsActivity.this);
			pDialog.setMessage("Loading Patient Vitals. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false); 
			pDialog.show();
		}
		
		@Override
		protected String doInBackground(String...arg0) 
		{
			try
			{
				String patient_id = (String)arg0[0]; 
				String link = "http://glass.herumla.com/?patient_id=" + patient_id + "&dataType=vitals" ;
	            HttpClient client = new DefaultHttpClient();
	            HttpGet request = new HttpGet();
	            request.setURI(new URI(link));
	            HttpResponse response = client.execute(request);
	            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

	            StringBuffer sb = new StringBuffer("");
	            String line="";
	            while ((line = in.readLine()) != null) 
	            {
	            	sb.append(line);
	            	break;
	            }
	            in.close();
	            return sb.toString();
			}
			catch(Exception e){
				return new String("Exception: " + e.getMessage());
			}		
		}
		
		protected void onPostExecute(String result) 
		{
			pDialog.dismiss();
			System.out.println( "OnPostExecute running");
			System.out.println( result );
			try {
			    JSONObject json = new JSONObject(result);
			    this.result_status = (String) json.get("result_status");
			    if (  result_status.equals("success") )
			    {
			    	this.patientIdTxtView.setText("#" + (String) json.get("patient_id"));
				    this.patientName.setText((String) json.get("name"));
				    this.patientWBC.setText((String) json.get("WBC"));
				    this.patientMAP.setText((String) json.get("MAP"));
				    this.patientBacteria.setText((String) json.get("BacteriaPresent"));
				    this.patientRespRate.setText((String) json.get("RespRate"));
				    this.patientSBP.setText((String) json.get("SBP"));
				    this.patientTemp.setText((String) json.get("Temperature"));
				    this.patientState.setText((String) json.get("StateName"));
			    }
			    else 
			    {
			    	System.out.println("No patient with that id exists");
			    	Global.alertUser(VitalsActivity.this, "Exception", "No patient with that id exists");
			    }
			    
			}
			catch(Exception e) 
			{
				// error
	            System.out.println("unable to read json.");
	            Global.alertUser(VitalsActivity.this, "Exception", "Unable to read json.");
			}
		}
	}
	
	private GestureDetector createGestureDetector(Context context) 
	{
		GestureDetector gestureDetector = new GestureDetector(context);
		
        // create a base listener for generic gestures
        gestureDetector.setBaseListener( new GestureDetector.BaseListener() 
        {
            @Override
            public boolean onGesture(Gesture gesture) 
            {
                if (gesture == Gesture.TAP) 
                {
                    return true;
                } 
                else if (gesture == Gesture.TWO_TAP) 
                {
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_RIGHT) 
                {
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_LEFT) 
                {
                	// go back to overview
                	finish();
                    return true;
                }
                return false;
            }
        });
        
        return gestureDetector;
    }

    // send generic motion events to the gesture detector
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) 
    {
        if (mGestureDetector != null)
            return mGestureDetector.onMotionEvent(event);
        
        return false;
    }
    
    
}
