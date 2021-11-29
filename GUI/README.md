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
12. Name the Library XBee. Then select ADD JAR/Folder. Navigate to the libs folder from step 7 and add the follwing JAR files to the library xbee-java-library-X.Y.Z.jar, rxtx-2.2.jar, slf4j-api-1.7.12.jar, slf4j-nop-1.7.12.jar, android-sdk-5.1.1.jar.
13. Similar to step 11 and 12, create a new library with the name JSSC and add the jar file jssc.jar from the file in step 10 -> jSSC-2.7.0-Release.
14. Similar to step 11 and 12, create a new library with the name Javafx and add the jar file 

uncompleted 
