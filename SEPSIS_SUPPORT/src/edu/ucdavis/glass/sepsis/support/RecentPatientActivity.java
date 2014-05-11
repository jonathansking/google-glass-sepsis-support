package edu.ucdavis.glass.sepsis.support;

import java.util.ArrayList;
import java.lang.Object;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.glass.app.*;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.*;

public class RecentPatientActivity extends Activity 
{
	private GestureDetector mGestureDetector;
    private ArrayList<Card> mCards;
    private CardScrollView mCardScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        mGestureDetector = createGestureDetector(this);
        
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
        		// use position to get the user name and id
        		Patient p = (Patient)Global.recentPatients.toArray()[position];

        		// move recent patient to front of deque
//			    Global.pushRecentPatient( p.id, p.json );

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
        	c.addImage(R.drawable.default_user);
        	mCards.add(c);
        }
        
	    // error, no recent patients
        if( mCards.isEmpty() ) {
            System.out.println("no recent patients.");
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
                	System.out.println("Tap on Patient");
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
                	// go back to welcome view
                    finish();
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