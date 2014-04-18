package edu.ucdavis.glass.sepsis.support;

import java.util.ArrayList;
import java.lang.Object;

import android.app.Activity;
//import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.AdapterView;

import com.google.android.glass.app.*;
import com.google.android.glass.widget.*;
//import com.google.android.glass.app.Card;
//import com.google.android.glass.widget.CardScrollAdapter;
//import com.google.android.glass.widget.CardScrollView;
//import com.google.android.glass.widget.CardScrollView;

public class RecentPatientActivity extends Activity {

    private ArrayList<Card> mCards;
    private CardScrollView mCardScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        System.out.println( "here" );
//        System.err.println( "hereerr" );
        createCards();
        
        mCardScrollView = new CardScrollView(this);
        PatientCardScrollAdapter adapter = new PatientCardScrollAdapter();
        mCardScrollView.setAdapter(adapter);
        mCardScrollView.activate();
        System.out.println( mCardScrollView.getCount() );
        setContentView(mCardScrollView);
    }

    private void createCards() {
        mCards = new ArrayList<Card>();

        Card newcard;

        newcard = new Card(this);
        //newcard.setText("This card has a footer.");
        //newcard.setFootnote("I'm the footer!");
        newcard.setImageLayout(Card.ImageLayout.FULL);
        mCards.add(newcard);

//        newcard = new Card(this);
//        //newcard.setText("This card has a puppy background image.");
//        //newcard.setFootnote("How can you resist?");
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
    	
//    	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//        	startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
//        }
    }
}