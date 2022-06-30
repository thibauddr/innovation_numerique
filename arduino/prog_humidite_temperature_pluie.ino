
#include "DHT.h"

int rainPin = A0;
int greenLED = 6;
int redLED = 7;
// you can adjust the threshold value
int thresholdValue = 600;





DHT dht(2, DHT11);

void setup() {
   dht.begin();
   Serial.begin(9600);
   pinMode(rainPin, INPUT);
   pinMode(greenLED, OUTPUT);
   pinMode(redLED, OUTPUT);
   digitalWrite(greenLED, LOW);
   digitalWrite(redLED, LOW);
   Serial.begin(9600);
}



void loop() {
  float h = dht.readHumidity();
   float t = dht.readTemperature();

  int sensorValue = analogRead(rainPin);


   
  if (h>50){
   Serial.print("Humidity: ");
   Serial.print(h);
   Serial.println(" : temps humide ");
   Serial.print("Temperature: ");
   Serial.println(t);

    if(sensorValue <= thresholdValue){
      Serial.print(sensorValue);
      Serial.println(" - Il pleut");
      digitalWrite(greenLED, LOW);
      digitalWrite(redLED, HIGH);
  }
    else {
      Serial.print(sensorValue);
     Serial.println(" - c'est sec");
     digitalWrite(greenLED, HIGH);
     digitalWrite(redLED, LOW);
    }
  }
  else{
   Serial.print("Humidity: ");
   Serial.print(h);
   Serial.println(" : temps sec ");
   Serial.print("Temperature: ");
   Serial.println(t);
    if(sensorValue <= thresholdValue){
      Serial.print(sensorValue);
      Serial.println(" - Il pleut");
      digitalWrite(greenLED, LOW);
      digitalWrite(redLED, HIGH);
     }
    else {
      Serial.print(sensorValue);
      Serial.println(" - c'est sec");
      digitalWrite(greenLED, HIGH);
      digitalWrite(redLED, LOW);
    }
     
    }
 
  // read the input on analog pin 0:
    Serial.println("");
    Serial.println("");  

  delay(500);
}
