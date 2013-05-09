package com.example.mobilelostfound;

	/* This file provides details about the AWS credentials.  
	 * It also provides the details about the DynamoDB tables and attributes
	 */

	public class Constants {
		
		// Credential Properties
		public static final String ACCESS_KEY_ID = "AKIAJZ5EU4EXKKBFX65JQ";
		public static final String SECRET_KEY    = "go3juQyjzQIPVvS4q/87QYpV2FV9Ln6EdjyqsFrhH";
		
		// DynamoDB related
		public static final String US_N_VIRGINIA = "http://dynamodb.us-east-1.amazonaws.com/";
		public static final String TABLE_NAME = "messages";
		public static final String HASH_KEY_NAME = "timestamp";
		public static final String IMEI = "imeinumber";
		public static final String LATITUDE = "latitude";
		public static final String LONGITUDE = "longitude";
		public static final String DATE = "date";
		public static final String MESSAGE = "message";
		
		
		public static final String TABLE_NAME_USER = "userdetails";
		public static final String USERNAME = "username";
		public static final String EMAIL = "email";
		public static final String HASH_KEY_NAME_USER = "imeinumber";
		public static final String PASSWORD = "password";
		
		
	}
