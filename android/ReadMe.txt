MobileLostFound Mobile App:
--------------------------
How to run the code and install the application
------------------------------------------------
1) Download and install the Android SDK from the website: http://developer.android.com/sdk/index.html
2) If you have not downloaded SDK ADT bundle, install compatible eclipse IDE
3) Ensure you have the AWS account for DynamoDB. If not, create one and have a access to the tables in it.
3) Import the project into the eclipse.
4) Add the jars aws-android-sdk-1.5.0.jar for communicating with AWS dynamoDB into the build path
5) Add the jars, activation.jar, additionnal.jar, mail.jar. Theses are required for sending an email to the mobile owner using Gmail SMTP server.
6) After adding the above three jars to the build path, it is important to set the order of them. 
   All 3 jars must be placed on top of other jars(like android.jar, android-support-v4.jar, aws-android-sdk-1.5.0.jar etc)
7) For the first time, select the application and run as-> android application.
8) Connect the device into the laptop and run the code. Select the device as target in the Avd manager.
   To run the code, go to Run -> Run Configuration -> select the device from AVD manager.
9) Otherwise, the built .apk can be directly installed into the phone following the below steps :
   Press Win+R > cmd
   Navigate to platform-tools\ in the android-sdk folder 
   type adb
   Run the command: adb install example.apk

Testing:
-----------
1) Enable 4G and GPS in your mobile.
2) Click on the application icon from the Home Screen
3) Click on Notify
4) Enter the message and click on Send button

Verification:
-------------
1) You should receive an email(provided in Mobile Lost & Found web application, available in 'userdetails' table on signup) from mobile.lostfound@gmail.com.
2) The message with longitude, latitude, date-time and IMEI of the device will be sent to the 'messages' table in DynamoDB.
3) This can be viewed through Mobile Lost & Found web application with the google map.


