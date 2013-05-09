package com.example.mobilelostfound;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

	/* This class helps the user to compose the message and provide all the information
	 * required to be stored and displayed in web page.It 
	 */
	public class MessageComposeActivity extends Activity implements LocationListener{
		
		String latitude;
		private AmazonDynamoDBClient iClient;
		String longitude;
		LocationManager mLocationManager;
		String emailAdrress;
			
		@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_message_compose);
		}
		
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		// Edit text in the layout
        final EditText msgText = (EditText)findViewById(R.id.editText1);
        
        // Member variable for button in the layout   
        final Button sendButton =  (Button)findViewById(R.id.sendButton);
        
        // Finds the longitude and latitude of the location from where message is sent.
        // This uses GPS facility from the phone.
        getGPSLocation();		     
      
        //Implement listener for send button click  
        sendButton.setOnClickListener(new  View.OnClickListener() {
			
        /*
         * (non-Javadoc)
         * @see android.view.View.OnClickListener#onClick(android.view.View)
         * Listener for send button click 
         */
		@Override
		public void onClick(View v) {	
			String messageText = msgText.getText().toString();
			if(messageText.isEmpty())
			{
				Toast toast = Toast.makeText(MessageComposeActivity.this, R.string.enter_message, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 80);
				toast.show();
			}
			else
			{
				msgText.setVerticalScrollBarEnabled(true);				
				Log.v("Message", "Message=" + messageText);				
				Context myAppContext = getApplicationContext();	
				
				// Sends an email using Gmail SMTP server
				 SendEmail();
				  
				// Separate the send message task from UI thread
				// Hence done in background using AsyncTask
				
				SendMessage message = new SendMessage(myAppContext, MessageComposeActivity.this);
				
				message.execute(getIMEI(),messageText, getDateTime(),latitude, longitude);
			}
					
		}
       });
	}       
	  
	/* Sends an email to the owner of the phone from mobile.lostfound@gmail.com.
	 *   The email address is fetched from DynamoDb 'userdetails' table.
	 *   Uses Gmail SMTP server to send a hidden email.  
	 */
	private void SendEmail() {
	
		try {             	
			String emailAddress = getEmailAddress();
            GmailSender sender = new GmailSender("mobile.lostfound@gmail.com", "cmpe272group5");
            sender.sendMail("Found your phone !!!",   
                    "Notification- You have a message from your lost phone at your Mobile Lost & Found web application",   
                    "mobile.lostfound@gmail.com",   
                    emailAddress);   
        } catch (Exception e) {   
            Log.e("SendMail", e.getMessage(), e);   
        } 
        }
	/*
	 * Finds the email address of the owner from userdetails table
	 * Once the user signs up in the web application, his 
	 * email address will be stored in DynamoDb. Hence, it is fetached 
	 * from userdetails table in the DynamoDb
	 */
			
	private String getEmailAddress()
	{
		try
	    {
			AWSCredentials credentials = new BasicAWSCredentials(Constants.ACCESS_KEY_ID, Constants.SECRET_KEY);
	    	
	    	iClient = new AmazonDynamoDBClient(credentials);
	    	iClient.setEndpoint(Constants.US_N_VIRGINIA);
	    }
	    catch (Exception ase) {
	    	Log.e("Error", "Amazon DynamoDB client creation failed." + ase);
	    
	    }
	    
	    ScanRequest scanRequest = new ScanRequest("userdetails");
		ScanResult scanResult = iClient.scan(scanRequest);
		List<Map<String, AttributeValue>> tableData = scanResult.getItems();
		for(Map<String, AttributeValue> rowData : tableData)
		{
		    String user = rowData.get("username").getS();
			String password = rowData.get("password").getS();
			String imei = rowData.get("imeinumber").getS();
			String email = rowData.get("email").getS();
			            
			            if(imei.contentEquals(getIMEI()))
			            {
			            	emailAdrress = email;
			            	return emailAdrress;
			            }
			        }
			 return null;
	}
	
	/* Fetches the longitude and latitude of the message sent.  
	 * Uses GPS facility from the phone.	
	 */
		
	private void getGPSLocation() {
		
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(location != null) {
			latitude = String.valueOf(location.getLatitude());
			longitude = String.valueOf(location.getLongitude());
		}
		else {
		    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		}
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}
		
		/*Fetches the IMEI of the device
		 * Returns String
		 */
		
		 private String getIMEI() 
		 {
		    	TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		    	if( telephonyManager != null ) {
		    		return telephonyManager.getDeviceId();
		    	}	    	
		    	return null;
		    }
		 
		 /*Fetches date and time 
		  * Returns String
		  */
			
		 private String getDateTime()
		 {
			 SimpleDateFormat dateFormat = new SimpleDateFormat(
			            "yyyy-MM-dd HH:mm:ss");
			    Calendar cal = Calendar.getInstance();
			    
			    String timeString = dateFormat.format(cal.getTime());
			    
			    return timeString;	 
		 }
		 
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
			if (location != null) {
	            Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
	        }
			
		}
	
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
	}
		    
		    
		    
		
