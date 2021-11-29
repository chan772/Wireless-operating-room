package com.digi.xbee.example;

//import com.digi.xbee.api.WiFiDevice;
import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.exceptions.XBeeException;
//import com.digi.xbee.api.models.XBeeProtocol;
import com.digi.xbee.api.models.XBeeMessage;
//import com.digi.xbee.api.RemoteXBeeDevice;
import com.digi.xbee.api.listeners.IDataReceiveListener;

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
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

//import jssc.SerialPort;
//import static jssc.SerialPort.MASK_RXCHAR;
//import jssc.SerialPortEvent;
//import jssc.SerialPortException;
import jssc.SerialPortList;

public class MainApp extends Application {
    private static final int BAUD_RATE = 9600;
    ObservableList<String> portList;
    Label labelValue;

    //function to collect all ports name into portList
    private void detectPort(){
         
        portList = FXCollections.observableArrayList();
 
        String[] serialPortNames = SerialPortList.getPortNames();
        for(String name: serialPortNames){
            System.out.println(name);
            portList.add(name);
        }
    }
    
    @Override
    public void start(Stage primaryStage) {
        
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

                System.out.println(newValue);
                connectArduino(newValue);
            }

        });
        
        //adding ports and labal to vbox pane 
        VBox vBox = new VBox();
        vBox.getChildren().addAll(
                comboBoxPorts, labelValue);
        
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
        XBeeDevice myXBeeDevice = new XBeeDevice(port, BAUD_RATE);
        try {
            myXBeeDevice.open();
            // Create the data reception listener.
            MyDataReceiveListener myDataReceiveListener = new MyDataReceiveListener(); ;
            // Subscribe to data reception.
            myXBeeDevice.addDataListener(myDataReceiveListener);
        } catch(XBeeException e) {
            System.out.println(">> Error");
            System.exit(1);
        } 
   
    }
    
    public class MyDataReceiveListener implements IDataReceiveListener {
	/*
	* Data reception callback.
	*/
	@Override
	public void dataReceived(XBeeMessage xbeeMessage) {	
		String address = xbeeMessage.getDevice().get64BitAddress().toString();
		String dataString = xbeeMessage.getDataString();
		System.out.println("Received data from " + address +
				": " + dataString);
                //Update label in ui thread
                Platform.runLater(() -> {
                    labelValue.setText(dataString);  
                });
	}
}



    public static void main(String[] args) {
        launch(args);
    }
}
