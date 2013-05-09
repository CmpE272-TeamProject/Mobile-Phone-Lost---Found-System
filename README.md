Mobile-Phone-Lost---Found-System
================================

Team Project for CmpE-272 by Team-5

Steps to be included in the README file for running the Mobile Lost & Found web-application:

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

