package edu.ucdavis.glass.sepsis.support;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class LoadJSONAsyncTask extends AsyncTask<String,Void,String> 
{
	private JSONObject json;
	private String result_status;
	
	private ProgressDialog pDialog;
	private String pDialog_text;
	private Context pDialog_context;
	
	public LoadJSONAsyncTask( Context context, JSONObject j, String progressText ) 
	{
		this.json = j;
		this.pDialog_text = progressText;
		this.pDialog_context = context;
	}
	
	protected void onPreExecute()
	{
		super.onPreExecute();
		pDialog = new ProgressDialog(pDialog_context);
		pDialog.setMessage(pDialog_text);
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
		    	this.json = json;
		    }
		    else 
		    {
		    	System.out.println("No patient with that id exists");
		    	Global.alertUser(pDialog_context, "Exception", "No patient with that id exists");
		    }
		    
		}
		catch(Exception e) 
		{
			// error
            System.out.println("unable to read json.");
            Global.alertUser(pDialog_context, "Exception", "Unable to read json.");
		}
	}
}