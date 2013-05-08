package com.example.mobilelostfound;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/* This class helps the user to compose the message and provide all the information
 * required to be stored and displayed in web page.
 */
public class MessageComposeActivity extends Activity implements LocationListener{
	
	String latitude;
	String longitude;
	LocationManager mLocationManager;
		
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
        
        getGPSLocation();		

        //Implement listener for send button click  
        sendButton.setOnClickListener(new  View.OnClickListener() {
			
		@Override
		public void onClick(View v) {	
			//new ServerTask().execute(MainActivity.this.getApplicationContext());
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
				
						
				  
				// Seperate the send message task from UI thread
				// Hence done in background using AsyncTask
				
				SendMessage message = new SendMessage(myAppContext, MessageComposeActivity.this);
				message.execute(getIMEI(),messageText, getDateTime()/*,"-121.88159577","-121.88159577"*/,latitude, longitude);
			}
					
		}
       });
	}
	
	private void getGPSLocation() {
		// TODO Auto-generated method stub
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
	
	 private String getIMEI() {
	    	TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
	    	if( telephonyManager != null ) {
	    		return telephonyManager.getDeviceId();
	    	}	    	
	    	return null;
	    }
	 
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
	 
	/*private void findCurrentLocation() {
	        myLocation.getLocation(this);
	        
	    }*/

	/*public LocationResult locationResult = new LocationResult() {

	        @Override
	        public void gotLocation(Location location) {
	            // TODO Auto-generated method stub
	            if (location != null) {
	            	
	            	Log.v("location", location.getLatitude() + ","
	                        + location.getLongitude());
	            	latitude = String.valueOf(location.getLatitude());
	            	longitude = String.valueOf(location.getLongitude());
	            			
	            	
	                String strloc  = location.getLatitude() + ","
	                        + location.getLongitude();
	            }
	        }
	    };*/
	    
	    
	    
	
