package edu.ucdavis.glass.sepsis.support;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.glass.app.*;
import com.google.android.glass.widget.*;


public class QRScanner extends Activity {
	protected void onCreate(Bundle savedInstanceState){ 
	    super.onCreate(savedInstanceState);
	    
	    try{ 
		    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		    startActivityForResult(intent, 0); 
		    
		    Card card1 = new Card(this);
		    card1.setText("Spoken Words!");
		    card1.setFootnote("my app");
		    View card1View = card1.getView();
		    setContentView(card1View);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// when a QR code is read, it will send a result code 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == 0){
	    	if (resultCode == RESULT_OK){
	    		String contents = data.getStringExtra("SCAN_RESULT");
	    		Card card1 = new Card(this);
	            card1.setText(contents);
	            card1.setFootnote("zxing");
	            View card1View = card1.getView();
	            setContentView(card1View);
	            
	    	}
	    	else if (resultCode == RESULT_CANCELED) {
//	            Handle cancel
	        }
    	}
    	super.onActivityResult(requestCode, resultCode, data);
    }
}