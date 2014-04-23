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
	
	public final static String PATIENT_ID = "Patient info";
    private ArrayList<Card> mCards;
    private CardScrollView mCardScrollView;
    private String patientID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Assign dummy data for global ID and Name        
        for (int i = 0; i < global.MAX_RECENT_PATIENT; i++)
        {
        	global.id.add(String.valueOf(i));
        	global.name.add("Patient Name " + String.valueOf(i));
        }
        ///////////////
        
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
        		Intent overviewIntent = new Intent(getApplicationContext(), OverviewActivity.class);

        		//Get the patient ID from the global class. 
        		//Couldn't send it by integer, I converted it to String to put to intent
        		patientID = String.valueOf(global.id.get(position));
        		overviewIntent.putExtra(PATIENT_ID, patientID);
        		
        		startActivity( overviewIntent );
        	}
        });
    }

    private void createCards() {
        mCards = new ArrayList<Card>(global.MAX_RECENT_PATIENT);

        Card newcard;
        
        for (int i = 0; i < global.name.size(); i++)
        {
        	newcard = new Card(this);
        	newcard.setText(global.name.get(i));
        	newcard.setImageLayout(Card.ImageLayout.LEFT);
        	newcard.addImage(R.drawable.default_user);
        	mCards.add(newcard);
        }

    }
    
    private class PatientCardScrollAdapter extends CardScrollAdapter {

        @Override
        public int getPosition(Object item) {
            return mCards.indexOf(item);
        }

        @Override
        public int getCount() {
        	//System.out.println(mCards.size());
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
        public View getView(int position, View convertView, ViewGroup parent) {
            return  mCards.get(position).getView(convertView, parent);
        }
    }
}