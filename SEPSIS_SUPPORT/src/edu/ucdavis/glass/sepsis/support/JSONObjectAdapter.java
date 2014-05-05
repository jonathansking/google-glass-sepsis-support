package edu.ucdavis.glass.sepsis.support;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class JSONObjectAdapter extends BaseAdapter implements ListAdapter
{
	private JSONObject json;
	private Context context;
	
	public JSONObjectAdapter(Context c, JSONObject j)
	{
	    super();
	    this.json = j;
	    this.context = c;
	}
	
	public int getCount()
	{
        System.out.println( "getCount: " + json.length() );
	    return json.length();
	}
	
	public Object getItem(int position)
	{
        System.out.println( "getItem: " + position );
	    return position;
	}
	
	public long getItemId(int position)
	{
        System.out.println( "getItemID: " + position );
		return position;
	}
	
	public boolean areAllItemsEnabled() {
		return false;
	}
	
	public boolean isEnabled(int position) {
		return false;
	}
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
	    View view = convertView;
	
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = vi.inflate(R.layout.row, null);

        TextView left = (TextView) view.findViewById(R.id.leftText);
        TextView right = (TextView) view.findViewById(R.id.rightText);
        
        // not the best solution, but it will work for now
        int p = 0;
        
        for(Iterator<String> iter = json.keys(); iter.hasNext();) {
        	
            String key = iter.next();
            Object value = new Object();
            
            System.out.println( "p: " + p );
            System.out.println( "position: " + position );
            
	        try {
	            value = json.get(key);

	            System.out.println( key );
	            System.out.println( value );

	            
	        } catch (JSONException e) {
	            System.out.println( "EXCEPTION" );
	        }
            
            if( p == position ) {
	            left.setText( key );
	            right.setText( value.toString() );
			}

            p++;
        }
        
	    return view;
	}
}