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
    private AsyncTaskCompleteListener<JSONObject> callback;
	private ProgressDialog pDialog;
	private String pDialogText;
	private Context pDialogContext;
	
	public LoadJSONAsyncTask( Context context, String progressText, AsyncTaskCompleteListener<JSONObject> cb ) 
	{
		this.pDialogText = progressText;
		this.pDialogContext = context;
        this.callback = cb;
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
		JSONObject fail = new JSONObject();
		
		try 
		{
			fail.accumulate("result_status", "fail");
			
			// download JSON content
			String id = (String)arg0[0];

			String link = "http://glass.herumla.com/?patient_id=" + id;
			System.out.println(link);
			
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
			JSONObject success = new JSONObject( sb.toString() );
			
			// return if successful
		    if (  (success.get("result_status").toString()).equals("success") )
		    {
		    	return success;
		    }
		    
		}
		catch(Exception e) 
		{
		    // return if unsuccessful
	    	System.out.println("patient doesn't exist 1.");
	    	return fail;
		}

	    // return if unsuccessful
    	System.out.println("patient doesn't exist 2.");
    	return fail;
		
	}
	
	@Override
	protected void onPostExecute(JSONObject j) 
	{
		pDialog.dismiss();
	    callback.onTaskComplete(j);
	}
}