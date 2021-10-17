#include <Wire.h>

int LED_Output = 9; //LED output is pin 9
int Data_State = 0;// State of the data


void setup() {
  pinMode(LED_Output, OUTPUT);
}

// the loop function runs over and over again forever
void loop() {

  Wire.begin(9); // join i2c bus (address optional for master) 
  Data_State = Wire.read(); // read data transmitted from transmitter arduinos
  Wire.endTransmission(); // stop transmitting

  //if data was received turn on the LED
  if (Data_State != 0) {
    digitalWrite(LED, HIGH);
    delay(200);// wait for a second
  } else {
    digitalWrite(LED, LOW);
    delay(200);
  }
  
}
