# Android-Sensor-Monitor

*IN DEVELOPMENT!!!*

This Android application monitors accelerometer and gyroscope sensors to detect events e.g. door opening/closing, and someone  getting up (or sitting down) on the couch.  This is achieved by taping/attaching your phone to a door or a couch so that the on-board accelerometer and gyroscope could be utilized.  The detected event is then posted to a Tomcat/Servlet based web application using "Microsoft Access" for storage, displaying the event as well as the exact time when the event was detected.  Also, there is a web page that allows users to view these events in a descending temporal order i.e. most recent event on the top. 