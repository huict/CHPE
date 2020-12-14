// Receiver
#include "../include/glove.hpp"

uint8_t glove_switch = 2;
uint8_t battery_pin = 3;

uint8_t battery_LED_RGB[3] = {4, 5, 6};
uint8_t glove_LED_RGB[3] = {7, 8, 9};
uint8_t phone_LED_RGB[3] = {10, 11, 12};

uint8_t fingers[5] = {A5, A4, A3, A2, A1};

void connectHandler(BLEDevice central){
    Serial.print("Connected event, central: ");
    Serial.println(central.address());
};

void disconnectHandler(BLEDevice central){
    Serial.print("disconnect event, central: ");
    Serial.println(central.address());
};

void setup() {
	Serial.begin(9600);
    while(!Serial){};
	delay(5000);
	Serial.println("Serial works.");
	delay(1000);
	pinMode(glove_switch, INPUT);
};

void loop() {
	Serial.println("Stuck in loop...");
	if(digitalRead(glove_switch)){
		SubGlove glove(battery_pin, glove_LED_RGB, phone_LED_RGB, battery_LED_RGB, fingers);
		Serial.println("Starting Sub Glove!");
		glove.run();
	} else {
		DomGlove glove(battery_pin, glove_LED_RGB, phone_LED_RGB, battery_LED_RGB, fingers);
		Serial.println("Starting Dom Glove!");
		glove.run();
	};
	delay(1000);
};