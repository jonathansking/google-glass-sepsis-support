package edu.ucdavis.glass.sepsis.support;

import java.util.ArrayList;
import java.lang.Object;

import android.app.Activity;
import android.content.Intent;
//import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.AdapterView;

import android.widget.AdapterView;

import com.google.android.glass.app.*;
import com.google.android.glass.widget.*;
//import com.google.android.glass.app.Card;
//import com.google.android.glass.widget.CardScrollAdapter;
//import com.google.android.glass.widget.CardScrollView;
//import com.google.android.glass.widget.CardScrollView;

public class RecentPatientActivity extends Activity {
	public final static String EXTRA_MESSAGE = "Patient Info";
    private ArrayList<Card> mCards;
    private CardScrollView mCardScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createCards();
        
        mCardScrollView = new CardScrollView(this);
        PatientCardScrollAdapter adapter = new PatientCardScrollAdapter();
        mCardScrollView.setAdapter(adapter);
        mCardScrollView.activate();
        mCardScrollView.setHorizontalScrollBarEnabled(true); //changes
        setContentView(mCardScrollView);
        
        mCardScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        		//Object o = mCardScrollView.getItemAtPosition(position);
        		//use position to get the position of the card and pass it to the overview activity
        		Intent overview = new Intent(getApplicationContext(), OverviewActivity.class);
        		//example: send position to the overview Activity
        		//what we need: from position get the patient ID and send it to Overview!
        		//overview.putExtra(EXTRA_MESSAGE, position)
        		startActivity( overview ); 
        	}
        });
    }

    private void createCards() {
        mCards = new ArrayList<Card>();

        Card newcard;

        //Dummies data. Need request to database to get the latest 5 patients
        String patientName[]={"Joe Doe","Sarah Black","John Smith","Angolina Nguyen","Josue Hernandez"};
        
        for (int i = 0; i < patientName.length; i++)
        {
        	newcard = new Card(this);
        	newcard.setText(patientName[i]);
        	newcard.setImageLayout(Card.ImageLayout.LEFT);
        	newcard.addImage(R.drawable.default_user);
        	mCards.add(newcard);
        }
//        newcard = new Card(this);
//        newcard.setText("Joe Doe");
//        newcard.setImageLayout(Card.ImageLayout.LEFT);
//        newcard.addImage(R.drawable.default_user);
//        mCards.add(newcard);
//
//        newcard = new Card(this);
//        newcard.setText("This card has a puppy background image.");
//        newcard.setFootnote("How can you resist?");
////        newcard.setImageLayout(Card.ImageLayout.FULL);
//        mCards.add(newcard);
//
//        newcard = new Card(this);
//        newcard.setText("This card has a mosaic of puppies.");
//        newcard.setFootnote("Aren't they precious?");
////        newcard.setImageLayout(Card.ImageLayout.FULL);
//        mCards.add(newcard);
    }
    
    private class PatientCardScrollAdapter extends CardScrollAdapter {

        @Override
        public int getPosition(Object item) {
        	System.out.println("Line 69");
            return mCards.indexOf(item);
        }

        @Override
        public int getCount() {
        	System.out.println("Line 75");
        	System.out.println(mCards.size());
            return mCards.size();
        }

        @Override
        public Object getItem(int position) {
        	System.out.println("Line 81");
            return mCards.get(position);
        }

        /**
         * Returns the amount of view types.
         */
        @Override
        public int getViewTypeCount() {
        	System.out.println("Line 90");
            return Card.getViewTypeCount();
        }

        /**
         * Returns the view type of this card so the system can figure out
         * if it can be recycled.
         */
        @Override
        public int getItemViewType(int position){
        	System.out.println("Line 100");
            return mCards.get(position).getItemViewType();
        }

        @Override
        public View getView(int position, View convertView,
                ViewGroup parent) {
        	System.out.println("107");
        	System.out.println(position);
            return  mCards.get(position).getView(convertView, parent);
        }
    }
}