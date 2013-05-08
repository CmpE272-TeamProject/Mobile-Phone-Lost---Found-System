package com.example.mobilelostfound;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
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

/* This class provides the functionality to send the message to DynamoDB
 * from where the web application will fetch and display the message
 * to the owner of the phone once he logs in
 */

class SendMessage extends AsyncTask<String, Void, GetItemResult> 
{
	private Context 			context;
    private AmazonDynamoDBClient iClient;
    public Activity 			activityName;
    private String imeiNumber;
        
	public SendMessage(Context aContext, Activity aActivity) 
	{
        this.context     = aContext;
        this.activityName = aActivity;     
   
    }
	
			
	@Override
	protected GetItemResult doInBackground(String... params) {
		// TODO Auto-generated method stub
		String milliSecStr = String.valueOf(getMilliSecond());
		imeiNumber = params[0];
		
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
            System.err.println("Create items failed.");
        } 
		
		return null;
	}
	
	/*Instantiating AWS service
	 * returns void
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
		/*	try {             	
				String emailAddress = getEmailAddress();
                GmailSender sender = new GmailSender("mobile.lostfound@gmail.com", "cmpe272group5");
                sender.sendMail("This is Subject",   
                        "This is Body",   
                        "mobile.lostfound@gmail.com",   
                        "ramya.machina@gmail.com");   
            } catch (Exception e) {   
                Log.e("SendMail", e.getMessage(), e);   
            } */
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
		
		private String getUserName()
		{
			
		}
	 
	 private String getEmailAddress() {
		// TODO Auto-generated method stub
		 try {
	            
				HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
				key.put(Constants.IMEI, new AttributeValue().withS(imeiNumber));
								
				GetItemRequest getItemRequest = new GetItemRequest()
								                .withTableName(Constants.TABLE_NAME_USER)
								                .withKey(key)
								                .withAttributesToGet(Constants.EMAIL); 
				try
	            {
	            	createClient();
	            }
	            catch (Exception ase) {
	            	Log.e("Error", "Amazon DynamoDB client creation failed." + ase);
	                
	            }
	            
				GetItemResult resultItem = iClient.getItem(getItemRequest);
				Map<String, AttributeValue> emailAddress = resultItem.getItem();
				for (Map.Entry<String, AttributeValue> item : emailAddress.entrySet()) {
		            String attributeName = item.getKey();
		            AttributeValue value = item.getValue();
				}
		 }  catch (Exception ase) {
	            System.err.println("Failed to retrieve item in " + Constants.TABLE_NAME_USER);
	            return null;
	        }
		 	return null;
		 
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