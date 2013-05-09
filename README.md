Mobile-Phone-Lost---Found-System
================================

Team Project for CmpE-272 by Team-5

Abstract - “Help, I’ve lost my phone!” is an all-inclusive packaged solution to reconnect weary owners with their prized mobile possessions when they seemingly disappear on their own.  The solution incorporates several different functionalities such as geolocation tracking and message sending to accomplish this task.  The first step of getting a lost device back to its owner is opening and maintaining a communication channel between the owner and the missing/stolen mobile device.  This is accomplished by the message sending/receiving feature.  The message sending feature is a two way system that allows a message to be sent from a computer terminal and received by a mobile device.  Conversely, the system allows a message to be sent from a mobile device and received by a computer terminal.  The second step is locating the physical location of the missing device and is accomplished by the geolocation tracking feature.  The geolocation tracking feature allows the owner to pinpoint where the current location of the mobile device is.  With the implementation of both communication and tracking means our application can thus serve as an effective means to locate and return missing mobile devices.

Steps to be included in the README file for running the Mobile Lost & Found web-application:

This documentation is divided into four main parts: Browser Install, Mobile(Android) Install, Phone Settings and
Sample Run.
Browser Install consists of steps needed to run the application on the browser side.
Mobile Install consists of steps needed to run the application on an android enabled mobile device.
Both Installs are needed to get the application working.

Browser Install
1.	Install tomcat.
2.	In the build.properties file change the path of appserver.home to the directory where your tomacat is install.
appserver.home=${user.home}/<path to tomcat directory>

For example if you installed the file in your home directory
appserver.home=${user. Home}/apache-tomcat-7.0.39

3.	Go to ${user. Home}/<path to tomcat directory>/bin and start the tomcat by giving the following command: 
./start.sh
4.	Unzip the springapp.zip file.
5.	In the command prompt, go to the path where the application folder “springapp” is saved.
6.	Then type one of the following commands: 
    a.)	ant build deploy start  // If deploying the app for the first time
    b.)	ant build undeploy deploy start // If not deploying for the first time.
7.	After getting the build successful message, open a browser and type the following to launch the web application:
http://localhost:8080/springapp/login.htm
8. Ensure that the database services in the DynamoDb account for the application are up and running, and the userdetails and messages tables are available which are required for using the Mobile Lost & Found application.

Mobile Install

1. Download and install the Android SDK from the website: http://developer.android.com/sdk/index.html
2. If you have not downloaded SDK ADT bundle, install compatible eclipse IDE
3. Ensure you have the AWS account for DynamoDB. If not, create one and have a access to the tables in it.
4. Import the project into the eclipse.
5. Add the jars aws-android-sdk-1.5.0.jar for communicating with AWS dynamoDB into the build path.
6. Add the jars, activation.jar, additionnal.jar, mail.jar. Theses are required for sending an email to the mobile owner using Gmail SMTP server.
7. After adding the above three jars to the build path, it is important to set the order of them. 
   All 3 jars must be placed on top of other jars(like android.jar, android-support-v4.jar, aws-android-sdk-1.5.0.jar etc)
8. For the first time, select the application and run as-> android application.
9. Connect the device into the laptop and run the code. Select the device as target in the Avd manager.
   To run the code, go to Run -> Run Configuration -> select the device from AVD manager.
10. Otherwise, the built .apk can be directly installed into the phone following the below steps :
   Press Win+R > cmd
   Navigate to platform-tools\ in the android-sdk folder 
   type adb
   Run the command: adb install example.apk

Mobile Device Settings
1) Enable 4G and GPS in your mobile.
2) Click on the application icon from the Home Screen.
3) Click on Notify.
4) Enter the message and click on Send button.

Sample Run
1) You should receive an email(provided in Mobile Lost & Found web application, available in 'userdetails' table on signup) from mobile.lostfound@gmail.com.
2) The message with longitude, latitude, date-time and IMEI of the device will be sent to the 'messages' table in DynamoDB.
3) This can be viewed through Mobile Lost & Found web application with the google map.

Cheers!
