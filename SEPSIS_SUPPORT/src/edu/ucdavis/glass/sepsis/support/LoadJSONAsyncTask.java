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

public class LoadJSONAsyncTask extends AsyncTask<String,Void,JSONObject> 
{
	private ProgressDialog pDialog;
	private String pDialogText;
	private Context pDialogContext;
	
	public LoadJSONAsyncTask( Context context, String progressText ) 
	{
		this.pDialogText = progressText;
		this.pDialogContext = context;
	}
	
	protected void onPreExecute()
	{
		super.onPreExecute();
		pDialog = new ProgressDialog(pDialogContext);
		pDialog.setMessage(pDialogText);
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false); 
		pDialog.show();
	}
	
	@Override
	protected JSONObject doInBackground(String...arg0) 
	{
		try 
		{
			// download JSON content
			String patientId = (String)arg0[0]; 
			String dataType = (String)arg0[1];
			String link = "http://glass.herumla.com/?patient_id=" + patientId + "&dataType=" + dataType;
			
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
            
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = new String();
            
            while ((line = in.readLine()) != null) 
            {
            	sb.append(line);
            	break;
            }
            in.close();
			
            // make JSON
			JSONObject json = new JSONObject( sb.toString() );
			
			// return if successful
		    if (  ((String) json.get("result_status")).equals("success") )
		    {
		    	return json;
		    }
		}
		catch(Exception e) 
		{
			// error
            System.out.println("unable to read json.");
            Global.alertUser(pDialogContext, "Exception", "Unable to read JSON.");
		}
		
    	// return if unsuccessful
    	return new JSONObject();
	}
	
	@Override
	protected void onPostExecute(JSONObject j) 
	{
		pDialog.dismiss();
	}
}