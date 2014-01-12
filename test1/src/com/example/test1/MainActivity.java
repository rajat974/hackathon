package com.example.test1;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.R.string;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends Activity {
	
	public static String xt; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button b = (Button) findViewById(R.id.button1);
		TextView tv = (TextView) findViewById(R.id.textView1);
	/*	String name = "krishnamfile";
		File file = new File(getFilesDir(), name);
		
		InputStream in = null;
		try {
			in = openFileInput(name);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		InputStreamReader input = new InputStreamReader(in);
		BufferedReader bufreader = new BufferedReader(input);
		
		  try {
			xt = bufreader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		  try {
			bufreader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	*/
		
		tv.setText(xt);

			
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// use this to start and trigger a service
				Intent i= new Intent(getApplicationContext(), MyService.class);
				// potentially add data to the intent
				i.putExtra("KEY1", "Value to be used by the service");
				getApplicationContext().startService(i); 
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mymenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
	    switch (item.getItemId()) {
        case R.id.item1:
            
            return true;
        case R.id.item2:
			Intent intent = new Intent(MainActivity.this , keyword.class);
			//intent.putExtra("thetext", et.getText().toString() ) ;
			startActivity(intent);
            
            return true;
        default:
            return super.onOptionsItemSelected(item);
		
	}
	}
}
