package edu.ucdavis.glass.sepsis.support;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class LoadJSONAsyncTask extends AsyncTask<String,Void,JSONObject> 
{
    private Global.AsyncTaskCompleteListener<JSONObject> callback;
	private ProgressDialog pDialog;
	private String pDialogText;
	private Context pDialogContext;
	
	public LoadJSONAsyncTask( Context context, String progressText, Global.AsyncTaskCompleteListener<JSONObject> cb ) 
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
		JSONObject result = new JSONObject();
		try {
			result.accumulate("result_status", "fail");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try 
		{
			// download JSON content
			String dataType = (String)arg0[0];
			
			Patient p = Global.recentPatients.peek();

			String link = "http://glass.herumla.com/?patient_id=" + p.getId() + "&dataType=" + dataType + "&stateNumber=" + p.getViewingState();
//			String link = "http://glass.herumla.com/?patient_id=" + p.getId() + "&dataType=" + dataType;
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
			JSONObject json = new JSONObject( sb.toString() );
			
			// return if successful
		    if (  (json.get("result_status").toString()).equals("success") )
		    {
		    	return json;
		    }
		    
		}
		catch(Exception e) 
		{
			// error
			// don't put a notification here because we are in a background thread.

		    // return if unsuccessful
	    	return result;
		}

	    // return if unsuccessful
    	return result;
		
	}
	
	@Override
	protected void onPostExecute(JSONObject j) 
	{
		pDialog.dismiss();
	    callback.onTaskComplete(j);
	}
}