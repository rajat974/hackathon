package com.example.test1;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;



	

public  class MyService extends Service
{
	public String mess;
	
	protected AudioManager mAudioManager; 
    protected SpeechRecognizer mSpeechRecognizer;
    protected Intent mSpeechRecognizerIntent;
    protected final Messenger mServerMessenger = new Messenger(new IncomingHandler(this));

    protected boolean mIsListening=false;
    protected volatile boolean mIsCountDownOn;

    static final int MSG_RECOGNIZER_START_LISTENING = 1;
    static final int MSG_RECOGNIZER_CANCEL = 2;

    private int mBindFlag;
    private Messenger mServiceMessenger;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE); 
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener(new SpeechRecognitionListener());
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                         RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                                         this.getPackageName());

        //mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
    }

    protected static class IncomingHandler extends Handler
    {
        private WeakReference<MyService> mtarget;

        IncomingHandler(MyService target)
        {
            mtarget = new WeakReference<MyService>(target);
        }


        @Override
        public void handleMessage(Message msg)
        {
            final MyService target = mtarget.get();

            switch (msg.what)
            {
                case MSG_RECOGNIZER_START_LISTENING:
                	//Toast.makeText(target, "startde", Toast.LENGTH_SHORT).show();
             //       if (Build.VERSION.SDK_INT >= 8);//Build.VERSION_CODES.JELLY_BEAN)
               //     {
                        // turn off beep sound  
                 //       target.mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
                  //  }
                     if (!target.mIsListening)
                     {
                         target.mSpeechRecognizer.startListening(target.mSpeechRecognizerIntent);
                         target.mIsListening = true;
                         
                        //Log.d(TAG, "message start listening"); //$NON-NLS-1$
                     }
                     break;

                 case MSG_RECOGNIZER_CANCEL:
                      target.mSpeechRecognizer.cancel();
                      target.mIsListening = false;
                      //Log.d(TAG, "message canceled recognizer"); //$NON-NLS-1$
                      break;
             }
       } 
    } 

    // Count down timer for Jelly Bean work around
    protected CountDownTimer mNoSpeechCountDown = new CountDownTimer(3000, 1000)
    {

        @Override
        public void onTick(long millisUntilFinished)
        {
            // TODO Auto-generated method stub
        //	Toast.makeText(getApplicationContext(), "rajat",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFinish()
        {
            mIsCountDownOn = false;
            Message message = Message.obtain(null, MSG_RECOGNIZER_CANCEL);
            try
            {
                mServerMessenger.send(message);
                message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
                mServerMessenger.send(message);
              //  Toast.makeText(getApplicationContext(),"rajat",Toast.LENGTH_SHORT).show();
            }
            catch (RemoteException e)
            {

            }
        }
    };

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) 
    {
    	 mess=(String)intent.getExtras().get("arg");
        //mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
        try
        {
            Message msg = new Message();
            msg.what = MSG_RECOGNIZER_START_LISTENING; 
            mServerMessenger.send(msg);
            //Toast.makeText(getApplicationContext(), "start listening", Toast.LENGTH_SHORT).show();
        }
        catch (RemoteException e)
        {

        }
        return  START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (mIsCountDownOn)
        {
            mNoSpeechCountDown.cancel();
        }
        if (mSpeechRecognizer != null)
        {
            mSpeechRecognizer.destroy();
        }
    }

    protected class SpeechRecognitionListener implements RecognitionListener
    {

        @Override
        public void onBeginningOfSpeech()
        {
            // speech input will be processed, so there is no need for count down anymore
            if (mIsCountDownOn)
            {
            	int i=10;
            	 while(i>0){
                     i--;
                 	mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
                 }
                mIsCountDownOn = false;
                mNoSpeechCountDown.cancel();
            }               
            //Log.d(TAG, "onBeginingOfSpeech"); //$NON-NLS-1$
        }

        @Override
        public void onBufferReceived(byte[] buffer)
        {
            String sTest = "";
        }

        @Override
        public void onEndOfSpeech()
        {
           // Log.d("TESTING: SPEECH SERVICE", "onEndOfSpeech"); //$NON-NLS-1$
        	mIsListening=false;
        	Message message = new Message();
        	message.what = MSG_RECOGNIZER_START_LISTENING;
        	try {
				mServerMessenger.send(message);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }

        @Override
        public void onError(int error)
        {
            if (mIsCountDownOn)
            {
                mIsCountDownOn = false;
                mNoSpeechCountDown.cancel();
            }
             mIsListening = false;
             Message message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
             try
             {
                    mServerMessenger.send(message);
             }
             catch (RemoteException e)
             {

             }
            //Log.d(TAG, "error = " + error); //$NON-NLS-1$
        }

        @Override
        public void onEvent(int eventType, Bundle params)
        {

        }

        @Override
        public void onPartialResults(Bundle partialResults)
        {
        	//CharSequence str = partialResults.toString();
        	//Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onReadyForSpeech(Bundle params)
        {
            if (Build.VERSION.SDK_INT >=8);//Build.VERSION_CODES.JELLY_BEAN)
            {
                mIsCountDownOn = true;
                int i=10;
                while(i>0){
                    i--;
                	mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
                }
                //}
                mNoSpeechCountDown.start();
              
               // mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
            }
         //   Log.d("TESTING: SPEECH SERVICE", "onReadyForSpeech"); //$NON-NLS-1$
        }

        @Override
        public void onResults(Bundle results)
        {
            //Log.d(TAG, "onResults"); //$NON-NLS-1$
        	String str = new String();
           // Log.d(TAG, "onResults " + results);
            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            for (int i = 0; i < data.size(); i++)
            {
                   //   Log.d(TAG, "result " + data.get(i));
                      str += data.get(i);
            }
           // mText.setText("results: "+String.valueOf(data.size()));
            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
			
           

            if (mess.contentEquals(str)){
            	Intent i = new Intent(getApplicationContext(),MainActivity2.class);
            	startService(i);
            	
            }
            	
            //("MESSAGES",str);
           // try {
            mIsListening=false;
            	Message msg = new Message();
            	msg.what=MSG_RECOGNIZER_START_LISTENING;
				try {
					mServerMessenger.send(msg);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
           // Toast.makeText(getApplicationContext(), "done2", Toast.LENGTH_SHORT).show();
				
        }

        @Override
        public void onRmsChanged(float rmsdB)
        {

        }



    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
}
	
