#include <Wire.h>

int LED_Output = 28; //LED output is pin 28
int Data_State = 0;// State of the data

//This is the Arduino reciver. This arduino has a LED that
//turn on when data is received from the transmitter arduino


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
    digitalWrite(LED_Output, HIGH);
    delay(200);// wait for a second
  } else {
    digitalWrite(LED_Output, LOW);
    delay(200);
  }
  
}
