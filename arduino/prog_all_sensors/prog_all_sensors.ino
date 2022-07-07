
#include "DHT.h"

int rainPin = A0;
int lightPin = A5;

// you can adjust the threshold value
int thresholdValue = 600;
const int light_seuil = 500;

DHT dht(2, DHT11);

void setup() {
   dht.begin();
   pinMode(rainPin, INPUT);
   pinMode(lightPin, INPUT);
   Serial.begin(9600);
}


void loop() {
  float h = dht.readHumidity();
  float t = dht.readTemperature();

  int sensorValue = analogRead(rainPin);
  int lightValue = analogRead(lightPin);
   
  if (h>50){  
   // température
   Serial.print(t);
   Serial.print(" - ");
   Serial.print(lightValue);
   Serial.print(" - ");

   // humidité
   Serial.print(h);
   Serial.print(" - ");

    if(sensorValue <= thresholdValue){
      // pluie détectée
      Serial.print(sensorValue);
      Serial.print(" - ");
    } else {
      // pas de pluie
      Serial.print(sensorValue);
      Serial.print(" - ");
    }
  } else {
    Serial.print(t);
    Serial.print(" - ");
    Serial.print(lightValue);
    Serial.print(" - ");
    Serial.print(h);
    Serial.print(" - ");

    if(sensorValue <= thresholdValue){
      Serial.print(sensorValue);
     }
    else {
      Serial.print(sensorValue);
    }
  }
 
  // read the input on analog pin 0:
    Serial.println("");

// délai de refresh
  delay(1000);
}
