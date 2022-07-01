void setup() {
  Serial.begin(9600);
  pinMode(13, OUTPUT);  
    digitalWrite(13, LOW);

}


void loop() {
  int value = analogRead(A0);
  const int seuil_value = 500;
  digitalWrite(13, LOW);

  Serial.println("Analog value : ");
  Serial.println(value);
  if (value<seuil_value) {
    digitalWrite(13, HIGH);
  } else {
    digitalWrite(13, LOW);
  }
    delay(250);

}
