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

import android.content.Intent;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;


public class OverviewActivity extends Activity 
{
	private GestureDetector mGestureDetector;
	private TextView patientIdTxtView, patientName,patientSex, patientHospAdm, patientHospDisch;
	private ProgressDialog pDialog;

	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        mGestureDetector = createGestureDetector(this);
        
        // catch patient id number from caller
        Intent recentPatientIntent = getIntent();
        String patient_id = recentPatientIntent.getStringExtra(Global.PATIENT_ID);
        setContentView(R.layout.overview);

        patientIdTxtView = (TextView) findViewById(R.id.patientId);
        patientName = (TextView) findViewById(R.id.patientName);
        patientSex = (TextView) findViewById(R.id.patientSex);
        patientHospAdm = (TextView) findViewById(R.id.patientHospAdm);
        patientHospDisch = (TextView) findViewById(R.id.patientHospDisch);        
        new LoadOverviewData(patientIdTxtView, patientName, patientSex, patientHospDisch, patientHospAdm).execute(patient_id);
	}
	
	private class LoadOverviewData extends AsyncTask<String,Void,String> 
	{
		private TextView patientIdTxtView, patientName, patientSex, patientHospDisch, patientHospAdm;
		public LoadOverviewData(TextView patientIdTxtView, TextView patientName, TextView patientSex, TextView patientHospDisch, TextView patientHospAdm) 
		{
			this.patientIdTxtView = patientIdTxtView;
			this.patientName = patientName;
			this.patientSex = patientSex;	
			this.patientHospDisch = patientHospDisch;
			this.patientHospAdm = patientHospAdm;
		}
		
		protected void onPreExecute()
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(OverviewActivity.this);
			pDialog.setMessage("Loading Patient Overview. Please wait...");
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
				String link = "http://glass.herumla.com/?patient_id=" + patient_id ;
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
			catch(Exception e)
			{
				return new String("Exception: " + e.getMessage());
			}		
		}
		
		protected void onPostExecute(String result) 
		{
			pDialog.dismiss();
			try {
			    JSONObject json = new JSONObject(result);
			    this.patientIdTxtView.setText("#" + (String) json.get("patient_id"));
			    this.patientName.setText((String) json.get("name"));
			    this.patientSex.setText((String) json.get("sex"));
			    this.patientHospAdm.setText((String) json.get("hosp_admission"));
			    this.patientHospDisch.setText((String) json.get("hosp_discharge"));
			    
			    // add to recent patients
			    Global.pushRecentPatient( json.getString("patient_id"), json.getString("name") );
			}
			catch(Exception e) 
			{
				// error
	            System.out.println("unable to read json.");
	            Global.alertUser(OverviewActivity.this, "Exception", "Unable to read json.");
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
                	// go to vitals view
                	startActivity( new Intent(getApplicationContext(), VitalsActivity.class) );
                    return true;
                } 
                else if (gesture == Gesture.SWIPE_LEFT) 
                {
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
