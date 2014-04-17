package edu.ucdavis.glass.sepsis.support;

import java.util.ArrayList;
import java.util.List;
import java.lang.Object;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

public class RecentPatientActivity extends Activity {

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
        setContentView(mCardScrollView);
    }

    private void createCards() {
        mCards = new ArrayList<Card>();

        Card newcard;

        newcard = new Card(this);
        newcard.setText("This card has a footer.");
        newcard.setFootnote("I'm the footer!");
//        newcard.setImageLayout(Card.ImageLayout.FULL);
        mCards.add(newcard);

        newcard = new Card(this);
        newcard.setText("This card has a puppy background image.");
        newcard.setFootnote("How can you resist?");
//        newcard.setImageLayout(Card.ImageLayout.FULL);
        mCards.add(newcard);

        newcard = new Card(this);
        newcard.setText("This card has a mosaic of puppies.");
        newcard.setFootnote("Aren't they precious?");
//        newcard.setImageLayout(Card.ImageLayout.FULL);
        mCards.add(newcard);
    }
    
    private class PatientCardScrollAdapter extends CardScrollAdapter {

        @Override
        public int getPosition(Object item) {
            return mCards.indexOf(item);
        }

        @Override
        public int getCount() {
            return mCards.size();
        }

        @Override
        public Object getItem(int position) {
            return mCards.get(position);
        }

        /**
         * Returns the amount of view types.
         */
        @Override
        public int getViewTypeCount() {
            return Card.getViewTypeCount();
        }

        /**
         * Returns the view type of this card so the system can figure out
         * if it can be recycled.
         */
        @Override
        public int getItemViewType(int position){
            return mCards.get(position).getItemViewType();
        }

        @Override
        public View getView(int position, View convertView,
                ViewGroup parent) {
            return  mCards.get(position).getView(convertView, parent);
        }
    	
//    	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//        	startActivity( new Intent(getApplicationContext(), OverviewActivity.class) );
//        }
    }
}