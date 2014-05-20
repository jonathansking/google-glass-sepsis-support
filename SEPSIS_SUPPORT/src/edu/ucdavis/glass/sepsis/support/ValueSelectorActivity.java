package edu.ucdavis.glass.sepsis.support;

import java.util.ArrayList;
import java.lang.Object;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.*;

public class ValueSelectorActivity extends Activity implements GestureDetector.BaseListener
{
    private ArrayList<Integer> cardValues;
    private CardScrollView mCardScrollView;
    private AudioManager mAudioManager;
	private GestureDetector mGestureDetector;
	private Context mContext;
	private int requestCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mContext = this.getBaseContext();
        
        // get card values from intent
        cardValues = getIntent().getIntegerArrayListExtra("values");
        requestCode = getIntent().getIntExtra("requestCode",-1);
        // set up view
        mCardScrollView = new CardScrollView(this) {
            @Override
            public final boolean dispatchGenericFocusedEvent(MotionEvent event) {
                if (mGestureDetector.onMotionEvent(event)) {
                    return true;
                }
                return super.dispatchGenericFocusedEvent(event);
            }
        };
        
        mCardScrollView.setAdapter( new ValueScrollAdapter() );
        setContentView(mCardScrollView);

        mGestureDetector = new GestureDetector(this).setBaseListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mCardScrollView.activate();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCardScrollView.deactivate();
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mGestureDetector.onMotionEvent(event);
    }
    
    @Override
    public boolean onGesture(Gesture gesture) {
        if (gesture == Gesture.TAP) {
            finish();
            
        	Intent resultIntent = new Intent();
        	if(2 == requestCode)
        	{
        		if(0 == cardValues.get(mCardScrollView.getSelectedItemPosition()) )
        			resultIntent.putExtra("selected_value", false);	
        		else
        			resultIntent.putExtra("selected_value", true);
        	}
        	else
            {
        		resultIntent.putExtra("selected_value", cardValues.get(mCardScrollView.getSelectedItemPosition()) );
            }
            setResult(RESULT_OK, resultIntent);
            mAudioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);
            return true;
        }
        return false;
    }

    private class ValueScrollAdapter extends CardScrollAdapter 
    {
        @Override
        public int getPosition(Object item) 
        {
            return cardValues.indexOf(item);
        }

        @Override
        public int getCount() 
        {
            return cardValues.size();
        }

        @Override
        public Object getItem(int position) 
        {
            return cardValues.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) 
        {
        	View view = convertView;
        	
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.card_select_value, null);

            TextView left = (TextView) view.findViewById(R.id.value);
            
            if (2 == requestCode)
            {
            	if(cardValues.get(position) == 0)
	            	left.setText( "OFF" );
	            else
	            	left.setText( "ON" );
            }
            else
            {
            	if(cardValues.get(position) == -1)
	            	left.setText( "Never" );
	            else
	            	left.setText( cardValues.get(position).toString() );
            }
            
            return  view;
        }
    }
}