package com.example.test1;


import java.util.ArrayList;

import com.example.test1.GPSTracker;

import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MyService2 extends Service {

	  @SuppressWarnings("deprecation")
	  GPSTracker gps;
	  
	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	    //TODO do something useful
		  Log.d("Hello","Service");
		  
		  try
		  {
			  gps = new GPSTracker(MyService2.this);  	
			  if(gps.canGetLocation())
		        {
		        	
		        	double latitude = gps.getLatitude();
		        	double longitude = gps.getLongitude();
		        	
		        	// \n is for new line
		        	Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
		        	sendSms(intent,latitude,longitude);
		        }
		        else{
		        	// can't get location
		        	// GPS or Network is not enabled
		        	// Ask user to enable GPS/network in settings
		        	Log.d("sexy","GPSFailed");
		        	gps.showSettingsAlert();
		        	}
			  	
			Log.d("Calling","Start");
	        String uri = "tel:9581243782";
	        Intent intent1 = new Intent(Intent.ACTION_CALL);
	        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        intent1.setData(Uri.parse(uri));
	        startActivity(intent1);
	        Log.d("sexy","CALLSuccess");
	        //Log.d("GPS", "Started");
	        
	      
			// check if GPS enabled		
	        
		  }
		  catch(Exception e)
		  {
			  e.printStackTrace();
		  }
		return Service.START_NOT_STICKY;
	  }
	  
	  


	@Override
	  public IBinder onBind(Intent intent) {
	  //TODO for communication return IBinder implementation
	    return null;
	  }
	  
	  public void sendSms(Intent i, double latitude, double longitude)
	  {
		  int j=0;
		  String number;
		  while(i.getExtras().get(Integer.toString(j))!=null)
		  {
			  number=(String)i.getExtras().get(Integer.toString(j));
			  PendingIntent pi = PendingIntent.getActivity(this, 0,new Intent(this, MainActivity2.class), 0);                
			  SmsManager sms = SmsManager.getDefault();
			  String msg= "2I am in DANGER. Please help me. I am located here-"+Double.toString(latitude)+Double.toString(longitude);
			  sms.sendTextMessage(number, null,msg , null, null);
			  j++;
		  }
		  
	  }
	  
	} 