package com.example.mobilelostfound;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;

	/* This class provides the initial screen which initiates 
	 * the process of sending the notification.The founder of the phone can press 
	 * the Notify button to send a message to the owner of the phone.
	 */
	public class MobileLostFoundActivity extends Activity {
		
		private AmazonDynamoDBClient iClient;
		private String				 iDeviceIMEI;
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_mobile_lost_found);
			
			try {
	        	createClient();
	        }
	        catch (Exception ase) {
	        	Log.e("Error", "Amazon DynamoDB client creation failed." + ase);            
	        }
			
			//Get the device IMEI
			iDeviceIMEI = getIMEI();
			
	    	//Instantiating Notify button
	    	Button notifyButton = (Button) findViewById(R.id.notifyButton);
	    	notifyButton.setOnClickListener(handleNotifyButton);
	    	
	    	Log.v("IMEI", "Device IMEI=" + iDeviceIMEI);
		}
	
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.mobile_lost_found, menu);
			return true;
		}
		
		/*Instantiating AWS service
		 * returns void
		 */
		private void createClient() throws Exception {
			
	    	AWSCredentials credentials = new BasicAWSCredentials(Constants.ACCESS_KEY_ID, Constants.SECRET_KEY);
	    	iClient = new AmazonDynamoDBClient(credentials);
	    	iClient.setEndpoint(Constants.US_N_VIRGINIA);//region	    	
	    }
	    
		/*Get the device IMEI
		 * returns 15 digit IMEI as a string
		 */
	    private String getIMEI() 
	    {
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			
			if( telephonyManager != null ) {
				return telephonyManager.getDeviceId();
		}
		
		return null;
	    }
	    
	    /* When Notify Button is clicked, collect the IMEI of the device
	     * and fetch the message from the user. 
	   	 * Upload this message onto the DB which will notify the user by ending an email. 
	     */
	   	       View.OnClickListener handleNotifyButton = new View.OnClickListener() {
	       	public void onClick(View aView) {
	       		
	       		// Show the user a new activity where he can enter the custom message. 
	       		Intent intent = new Intent(aView.getContext(), MessageComposeActivity.class);
	       		aView.getContext().startActivity(intent);      		
	       	}
	       };
}


