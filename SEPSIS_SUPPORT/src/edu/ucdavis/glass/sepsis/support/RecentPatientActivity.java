package edu.ucdavis.glass.sepsis.support;

import java.util.ArrayList;
import java.lang.Object;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.glass.app.*;
import com.google.android.glass.widget.*;

import org.json.JSONException;
import org.json.JSONObject;

public class RecentPatientActivity extends Activity 
{
    private ArrayList<Card> mCards;
    private CardScrollView mCardScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        createCards();
        mCardScrollView = new CardScrollView(this);
        PatientCardScrollAdapter adapter = new PatientCardScrollAdapter();
        mCardScrollView.setAdapter(adapter);
        mCardScrollView.activate();
        mCardScrollView.setHorizontalScrollBarEnabled(true);
        setContentView(mCardScrollView);
        
        mCardScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
        	{
        		// use position to get the user name and id
        		Patient p = (Patient)Global.recentPatients.toArray()[position];

        		// move recent patient to front of deque
			    try {
					Global.pushRecentPatient( p.id, new JSONObject(p.json) );
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

            	// go to overview
            	startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
        	}
        });
    }

    private void createCards() {
        mCards = new ArrayList<Card>( Global.recentPatients.size() );

        for( Patient p : Global.recentPatients )
        {
        	Card c = new Card(this);
        	c.setText( p.name );
        	c.setFootnote( p.id );
        	c.setImageLayout(Card.ImageLayout.LEFT);
        	if( p.id.equals("1") ){
            	c.addImage(R.drawable.patient_1);
        	} else if( p.id.equals("2") ){
            	c.addImage(R.drawable.patient_2);
        	} else if( p.id.equals("3") ) {
            	c.addImage(R.drawable.patient_3);
        	} else if( p.id.equals("4") ) {
            	c.addImage(R.drawable.patient_4);
        	} else {
            	c.addImage(R.drawable.default_user);
        	}
        	mCards.add(c);
        }
        
	    // error, no recent patients
        if( mCards.isEmpty() ) {
        	finish();
        	
        	Global.toastUser(this, "No recent patients");
            System.out.println("No recent patients.");
        }
    }
    
    private class PatientCardScrollAdapter extends CardScrollAdapter 
    {
        @Override
        public int getPosition(Object item) 
        {
            return mCards.indexOf(item);
        }

        @Override
        public int getCount() 
        {
            return mCards.size();
        }

        @Override
        public Object getItem(int position) 
        {
            return mCards.get(position);
        }
        
        // returns the amount of view types.
        @Override
        public int getViewTypeCount() 
        {
            return Card.getViewTypeCount();
        }

        // returns the view type of this card so the system can figure out if it can be recycled.
        @Override
        public int getItemViewType(int position)
        {
            return mCards.get(position).getItemViewType();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) 
        {
            return  mCards.get(position).getView(convertView, parent);
        }
    }
    
}