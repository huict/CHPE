// Receiver
#include <ArduinoBLE.h>

BLEDevice peripheral;

BLECharacteristic fingers[5];
char fingers_uuids[5][37] = {
  "d2be0238-e8d7-4b2f-9a2a-0063c1f1f456",
  "c00552e0-d880-4c7d-8baf-93356a8ba835",
  "20ac424f-de57-4c34-b186-920c27b117c9",
  "2ed22785-bc51-442a-8f29-2d2ea7a40e9f",
  "52155daf-df88-4990-bc5c-125e5cbd759b"
};

void setup() {
  // Start terminal
  Serial.begin(9600);
  while(!Serial);
  BLE.begin();
  Serial.println("Start scanning");
  // Start scan for peripheral service
  BLE.scanForUuid("bd3d409d-f8a3-4c80-b8db-daea6ddabec3");

  // check if a peripheral has been discovered
  bool glove_connected = false;
  while(!glove_connected)
  {
    peripheral = BLE.available();
    Serial.println("BLE Available");
    if (peripheral) {
      // Check for correct device discovered
      if (peripheral.localName() == "GLOVE") {
        glove_connected = true;
        Serial.println("Found Glove");
        BLE.stopScan();
        if (peripheral.connect()) {
          Serial.println("Connected");
        }
        if(peripheral.discoverAttributes()){
          fingers[0] = peripheral.characteristic("d2be0238-e8d7-4b2f-9a2a-0063c1f1f456");
          fingers[1] = peripheral.characteristic("c00552e0-d880-4c7d-8baf-93356a8ba835");
          fingers[2] = peripheral.characteristic("20ac424f-de57-4c34-b186-920c27b117c9");
          fingers[3] = peripheral.characteristic("2ed22785-bc51-442a-8f29-2d2ea7a40e9f");
          fingers[4] = peripheral.characteristic("52155daf-df88-4990-bc5c-125e5cbd759b");
          for(unsigned int i = 0; i < 5; i++){
            Serial.println("Finger : "+String(i));
            if(!fingers[i]){
              Serial.println("NOT Accessable");
            }
            if(fingers[i].canSubscribe()){
              fingers[i].subscribe();
            } else {
              Serial.println("NOT Subscribable");
            }
          }
        }
      }
    }
  }
  // Check for discovered device
  Serial.println("Begin loop!");
}

void loop() {
  wait_us(1'000'000);
  if (peripheral.connected()) {
    //Check for updated values
    Serial.println("=======================");
    for(unsigned int i = 0; i < 5; i++){
      if (fingers[i].read()){
        Serial.println("finger : "+String(i));
        unsigned char fingerVal;
        fingers[i].readValue(fingerVal);
        Serial.print("LED_value: ");
        Serial.println(char(fingerVal));
        Serial.println("");
      }
    }
  }
}