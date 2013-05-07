package test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.model.TableStatus;

/*
 * We are creating dynamo db tables to store all logins, userdetails, messages.
 *
 * Tables:
 *      1. login => username(key), password
 *      2. userdetails => username(key),imeinumber,email
 *      3. message => timestamp, imeinumber, messagedetails 
 *
 */
public class TestDynamo 
{
    public static AmazonDynamoDBClient amazonDynamoDBClient = null;
    
    public static final String s_StringDateFormat = "yyyy-MM-dd HH:mm:ss";
            
    public static void main(String[] args) 
    {
//        String accessKey = "AKIAJZ5EU4EXKKBFX5JQ";
//        String secretKey = "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";
        
        String accessKey = "AKIAITJYQ3BRQ2BCROJA";
        String secretKey = "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";
        AWSCredentials awsCredential = new BasicAWSCredentials(accessKey, secretKey);
        
        amazonDynamoDBClient = new AmazonDynamoDBClient(awsCredential);
        
//        // Create Tables
//        createUserDetailsTable();
//        createMessagesTable();
        
     // Scan items from login table
//      HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
//      Condition condition = new Condition()
//          .withComparisonOperator(ComparisonOperator.GT.toString())
//          .withAttributeValueList(new AttributeValue().withN("1985"));
//      scanFilter.put("year", condition);
//      ScanRequest scanRequest = new ScanRequest(tableName).withScanFilter(scanFilter);

      ScanRequest scanRequest = new ScanRequest("userdetails");
      ScanResult scanResult = amazonDynamoDBClient.scan(scanRequest);
      List<Map<String, AttributeValue>> tableData = scanResult.getItems();
      for(Map<String, AttributeValue> rowData : tableData)
      {
          AttributeValue attributeValueUserName = rowData.get("username");
          AttributeValue attributeValuePwd = rowData.get("password");
          AttributeValue attributeValueMobile = rowData.get("imeinumber");
          AttributeValue attributeValueEmail = rowData.get("email");
          
          System.out.println("[username, password, imeinumber, email]=>["+attributeValueUserName.getS()
             +", "+attributeValuePwd.getS()+", "+", "+attributeValueMobile.getS()+", "+
                     attributeValueEmail.getS()+"]");
      }
      
      scanRequest = new ScanRequest("messages");
      scanResult = amazonDynamoDBClient.scan(scanRequest);
      tableData = scanResult.getItems();
      
      DescComparator descComparator = new DescComparator();
      TreeMap<Long, String> treeMap = new TreeMap<Long, String>(descComparator);
      for(Map<String, AttributeValue> rowData : tableData)
      {
          AttributeValue attributeValueTimestamp = rowData.get("timestamp");
          AttributeValue attributeValueDate = rowData.get("date");
          AttributeValue attributeValueMobile = rowData.get("imeinumber");
          AttributeValue attributeValueMessage = rowData.get("message");
          AttributeValue attributeValuelatitude = rowData.get("latitude");
          AttributeValue attributeValuelongitude = rowData.get("longitude");
          String value = attributeValueDate.getS()+","+attributeValueMobile.getS()+
                  ","+attributeValueMessage.getS()+","+attributeValuelatitude.getS()+
                  ","+attributeValuelongitude.getS();
          // add values so that it is sorted
          treeMap.put(Long.parseLong(attributeValueTimestamp.getN()), value);
      }
      
      for (Entry<Long, String> entry : treeMap.entrySet())
      {
          System.out.println("[timestamp, date, imeinumber, message]=>["+entry.getKey()
                  +", "+entry.getValue()+"]");
      }
      
      // scanning messages based on imeinumber
      String mobileNo = "1111111111";
      HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
      Condition condition = new Condition()
          .withComparisonOperator(ComparisonOperator.EQ.toString())
          .withAttributeValueList(new AttributeValue().withS(mobileNo));
      scanFilter.put("imeinumber", condition);
      scanRequest = new ScanRequest("messages").withScanFilter(scanFilter);
      scanResult = amazonDynamoDBClient.scan(scanRequest);
      tableData = scanResult.getItems();
      
      descComparator = new DescComparator();
      treeMap = new TreeMap<Long, String>(descComparator);
      for(Map<String, AttributeValue> rowData : tableData)
      {
          AttributeValue attributeValueTimestamp = rowData.get("timestamp");
          AttributeValue attributeValueDate = rowData.get("date");
          AttributeValue attributeValueMobile = rowData.get("imeinumber");
          AttributeValue attributeValueMessage = rowData.get("message");
          AttributeValue attributeValuelatitude = rowData.get("latitude");
          AttributeValue attributeValuelongitude = rowData.get("longitude");
          
          String value = attributeValueDate.getS()+","+attributeValueMobile.getS()+
                  ","+attributeValueMessage.getS()+","+attributeValuelatitude.getS()+
                  ","+attributeValuelongitude.getS();
          
          // add values so that it is sorted
          treeMap.put(Long.parseLong(attributeValueTimestamp.getN()), value);
      }
      
      for (Entry<Long, String> entry : treeMap.entrySet())
      {
          System.out.println("[timestamp, date, imeinumber, message]=>["+entry.getKey()
                  +", "+entry.getValue()+"]");
      }
      
      
//        String tableName = "Test";
//        
//        // Create Table request
//        KeySchemaElement keySchemaElement = new KeySchemaElement();
//        keySchemaElement.setAttributeName("name");
//        keySchemaElement.setKeyType(KeyType.HASH);
//        
////        CreateTableRequest createTableRequest = new CreateTableRequest("R", keySchema)
//        
//        // ProvisionedThroughput as per the free limit
//        ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput();
//        provisionedThroughput.setReadCapacityUnits(1L);
//        provisionedThroughput.setWriteCapacityUnits(1L);
//        
//        List<KeySchemaElement> keySchemaElements = new ArrayList<KeySchemaElement>();
//        keySchemaElements.add(keySchemaElement);
//        
//        List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
//        AttributeDefinition attributeDefinition = new AttributeDefinition();
//        attributeDefinition.setAttributeName("name");
//        attributeDefinition.setAttributeType(ScalarAttributeType.S);
//        attributeDefinitions.add(attributeDefinition);
//        
//        // Create a table with a primary key named 'name', which holds a string
//        CreateTableRequest createTableRequest = new CreateTableRequest();
//        createTableRequest.setTableName("Test"); createTableRequest.setKeySchema(keySchemaElements);
//        createTableRequest.setProvisionedThroughput(provisionedThroughput);
//        createTableRequest.setAttributeDefinitions(attributeDefinitions);
//        
//        TableDescription createdTableDescription = amazonDynamoDBClient.createTable(createTableRequest).getTableDescription();
//        System.out.println("Created Table: " + createdTableDescription);
//        
//        // Wait for it to become active
//        waitForTableToBecomeAvailable(tableName);
//
//        // Describe our new table
//        DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
//        TableDescription tableDescription = amazonDynamoDBClient.describeTable(describeTableRequest).getTable();
//        System.out.println("Table Description: " + tableDescription);
//
//        // Add an item
//        Map<String, AttributeValue> item = newItem("Bill & Ted's Excellent Adventure", 1989, "****", "James", "Sara");
//        PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
//        PutItemResult putItemResult = amazonDynamoDBClient.putItem(putItemRequest);
//        System.out.println("Result: " + putItemResult);
//
//        // Add another item
//        item = newItem("Airplane", 1980, "*****", "James", "Billy Bob");
//        putItemRequest = new PutItemRequest(tableName, item);
//        putItemResult = amazonDynamoDBClient.putItem(putItemRequest);
//        System.out.println("Result: " + putItemResult);

        // Scan items for movies with a year attribute greater than 1985
//        HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
//        Condition condition = new Condition()
//            .withComparisonOperator(ComparisonOperator.GT.toString())
//            .withAttributeValueList(new AttributeValue().withN("1985"));
//        scanFilter.put("year", condition);
//        ScanRequest scanRequest = new ScanRequest(tableName).withScanFilter(scanFilter);
//        ScanResult scanResult = amazonDynamoDBClient.scan(scanRequest);
//        System.out.println("Result: " + scanResult);
    }

