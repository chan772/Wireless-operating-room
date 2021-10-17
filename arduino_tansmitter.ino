#include <Wire.h>

int Pulse_Sensor = 5;//Pulse Senor input is pin 5
int Aduino_receiver = 9; //the receiver arduino is connected to pin 9
const int Push_button = 2; //push button that starts data collection from pulse sensor is at pin 2
int Pulse_data;



void setup() {
  pinMode(Pulse_Sensor, INPUT);
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
