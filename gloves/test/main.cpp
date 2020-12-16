#include <ArduinoBLE.h>

BLEService testService("bd3d409d-f8a3-4c80-b8db-daea6ddabec3");  // BLE LED Service

BLEIntCharacteristic switchCharacteristic("d2be0238-e8d7-4b2f-9a2a-0063c1f1f456", BLERead | BLEWrite | BLENotify);
BLEIntCharacteristic switchCharacteristic1("c00552e0-d880-4c7d-8baf-93356a8ba835", BLERead | BLEWrite | BLENotify);
BLEIntCharacteristic switchCharacteristic2("20ac424f-de57-4c34-b186-920c27b117c9", BLERead | BLEWrite | BLENotify);
BLEIntCharacteristic switchCharacteristic3("2ed22785-bc51-442a-8f29-2d2ea7a40e9f", BLERead | BLEWrite | BLENotify);
BLEIntCharacteristic switchCharacteristic4("52155daf-df88-4990-bc5c-125e5cbd759b", BLERead | BLEWrite | BLENotify);

void ConnectHandler(BLEDevice central) {
  // central connected event handler
  Serial.print("Connected event, central: ");
  Serial.println(central.address());
  BLE.advertise();
}

void DisconnectHandler(BLEDevice central) {
  // central disconnected event handler
  Serial.print("Disconnected event, central: ");
  Serial.println(central.address());
  BLE.advertise();
}

void setup() {
    Serial.begin(9600);
    while (!Serial)
        ;

    // begin initialization
    if (!BLE.begin()) {
        Serial.println("starting BLE failed!");

        while (1)
            ;
    }
    BLE.setEventHandler(BLEConnected, ConnectHandler);
    BLE.setEventHandler(BLEDisconnected, DisconnectHandler);
    // set advertised local name and service UUID:
    BLE.setLocalName("GLOVE");
    BLE.setAdvertisedService(testService);

    // add the characteristic to the service
    testService.addCharacteristic(switchCharacteristic);
    testService.addCharacteristic(switchCharacteristic1);
    testService.addCharacteristic(switchCharacteristic2);
    testService.addCharacteristic(switchCharacteristic3);
    testService.addCharacteristic(switchCharacteristic4);

    // add service
    BLE.addService(testService);
    // set the initial value for the characeristic:
    switchCharacteristic.writeValue('A');
    switchCharacteristic1.writeValue('B');
    switchCharacteristic2.writeValue('C');
    switchCharacteristic3.writeValue('D');
    switchCharacteristic4.writeValue('E');

    // start advertising
    BLE.advertise();

    Serial.println("BLE LED Peripheral");
    Serial.println(testService.uuid());
}

void loop() {
  BLE.poll();
  unsigned char results[5];
  switchCharacteristic.readValue(results[0]);
  switchCharacteristic1.readValue(results[1]);
  switchCharacteristic2.readValue(results[2]);
  switchCharacteristic3.readValue(results[3]);
  switchCharacteristic4.readValue(results[4]);

  Serial.println("--");
  for( unsigned int i = 0; i < 5; i++){
    Serial.println(char(results[i]));
  }
  Serial.println("--");
  wait_ns(100000000000);
}