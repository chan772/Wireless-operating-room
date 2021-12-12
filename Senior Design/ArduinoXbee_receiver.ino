#include <SoftwareSerial.h>

SoftwareSerial XBee(2, 3);
int LED_Output = 28; //LED output is pin 28
int Data_State = 0;// State of the data

//This is the Arduino reciver. This arduino has a LED that
//turn on when data is received from the transmitter arduino


void setup()
{
  pinMode(LED_Output, OUTPUT);
  XBee.begin(9600);
  Serial.begin(9600);
}

void loop()
{
  Data_State = XBee.read(); // read data transmitted from transmitter arduinos
  //if data was received turn on the LED
  if (Data_State != 0) {
    digitalWrite(LED_Output, HIGH);
    delay(200);// wait for a second
  } else {
    digitalWrite(LED_Output, LOW);
    delay(200);
  }
}
