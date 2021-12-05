package com.digi.xbee.example;

//import com.digi.xbee.api.WiFiDevice;
import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.exceptions.XBeeException;
//import com.digi.xbee.api.models.XBeeProtocol;
import com.digi.xbee.api.models.XBeeMessage;
import com.digi.xbee.api.RemoteXBeeDevice;
//import com.digi.xbee.api.listeners.IDataReceiveListener;
import com.digi.xbee.api.models.XBee64BitAddress;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Toggle;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

//import jssc.SerialPort;
//import static jssc.SerialPort.MASK_RXCHAR;
//import jssc.SerialPortEvent;
//import jssc.SerialPortException;
//import jssc.SerialPortList;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortDataListener;
//import java.io.InputStream;

public class MainApp extends Application {
    private static final int BAUD_RATE = 9600;
    ObservableList<String> portList;
    Label labelValue;
    RadioButton button1;
    RadioButton button2;
    int wired = 2;
    
    //wireless parameters 
    XBeeMessage xbeeMessage;
    XBeeDevice myXBeeDevice;
    RemoteXBeeDevice myRemoteDevice1;
    //RemoteXBeeDevice myRemoteDevice2;
    //RemoteXBeeDevice myRemoteDevice3;
    
    //wired parameters
    SerialPort serialport;
    byte[] serialData;
    int serialValue;

    //function to collect all ports name into portList
    private void detectPort(){
         
        portList = FXCollections.observableArrayList();
        SerialPort[] serialPortNames = SerialPort.getCommPorts();
        for (SerialPort serialPortName : serialPortNames) {
            portList.add(serialPortName.getSystemPortName());
        }
    }
    
    @Override
    public void start(Stage primaryStage) {
        ToggleGroup tg = new ToggleGroup();
  
        // create radiobuttons
        button1 = new RadioButton("Wired");
        button2 = new RadioButton("Wireless");
  
        // add radiobuttons to toggle group
        button1.setToggleGroup(tg);
        button2.setToggleGroup(tg);
        
        //Label for medical data
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
                System.out.println("Port Connection is " + newValue);
                if (tg.getSelectedToggle() == null)
                    System.out.println("Select a Connection; Wired or Wireless");
                tg.selectedToggleProperty()
                .addListener(new ChangeListener<Toggle>() {

                    @Override
                    public void changed(ObservableValue<? extends Toggle> ob, 
                           Toggle o, Toggle n) {

                        RadioButton rb = (RadioButton)tg.getSelectedToggle();

                        if (rb == null) {
                             System.out.println("Select a Connection; Wired or Wireless");
                        } else {
                            if (rb.getText().equals("Wired"))
                                wired = 1;
                            else 
                                wired = 0;
                            System.out.println(rb.getText() + " Connection"); 
                            connectArduino(newValue);
                        }  
                    }
                });
                if (wired < 2)
                    connectArduino(newValue);
            }
        });
        
        //adding ports, radiobutton, and labal to vbox pane 
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.getChildren().addAll(
                comboBoxPorts, button1, button2, labelValue);
        
        //adding vbox to stackpane 
        StackPane root = new StackPane();
        root.getChildren().add(vBox);
        
        //setting the scene 
        Scene scene = new Scene(root, 300, 400);
        
        primaryStage.setTitle("Medical Data");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public void connectArduino(String port) {
        System.out.println("here 1");
        System.out.println("here 2");
        if (wired == 1){
            //System.out.println(SerialPort.getCommPort(port));
            serialport = SerialPort.getCommPort(port);
            System.out.println("here 3");
            System.out.println("here 4");
            serialport.openPort();
            System.out.println("here 5");
            serialport.setComPortParameters(9600, 8, 1, 0);
            serialport.addDataListener( new SerialPortDataListener() {
                
                @Override
                public void serialEvent(SerialPortEvent event) {
                    var serialData1 = event.getSerialPort().bytesAvailable();
                    System.out.println(serialData1);
                    Platform.runLater(() -> {
                        labelValue.setText(String.valueOf(serialData1));
                    });
                }

                
                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
                }
            });
                
            //System.out.println("here 7");
            //serialport.closePort();
        } else {
            System.out.println("here 9");
            myXBeeDevice = new XBeeDevice(port, BAUD_RATE);
            try {
                System.out.println("here 10");
                myXBeeDevice.open();
                System.out.println("here 11");
                // Create a Remote XBee device 
                //Replace 0010013A20040XX in the next line with the MAC address of the remote XBee
                myRemoteDevice1 = new RemoteXBeeDevice (myXBeeDevice, new XBee64BitAddress("0010013A20040XX"));
                // Read data sent by remote Xbee device 
                xbeeMessage = myXBeeDevice.readDataFrom(myRemoteDevice1);
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
        if (wired == 1){
            if (serialport != null)
                serialport.closePort();
        } else {
            if (myXBeeDevice != null)
                myXBeeDevice.close();
        }
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
}
