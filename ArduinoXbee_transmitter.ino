#include <SoftwareSerial.h>
#include <XBee.h>

SoftwareSerial XBee(2, 3);
int Pulse_Sensor = A2;//Pulse Senor input is pin A2
int Push_button = 26; //push button that starts data collection from pulse sensor is at pin 2
int Pulse_data;
Xbee xbee = XBee();

//This is the Arduino that transmits data. This arduino has a pulse sensor and switch.
//When the switch is pressed the ardunino collects data fom the pulse sensor.
//Then the data is tansmitted through wire to the receiving Arduino.


void setup()
{
  pinMode(Pulse_Sensor, OUTPUT);
  pinMode(Push_button, INPUT);
  xbee.begin(9600);
  Serial.begin(9600);
}

void loop()
{
  if (digitalRead(Push_button) == HIGH) { 
    Pulse_data = analogRead(Pulse_Sensor);
  }
  else {
    Pulse_data = 0;
  }
  xbee.write(Pulse_data);
  delay(200);
}
