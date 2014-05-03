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
        mCardScrollView.setHorizontalScrollBarEnabled(true); //changes
        setContentView(mCardScrollView);
        
        mCardScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
        	{
        		//use position to get the position of the card and pass it to the overview activity
        		Intent overviewIntent = new Intent(getApplicationContext(), OverviewActivity.class);
        		overviewIntent.putExtra(Global.PATIENT_ID, ((Patient)Global.recentPatients.toArray()[position]).getId() ); // :)
        		startActivity( overviewIntent );
        	}
        });
    }

    private void createCards() {
        mCards = new ArrayList<Card>( Global.recentPatients.size() );

        for( Patient p : Global.recentPatients )
        {
        	Card c = new Card(this);
        	c.setText( p.getName() );
        	c.setFootnote( p.getId() );
        	c.setImageLayout(Card.ImageLayout.LEFT);
        	c.addImage(R.drawable.default_user);
        	mCards.add(c);
        }
        
        if( mCards.isEmpty() ) {
        	Card c = new Card(this);
        	c.setText( R.string.no_patients );
        	c.setImageLayout(Card.ImageLayout.LEFT);
        	c.addImage(R.drawable.empty_set);
        	mCards.add(c);
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
        	//System.out.println(mCards.size());
            return mCards.size();
        }

        @Override
        public Object getItem(int position) 
        {
            return mCards.get(position);
        }

        /**
         * Returns the amount of view types.
         */
        @Override
        public int getViewTypeCount() 
        {
            return Card.getViewTypeCount();
        }

        /**
         * Returns the view type of this card so the system can figure out
         * if it can be recycled.
         */
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