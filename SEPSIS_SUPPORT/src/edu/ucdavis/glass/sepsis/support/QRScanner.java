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

import com.google.android.glass.app.*;
import com.google.android.glass.widget.*;


public class QRScanner extends Activity {
	protected void onCreate(Bundle savedInstanceState){ 
	    super.onCreate(savedInstanceState);
	    
	    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	    intent.setPackage("com.google.zxing.client.android");
	    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
	    startActivityForResult(intent, 0);  
	    
	}
	
	//when a QR code is read, it will send a result code 
    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     if (requestCode == 0){
    	if (resultCode == RESULT_OK){
    		String contents = data.getStringExtra("SCAN_RESULT");
    	} else if (resultCode == RESULT_CANCELED) {
            // Handle cancel
        }
     }
    }*/
}