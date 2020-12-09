// Receiver
#include "../include/glove.hpp"

uint8_t glove_switch = 3;
uint8_t battery_pin = 4;

uint8_t battery_LED_RGB[3] = {15, 13, 14};
uint8_t glove_LED_RGB[3] = {23, 21, 27};
uint8_t phone_LED_RGB[3] = {2, 1, 8};
uint8_t fingers[5] = {2, 31, 29, 30, 5};

BLEService glove_service("bd3d409d-f8a3-4c80-b8db-daea6ddabec3");

void connectHandler(BLEDevice central){
    Serial.print("Connected event, central: ");
    Serial.println(central.address());
};

void disconnectHandler(BLEDevice central){
    Serial.print("disconnect event, central: ");
    Serial.println(central.address());
};

void setup() {
	pinMode(glove_switch, INPUT);
	if(digitalRead(glove_switch)){
		SubGlove glove(battery_pin, glove_LED_RGB, phone_LED_RGB, battery_LED_RGB, fingers);
		glove.run();
	} else {
		DomGlove glove(glove_service, battery_pin, glove_LED_RGB, phone_LED_RGB, battery_LED_RGB, fingers);
		glove.run();
	};
};

void loop() {
};