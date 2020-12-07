// Receiver
#include "../include/glove.hpp"

uint8_t glove_switch = 3;
uint8_t battery_pin = 4;

uint8_t battery_LED_RGB[3] = {15, 13, 14};
uint8_t glove_LED_RGB[3] = {23, 21, 27};
uint8_t phone_LED_RGB[3] = {2, 1, 8};
uint8_t fingers[5] = {2, 31, 29, 30, 5};

void setup() {
	pinMode(glove_switch, INPUT);
	if(digitalRead(glove_switch)){
		SubGlove glove(fingers, battery_pin, glove_LED_RGB, phone_LED_RGB, battery_LED_RGB);
	} else {
		DomGlove glove(fingers, battery_pin, glove_LED_RGB, phone_LED_RGB, battery_LED_RGB);
	}
}

void loop() {
	glove.run();
	
}