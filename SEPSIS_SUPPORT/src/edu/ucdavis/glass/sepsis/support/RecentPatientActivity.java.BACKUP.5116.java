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
//        	newcard.setFootnote("Tap to load patient"); // maybe something like this
        	mCards.add(newcard);
        }

    }
    
    private class PatientCardScrollAdapter extends CardScrollAdapter {

        @Override
        public int getPosition(Object item) {
<<<<<<< HEAD
        	//System.out.println("Line 69");
=======
>>>>>>> f21d8c80088924845495a52720ca80a8a0eb6d9f
            return mCards.indexOf(item);
        }

        @Override
        public int getCount() {
<<<<<<< HEAD
        	//System.out.println("Line 75");
        	//System.out.println(mCards.size());
=======
        	System.out.println(mCards.size());
>>>>>>> f21d8c80088924845495a52720ca80a8a0eb6d9f
            return mCards.size();
        }

        @Override
        public Object getItem(int position) {
<<<<<<< HEAD
        	//System.out.println("Line 81");
=======
>>>>>>> f21d8c80088924845495a52720ca80a8a0eb6d9f
            return mCards.get(position);
        }

        /**
         * Returns the amount of view types.
         */
        @Override
        public int getViewTypeCount() {
<<<<<<< HEAD
        	//System.out.println("Line 90");
=======
>>>>>>> f21d8c80088924845495a52720ca80a8a0eb6d9f
            return Card.getViewTypeCount();
        }

        /**
         * Returns the view type of this card so the system can figure out
         * if it can be recycled.
         */
        @Override
        public int getItemViewType(int position){
<<<<<<< HEAD
        	//System.out.println("Line 100");
=======
>>>>>>> f21d8c80088924845495a52720ca80a8a0eb6d9f
            return mCards.get(position).getItemViewType();
        }

        @Override
<<<<<<< HEAD
        public View getView(int position, View convertView,
                ViewGroup parent) {
        	//System.out.println("107");
        	//System.out.println(position);
=======
        public View getView(int position, View convertView, ViewGroup parent) {
>>>>>>> f21d8c80088924845495a52720ca80a8a0eb6d9f
            return  mCards.get(position).getView(convertView, parent);
        }
    }
}