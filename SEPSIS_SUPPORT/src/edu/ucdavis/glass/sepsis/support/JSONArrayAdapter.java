package edu.ucdavis.glass.sepsis.support;

import java.util.Iterator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONArrayAdapter extends ArrayAdapter<JSONObject> 
{
	private final Context context;
	private final JSONObject json;

	public JSONArrayAdapter(Context context, JSONObject json) 
	{
		super(context, R.layout.row);
		this.context = context;
		this.json = json;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    View rowView = inflater.inflate(R.layout.row, parent, false);
	    TextView leftTextView = (TextView) rowView.findViewById(R.id.leftText);
	    TextView rightTextView = (TextView) rowView.findViewById(R.id.rightText);
	    
	   //loop through a print keys and value
	    Iterator<?> keys = json.keys();
		while( keys.hasNext() ){
	            String key = (String)keys.next();
	            
	            try {
					System.out.println( key + "  " + json.get(key));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}        
		}

	    leftTextView.setText("here");
	
    	return rowView;
  }
} 