    private static Map<String, AttributeValue> newItem(String name, int year, String rating, String... fans) {
        Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("name", new AttributeValue(name));
        item.put("year", new AttributeValue().withN(Integer.toString(year)));
        item.put("rating", new AttributeValue(rating));
        item.put("fans", new AttributeValue().withSS(fans));

        return item;
    }
    
    private static void waitForTableToBecomeAvailable(String tableName) {
        System.out.println("Waiting for " + tableName + " to become ACTIVE...");

        long startTime = System.currentTimeMillis();
        long endTime = startTime + (10 * 60 * 1000);
        while (System.currentTimeMillis() < endTime) {
            try {Thread.sleep(1000 * 20);} catch (Exception e) {}
            try {
                DescribeTableRequest request = new DescribeTableRequest().withTableName(tableName);
                TableDescription tableDescription = amazonDynamoDBClient.describeTable(request).getTable();
                String tableStatus = tableDescription.getTableStatus();
                System.out.println("  - current state: " + tableStatus);
                if (tableStatus.equals(TableStatus.ACTIVE.toString())) return;
            } catch (AmazonServiceException ase) {
                if (ase.getErrorCode().equalsIgnoreCase("ResourceNotFoundException") == false) throw ase;
            }
        }

        throw new RuntimeException("Table " + tableName + " never went active");
    }
    
