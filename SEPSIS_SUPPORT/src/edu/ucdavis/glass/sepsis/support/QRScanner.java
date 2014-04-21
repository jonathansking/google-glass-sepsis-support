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
	    intent.putExtra("SCAN_MODE",ONE_D_MODE,QR_CODE_MODE,PRODUCT_MODE,DATA_MATRIX_MODE);
	    startActivityForResult(intent, UPC_CODE_REQUEST);  
	    
	    Card card1 = new Card(this);
	    card1.setText("Spoken Words!");
	    card1.setFootnote("my app");
	    View card1View = card1.toView();
	    setContentView(card1View);
	    setDisplayCard(card1);
	}
	
	//when a QR code is read, it will send a result code 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     if (requestCode == UPC_CODE_REQUEST && resultCode == RESULT_OK){
        String contents = data.getStringExtra("SCAN_RESULT");
        Card card1 = new Card(this);
        card1.setText(contents);
        card1.setFootnote("zxing");
        View card1View = card1.toView();
        setContentView(card1View);
        setDisplayCard(card1);

     }
    super.onActivityResult(requestCode, resultCode, data);
    }
}