package com.digi.xbee.example;

//import com.digi.xbee.api.WiFiDevice;
import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.exceptions.XBeeException;
//import com.digi.xbee.api.models.XBeeProtocol;
import com.digi.xbee.api.models.XBeeMessage;
import com.digi.xbee.api.RemoteXBeeDevice;
//import com.digi.xbee.api.listeners.IDataReceiveListener;
import com.digi.xbee.api.models.XBee64BitAddress;

//import java.util.logging.Level;
//import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import jssc.SerialPort;
//import static jssc.SerialPort.MASK_RXCHAR;
//import jssc.SerialPortEvent;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class MainApp extends Application {
    private static final int BAUD_RATE = 9600;
    ObservableList<String> portList;
    Label labelValue;
    CheckBox checkBox1;
    CheckBox checkBox2;
    boolean wired = true;//defualt is wired system
    
    //wireless parameters 
    XBeeMessage xbeeMessage;
    XBeeDevice myXBeeDevice;
    RemoteXBeeDevice myRemoteDevice;
    
    //wired parameters
    SerialPort serialPort;
    byte[] serialData;
    int serialValue;

    //function to collect all ports name into portList
    private void detectPort(){
         
        portList = FXCollections.observableArrayList();
 
        String[] serialPortNames = SerialPortList.getPortNames();
        for(String name: serialPortNames){
            //System.out.println(name);
            portList.add(name);
        }
    }
    
    @Override
    public void start(Stage primaryStage) {
        
        
        checkBox1 = new CheckBox("Wired");
        checkBox2 = new CheckBox("Wireless");
        
        
        labelValue = new Label();
        labelValue.setFont(new Font("Arial", 150));
        
        //adding portlist to combobox 
        detectPort();
        final ComboBox comboBoxPorts = new ComboBox(portList);
        comboBoxPorts.valueProperty()
                .addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, 
                    String oldValue, String newValue) {
                disconnectArduino();
                System.out.println(newValue);
                connectArduino(newValue);
            }

        });
        
        
        //adding ports and labal to vbox pane 
        VBox vBox = new VBox();
        vBox.getChildren().addAll(
                comboBoxPorts, checkBox1, checkBox2, labelValue);
        
        //adding vbox to stackpane 
        StackPane root = new StackPane();
        root.getChildren().add(vBox);
        
        //setting the scene 
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Medical Data");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public void connectArduino(String port) {
        if (checkBox2.isSelected()) {
            wired = false;
        } else {
            wired = true;
        }
        
        
        if (wired){
            serialPort = new SerialPort(port);
            try{
                serialPort.openPort();
                serialPort.setParams(
                        SerialPort.BAUDRATE_9600,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
                
                serialData = serialPort.readBytes();
                //convert to int
                serialValue = serialData[0] & 0xff;

                //Update label in ui thread
                Platform.runLater(() -> {
                    labelValue.setText(String.valueOf(serialValue));  
                });
            } catch (SerialPortException ex) {
                System.out.println("SerialPortException: " + ex.toString());
            }
            
        } else {
            myXBeeDevice = new XBeeDevice(port, BAUD_RATE);
            try {
                myXBeeDevice.open();
                // Create a Remote XBee device 
                //Replace 0010013A20040XX in the next line with the MAC address of the remote XBee
                myRemoteDevice = new RemoteXBeeDevice (myXBeeDevice, new XBee64BitAddress("0010013A20040XX"));
                // Read data sent by remote Xbee device 
                xbeeMessage = myXBeeDevice.readDataFrom(myRemoteDevice);
                //Update label in ui thread
                Platform.runLater(() -> {
                    labelValue.setText(xbeeMessage.getDataString());
                });

            } catch(XBeeException e) {
                System.out.println(">> Error");
                System.exit(1);
            } 
        }
    }
    
    public void disconnectArduino() {
        System.out.println("disconnectArduino()");
        if (wired){
            try {
                if (serialPort != null)
                    serialPort.closePort();
            } catch (SerialPortException ex) {
                System.out.println("SerialPortException: " + ex.toString());
            }
        } else {
            if (myXBeeDevice != null)
                myXBeeDevice.close();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
