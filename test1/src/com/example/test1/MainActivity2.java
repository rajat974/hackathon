package com.example.test1;


import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.util.ArrayList;
import android.app.Service;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity2 extends Service {
    /** Called when the activity is first created. */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    //public void onCreate(Bundle savedInstanceState) {
      //  super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        
      
        String cNumber;
        String name;
        String id;
        String key;
        int counter=0;
        Bundle contacts = new Bundle();
        
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        
        if (cur.getCount() > 0) {
		   while (cur.moveToNext()) {
			    id = cur.getString( cur.getColumnIndex(ContactsContract.Contacts._ID));
			name = cur.getString( cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
			   //Query phone here.  Covered next
			Cursor pCur = cr.query(   ContactsContract.CommonDataKinds.Phone.CONTENT_URI,    null,   ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",  new String[]{id}, null);
			 while (pCur.moveToNext()) {
			
			cNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			 
			 
			//list.add(cNumber);
			key = Integer.toString(counter);
			counter++;
			contacts.putString(key,cNumber);
			  } 
			      pCur.close();
			}
		   }
		   cur.close();
      }
    
	    Intent  nxt_srvc_intent = new Intent(MainActivity2.this, MyService2.class);
	    nxt_srvc_intent.putExtras(contacts);
	    startService(nxt_srvc_intent);
		return 0;
    }

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}


