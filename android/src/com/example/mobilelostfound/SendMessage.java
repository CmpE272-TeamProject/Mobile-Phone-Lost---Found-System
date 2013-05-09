package com.example.mobilelostfound;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

/* This class provides the functionality to send the message to DynamoDB
 * from where the web application will fetch and display the message
 * to the owner of the phone once he logs in
 */

class SendMessage extends AsyncTask<String, Void, GetItemResult> 
{
	private Context 			context;
    private AmazonDynamoDBClient iClient;
    public Activity 			activityName;
 
        
	public SendMessage(Context aContext, Activity aActivity) 
	{
        this.context     = aContext;
        this.activityName = aActivity;     
   
    }
	
	/*
	 * (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 * This module does the work of adding the data to the 'messages' table 
	 * in dynamoDB. It adds the IMEI, longitude, latitude,date, time, timestamp
	 * (hash key, in milliseconds) and message. 
	 */ 
			
	@Override
	protected GetItemResult doInBackground(String... params) {
		String milliSecStr = String.valueOf(getMilliSecond());
				
		try {
            Map<String, AttributeValue> TableContent = new HashMap<String, AttributeValue>();
            TableContent.put(Constants.HASH_KEY_NAME, new AttributeValue().withN(milliSecStr));
            TableContent.put(Constants.DATE, new AttributeValue().withS(params[2]));
            TableContent.put(Constants.IMEI, new AttributeValue().withS(params[0]));
            TableContent.put(Constants.LATITUDE, new AttributeValue()
                .withS(params[3]));
            TableContent.put(Constants.MESSAGE, new AttributeValue().withS(params[1]));
            TableContent.put(Constants.LONGITUDE, new AttributeValue().withS(params[4]));
            
            try
            {
            	createClient();
            }
            catch (Exception ase) {
            	Log.e("Error", "Amazon DynamoDB client creation failed." + ase);
                
            }
            PutItemRequest putContent = new PutItemRequest()
            .withTableName(Constants.TABLE_NAME)
            .withItem(TableContent);
            PutItemResult addResult = iClient.putItem(putContent);
          
        } catch (AmazonServiceException ase) {
            Log.v("Add", "Create items failed.");
        } 
		
		return null;
	}
	
	/*Instantiating AWS service using the credentials provided in Constants.java
	 * Returns void
	 */
	
	 private void createClient() throws Exception {	    	
	    	AWSCredentials credentials = new BasicAWSCredentials(Constants.ACCESS_KEY_ID, Constants.SECRET_KEY);
	    	
	    	iClient = new AmazonDynamoDBClient(credentials);
	    	iClient.setEndpoint(Constants.US_N_VIRGINIA);//Region
	    }
	 
	 protected void onPostExecute(GetItemResult aResultItem) 
		{
			super.onPostExecute(aResultItem);
			displaySentMessage();
			
			// Close the activity and come back to previous screen
			activityName.finish();
		}
	 
	 	/* Display toast once the message is sent
	 	 * Returns void
	 	 */
		private void displaySentMessage() 
		{
			Toast toast = Toast.makeText(context, R.string.sent_message, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 80);
			toast.show();    
		}
	
	 /* To get the timestamp in milliseconds
	  * Returns long
	  */
	 private long getMilliSecond()
	 {
		 Date dateValue = new Date();  
		 long timeInMilliseconds = dateValue.getTime();  
		
		 return timeInMilliseconds;
	 
	 }
	 
	 
	 
}