package com.example.test1;

import android.app.Activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class keyword extends Activity implements OnClickListener {

	protected static final int REQUEST_OK = 1;
	public String mess;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.keyword);
		
		Button b = (Button) findViewById(R.id.button2);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// use this to start and trigger a service
				Intent i= new Intent(getApplicationContext(), MyService.class);
				i.putExtra("arg", mess);
				startService(i);
			//	finish();
				// potentially add data to the intent

			}
		});
		
		findViewById(R.id.button1).setOnClickListener(this);


		
	}

	@Override
	public void onClick(View v) {
		 Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
         i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
         try {
             startActivityForResult(i, REQUEST_OK);
         } catch (Exception e) {
        	 Toast.makeText(this, "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
         }
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_OK  && resultCode==RESULT_OK) {
        	ArrayList<String> thingsYouSaid = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        	((TextView)findViewById(R.id.text1)).setText(thingsYouSaid.get(0));
        mess = thingsYouSaid.get(0) ;
        /*	String name = "krishnamfile";
        	File file = new File(getFilesDir(),name);
        	BufferedOutputStream bufStream = null;
			try {
				bufStream = new BufferedOutputStream(new FileOutputStream(name));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            PrintStream p = new PrintStream(bufStream);
            p.println(thingsYouSaid.get(0));
            p.close();
            try {
				bufStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	*/
        
        }
    }

}
