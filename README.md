# Android-Sensor-Monitor


This Android application monitors accelerometer and gyroscope sensors to detect events e.g. door opening/closing, and someone  getting up (or sitting down) on the couch.  This is achieved by taping/attaching your phone to a door or a couch so that the on-board accelerometer and gyroscope could be utilized.  The detected event is then posted to a Tomcat/Servlet based web application using "Microsoft Access" for storage, displaying the event as well as the exact time when the event was detected.  Also, there is a web page that allows users to view these events in a descending temporal order i.e. most recent event on the top. 

Dependencies:
- Tomcat 9.0
- JDBC Servlet
- ngrok tunnel

Ngrok: A simple tool that allows you to forward your server and port to a public address without port forwarding https://ngrok.com . I highly recommend using this as it easy, fast, and free.

Tomcat 9.0: You can install tomcat on mac or windows just remember that in order to see changes you need to restart the server everytime. Make sure you can do this easily during development. http://tomcat.apache.org

JDBC Servlet: Since we can't directly access the database with HTML, we are using java to run sql staments and java system out to run a mini HTML servlet. Please see my repository for my detail on that https://github.com/thekrisho/JDBC-Servlet
