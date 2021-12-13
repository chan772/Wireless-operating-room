package com.digi.xbee.example;

//import com.digi.xbee.api.WiFiDevice;
import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.AbstractXBeeDevice;
import com.digi.xbee.api.connection.serial.SerialPortParameters;
import com.digi.xbee.api.connection.serial.SerialPortRxTx;
import com.digi.xbee.api.listeners.IDataReceiveListener;
import com.digi.xbee.api.exceptions.XBeeException;
//import com.digi.xbee.api.models.XBeeProtocol;
import com.digi.xbee.api.models.XBeeMessage;
import com.digi.xbee.api.RemoteXBeeDevice;
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
    SerialPort[] serialPortNames;
    Label labelValue;
    RadioButton button1;
    RadioButton button2;
    int wired = 2;
    
    //wireless parameters 
    XBeeMessage xbeeMessage;
    XBeeDevice myXBeeDevice;
    SerialPortParameters portParams;
    RemoteXBeeDevice myRemoteDevice1;
    //chenge the MAC address in double quotes
    XBee64BitAddress address1 = new XBee64BitAddress("0013A20040939296");
    //RemoteXBeeDevice myRemoteDevice2;
    //XBee64BitAddress address2 = new XBee64BitAddress("0013A20040939296");
    //RemoteXBeeDevice myRemoteDevice3;
    //XBee64BitAddress address3 = new XBee64BitAddress("0013A20040939296");
    
    //wired parameters
    SerialPort serialport;
    byte[] serialData;
    int serialValue;

    //function to collect all ports name into portList
    private void detectPort(){
         
        portList = FXCollections.observableArrayList();
        serialPortNames = SerialPort.getCommPorts();
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
                disconnectArduino();
                System.out.println("Port Connection is " + newValue);
                if (tg.getSelectedToggle() == null)
                    System.out.println("Select a Connection: Wired or Wireless");
                tg.selectedToggleProperty()
                .addListener(new ChangeListener<Toggle>() {

                    @Override
                    public void changed(ObservableValue<? extends Toggle> ob, 
                           Toggle o, Toggle n) {
                        disconnectArduino();
                        RadioButton rb = (RadioButton)tg.getSelectedToggle();

                        if (rb == null) {
                             System.out.println("Select a Connection: Wired or Wireless");
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
        if (wired == 1){
            //System.out.println(SerialPort.getCommPort(port));
            serialport = SerialPort.getCommPort(port);
            serialport.openPort();
            serialport.setComPortParameters(BAUD_RATE, 8, 1, 0);
            serialport.addDataListener( new SerialPortDataListener() {
                
                @Override
                public void serialEvent(SerialPortEvent event) {
                    var bytes = event.getSerialPort().bytesAvailable();
                    //System.out.println(bytes);
                    byte[] buffer = new byte[bytes];
                    int x = serialport.readBytes(buffer, 1);
                    int value = buffer[0] & 0xff;    //convert to int
                    System.out.println(value);
                    Platform.runLater(() -> {
                        labelValue.setText(String.valueOf(value));
                    });
                }

                
                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
                }
            });
            //serialport.closePort();
        } else {
            System.out.println("here 9");
            portParams = new SerialPortParameters(BAUD_RATE, 8, 1, 0, 0);
            myXBeeDevice = new XBeeDevice(port, portParams);
            try {
                System.out.println("here 10");
                myXBeeDevice.open();
                System.out.println("here 11");
                // Create a Remote XBee device 
                myRemoteDevice1 = new RemoteXBeeDevice (myXBeeDevice, address1);
                //myRemoteDevice2 = new RemoteXBeeDevice (myXBeeDevice, address2);
                //myRemoteDevice3 = new RemoteXBeeDevice (myXBeeDevice, address3);
                // Read data sent by remote Xbee device 
                myXBeeDevice.addDataListener(new IDataReceiveListener(){
                    @Override
                    public void dataReceived(XBeeMessage xbeeMessage) {	
                        if (address1 == xbeeMessage.getDevice().get64BitAddress()) {
                            //Update label in ui thread
                            Platform.runLater(() -> {
                                labelValue.setText("The BPM is" + xbeeMessage.getDataString());
                            });
                        }
                    } 
                });
            } catch(XBeeException e) {
                System.out.println(">> Error");
                System.exit(1);
            }
        }
    }
    
    
    public void disconnectArduino() {
        //System.out.println("disconnectArduino()");
        if (wired == 1){
            if (serialport != null)
                serialport.closePort();
        } 
        if (wired == 0) {
            if (myXBeeDevice != null)
                myXBeeDevice.close();
        }
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
}
