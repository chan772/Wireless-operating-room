import controlP5.*; //import ControlP5 library
import processing.serial.*;

Serial port;

ControlP5 cp5; //create ControlP5 object
PFont font;
PFont font2;
int myBPM = -1;
byte[] bpm;
ScrollableList lstPortList;

void setup(){ //same as arduino program

  size(700, 700);    //window size, (width, height)

   String[] serialPortNames = Serial.list();
   printArray(serialPortNames);   //prints all available serial ports
   
   cp5 = new ControlP5(this);
   lstPortList = cp5.addScrollableList("COM Port")
        .setPosition(20, 20)
        .setSize(200, 200)
        .setBarHeight(40)
        .setItemHeight(40)
        .addItems(serialPortNames)
        .setFont(createFont("Arial", 35))
        .setColorForeground(color(40, 128))
        .setValue(0);
  port = new Serial(this,lstPortList.getCaptionLabel().getText(), 9600);  //i have connected arduino to the chose com
  
  font = createFont(PFont.list()[2], 35);//custom font with 3rd font available and size 35
  font2 = createFont(PFont.list()[0], 50);//custom font with 1st font available and size 50
    
}

void draw(){  //same as loop in arduino
  
   background(150, 0 , 150); // background color of window (r, g, b) or (0 to 255)
   //lets give title to our window
   fill(0, 255, 0);               //text color (r, g, b)
   textFont(font2);
   text("Medical Data", 280, 80);  // ("text", x coordinate, y coordinate)
   //text("     ", 100, 150);
   textFont(font2);
   text("The BPM is:" + myBPM, 280, 300);
}


//When you press the start button, it gets the BPM from the serial port

void serialEvent(Serial myPort) {
  bpm = myPort.readBytes();
  myBPM = bpm[0] & 0xff;  
}
