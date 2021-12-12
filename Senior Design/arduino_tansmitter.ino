#include <Wire.h>

int Pulse_Sensor = A2;//Pulse Senor input is pin A2
int Aduino_receiver = 9; //the receiver arduino is connected to pin 9
int Push_button = 26; //push button that starts data collection from pulse sensor is at pin 26
int Pulse_data;

//This is the Arduino that transmits data. This arduino has a pulse sensor and switch.
//When the switch is pressed the ardunino collects data fom the pulse sensor.
//Then the data is tansmitted through wire to the receiving Arduino.

void setup() {
  pinMode(Pulse_Sensor, OUTPUT);
  pinMode(Push_button, INPUT);
  Wire.begin(); // join i2c bus (address optional for master)
  Serial.begin(9600); // start serial for output
}

// the loop function runs over and over again forever
void loop() {

  //if push button is pressed get pulse sensor data
  if (digitalRead(Push_button) == HIGH) {
    Pulse_data = analogRead(Pulse_Sensor);
  } else {
    Pulse_data = 0;
  }

  Wire.beginTransmission(Aduino_receiver);// transmit pulse sensor data to device #9
  Wire.write(Pulse_data);// sends Pulse_data
  Wire.endTransmission(); // stop transmitting
  delay(200);
}
