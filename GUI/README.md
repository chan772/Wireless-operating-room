1. This file contains instructions on how to setup the GUI
2. Firstly download and install Netbeans from this link https://netbeans.apache.org//
3. Then create a project by following the instructions in the link below
https://www.digi.com/resources/documentation/digidocs/90001438/Default.htm#tasks/t_create_project_netbeans.htm?TocPath=Getting%2520started%2520with%2520XBee%2520Java%2520Library%257CBuild%2520your%2520first%2520XBee%2520Java%2520application%257CCreate%2520the%2520project%257C_____2
4. Download Javafx sdk for x64 architecture and the operating system of your PC from the link below https://gluonhq.com/products/javafx/
5. Downlad the zip files jSSC-2.70-Release.zip and XBJL-1.2.1.zip in this folder 
6. unzip XBJL-1.2.1.zip
7. Create a folder called libs in the root file of the project.
8. Copy xbee-java-library-X.Y.Z.jar and the contents of the folder called extra-libs from the unziped file in step 6 into the newly created libs folder
9. Create a folder called javaFx in the root file of the project and unzip the contents of the downloaded javafx from step 4
10. Create a folder called jSSC in the root file of the project and unzip the contents of the downloaded jSSC-2.70-Release.zip file from step 5
11. In Netbeans go to Tools-> Libariries ->New Libary
12. Name the Library XBee. Then select ADD JAR/Folder. Navigate to the libs folder from step 7 and add the follwing JAR files to the library xbee-java-library-X.Y.Z.jar, rxtx-2.2.jar, slf4j-api-1.7.12.jar, slf4j-nop-1.7.12.jar, android-sdk-5.1.1.jar, android-sdk-addon-3.jar.
13. Similar to step 11 and 12, create a new library with the name JSSC and add the jar file jssc.jar from the file in (step 10 -> jSSC-2.7.0-Release) to the classpath and sources. 
14. Similar to step 11 and 12, create a new library with the name Javafx and add the jar files in the file in (step 9-> javafx-sdk-17.0.1->lib) to the classpath. CLick on Add JAR/Folder again in the classpath then add the bin file from the file in (step 9 ->javafx-sdk-17.0.1). Click on Sources in Netbeans and add JAR/FOLDER to add src.zip from the file in (step 9 ->javafx-sdk-17.0.1).
15. Then click ok
16. In Netbeans on the left, right click on myFirstXBeeApp project and select properties. Then go to Libraries. In the classpath click on the add icon the select Add Library. Then add the 3 libraries(XBee, JavaFx, JSSC) you created earlier. Still in the Libraries tab go to Run. Then in the Modulepath add the Xbee and javaFx library
17. Go to the link below and follow steps 1 - 5. 
https://www.digi.com/resources/documentation/digidocs/90001438/Default.htm#tasks/t_add_application_source_code_netbeans.htm?TocPath=Getting%2520started%2520with%2520XBee%2520Java%2520Library%257CBuild%2520your%2520first%2520XBee%2520Java%2520application%257CAdd%2520the%2520application%2520source%2520code%257C_____2
18. Go to the file in (step 9-> javafx-sdk-17.0.1->lib) then copy the address of the lib file as text
19. For Windows OS go to the file in (step 7-> native -> Windows -> win64) then copy the address of the win64 file as text. For MAC OS go to the file in (step 7-> native -> Mac_OS_X) then copy the address of the Mac_OS_X file as text
20. In Netbeans on the left right click on myFirstXBeeApp project and select properties. Then go to Run. In the VM options section add the following: --module-path "address copied from step 18" --add-modules=javafx.controls,javafx.fxml  --module-path "address copied from step 19"
21. In the Main class section add the following: com.digi.xbee.example.MainApp
21. then click ok 
23. Download and install scene builder from this link https://gluonhq.com/products/scene-builder/
24. In Netbeans go to Tools-> options. Then follow the image below. then click ok 
25. ![image](https://user-images.githubusercontent.com/92693957/144100375-47694a87-cb67-4fb2-babc-c58bdc468681.png)
26. In Netbeans on th left exand you project myFirstXBeeApp -> Source Packages -> com.digi.xbee.example
27. Then remove what is in MainApp.java then copy the contents of MainApp.java in this github and paste it into your Netbeans MainApp.java.
28. Save the content of your file then click on Run (a play icon on the top bar).