    /**
     * Current date in string format 
     * @return
     */
    public static String now() 
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(s_StringDateFormat);
        return sdf.format(cal.getTime());
    }
    
    /**
     * This is a test method to create the userdetails table and insert data for testing purposes
     * 
     *      Table Name : userdetails
     *          Column : username :: String ::: key
     *          Column : password :: String 
     *          Column : imeinumber :: String
     *          Column : email :: String
     */
    public static void createUserDetailsTable()
    {
        String tableName = "userdetails";
        createTable(tableName, "username", ScalarAttributeType.S);
        
        // Add an item
        Map<String, AttributeValue> item = newUserDetailsItem("cmpe", "cmpe", "1111111111", "cmpe@272.com");
        PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
        PutItemResult putItemResult = amazonDynamoDBClient.putItem(putItemRequest);
        System.out.println("Result: " + putItemResult);
        
        item = newUserDetailsItem("cmpe1", "cmpe1", "22222222222", "cmpe1@272.com");
        putItemRequest = new PutItemRequest(tableName, item);
        putItemResult = amazonDynamoDBClient.putItem(putItemRequest);
        System.out.println("Result: " + putItemResult);
    }
    private static Map<String, AttributeValue> newUserDetailsItem(String username, String password, String imeinumber, String email) {
        Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("username", new AttributeValue(username));
        item.put("password", new AttributeValue(password));
        item.put("imeinumber", new AttributeValue(imeinumber));
        item.put("email", new AttributeValue(email));

        return item;
    }
    
    /**
     * This is a test method to create the messages table and insert data for testing purposes
     * 
     *      Table Name : messages
     *          Column : timestamp :: Number ::: key
     *          Column : dateadded :: String (Show date on the ui)
     *          Column : imeinumber :: String
     *          Column : message :: String
     *          Column : latitude :: String
     *          Column : longitude :: String
     */
    public static void createMessagesTable()
    {
        String tableName = "messages";
        
        createTable(tableName, "timestamp", ScalarAttributeType.N);
        
        // Add an item
        Map<String, AttributeValue> item = newMessagesItem(System.currentTimeMillis(), now(), "1111111111", "I found your phone","37.3318","-122.0312");
        PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
        PutItemResult putItemResult = amazonDynamoDBClient.putItem(putItemRequest);
        System.out.println("Result: " + putItemResult);
        
        item = newMessagesItem(System.currentTimeMillis(), now(), "1111111111", "Take your phone","47.3318","-122.0312");
        putItemRequest = new PutItemRequest(tableName, item);
        putItemResult = amazonDynamoDBClient.putItem(putItemRequest);
        System.out.println("Result: " + putItemResult);
        
        item = newMessagesItem(System.currentTimeMillis(), now(), "22222222222", "Pick your phone","48.3318","-122.0312");
        putItemRequest = new PutItemRequest(tableName, item);
        putItemResult = amazonDynamoDBClient.putItem(putItemRequest);
        System.out.println("Result: " + putItemResult);
    }
    private static Map<String, AttributeValue> newMessagesItem(Long timestamp, String date, String imeinumber, String message, String latitude, String longitude) {
        Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("timestamp", new AttributeValue().withN(Long.toString(timestamp)));
        item.put("date", new AttributeValue(date));
        item.put("imeinumber", new AttributeValue(imeinumber));
        item.put("message", new AttributeValue(message));
        item.put("latitude", new AttributeValue(latitude));
        item.put("longitude", new AttributeValue(longitude));

        return item;
    }
    
    /**
     * Create table
     * @param tableName
     * @param keyElementName
     */
    public static void createTable(String tableName, String keyElementName, ScalarAttributeType typeOfKey)
    {
        // Create Table request
        KeySchemaElement keySchemaElement = new KeySchemaElement();
        keySchemaElement.setAttributeName(keyElementName);
        keySchemaElement.setKeyType(KeyType.HASH);
        
        // ProvisionedThroughput as per the free limit
        ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput();
        provisionedThroughput.setReadCapacityUnits(1L);
        provisionedThroughput.setWriteCapacityUnits(1L);
        
        List<KeySchemaElement> keySchemaElements = new ArrayList<KeySchemaElement>();
        keySchemaElements.add(keySchemaElement);
        
        List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
        AttributeDefinition attributeDefinition = new AttributeDefinition();
        attributeDefinition.setAttributeName(keyElementName);
        attributeDefinition.setAttributeType(typeOfKey);
        attributeDefinitions.add(attributeDefinition);
        
        // Create a table with a primary key named 'name', which holds a string
        CreateTableRequest createTableRequest = new CreateTableRequest();
        createTableRequest.setTableName(tableName); 
        createTableRequest.setKeySchema(keySchemaElements);
        createTableRequest.setProvisionedThroughput(provisionedThroughput);
        createTableRequest.setAttributeDefinitions(attributeDefinitions);
        
        TableDescription createdTableDescription = amazonDynamoDBClient.createTable(createTableRequest).getTableDescription();
        System.out.println("Created Table: " + createdTableDescription);
        
        // Wait for it to become active
        waitForTableToBecomeAvailable(tableName);

        // Describe our new table
        DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
        TableDescription tableDescription = amazonDynamoDBClient.describeTable(describeTableRequest).getTable();
        System.out.println("Table Description: " + tableDescription);
        
    }
    
    
    public static class DescComparator implements Comparator<Long> {
        @Override
        public int compare(Long l1, Long l2) {
            int compare = (int) Math.signum(l1.compareTo(l2));
            return compare * (-1);
        }
    }
}
