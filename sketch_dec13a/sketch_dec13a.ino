#include <SoftwareSerial.h>

SoftwareSerial mySerial(2, 3); // RX, TX
void setup() {
  Serial.begin(9600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for Native USB only
  }
    

  // set the data rate for the SoftwareSerial port
  mySerial.begin(9600);
  
 
}

void loop() {

  if (mySerial.available()){
    Serial.write(mySerial.read());
    Serial.flush();
  }


}
