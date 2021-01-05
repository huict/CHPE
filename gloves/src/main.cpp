/**
 * @file main.cpp
 * @author Maaike Hovenkamp, Duur Alblas
 * @brief 
 * @version 0.1
 * @date 2020-12-16
 * 
 * @copyright This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * 
 */

#include "../include/glove.hpp"

// Single pin inputs
const uint8_t glove_switch = 2;
const uint8_t battery_pin = 3;
const uint8_t fingers[5] = {A5, A4, A3, A2, A1};

// LED pins
const uint8_t battery_LED_RGB[3] = {4, 5, 6};
const uint8_t glove_LED_RGB[3] = {7, 8, 9};
const uint8_t phone_LED_RGB[3] = {10, 11, 12};

void setup() {
	Serial.begin(9600);
	// WARNING : Wait until a Serial Monitor is found before progressing further.
    while(!Serial){};
	delay(1000);
	Serial.println("Serial works.");
	delay(1000);
	pinMode(glove_switch, INPUT);
};

void loop() {
	if(digitalRead(glove_switch)){
		SubGlove glove(battery_pin, glove_LED_RGB, phone_LED_RGB, battery_LED_RGB, fingers);
		Serial.println("Starting Sub Glove!");
		glove.run();
	} else {
		DomGlove glove(battery_pin, glove_LED_RGB, phone_LED_RGB, battery_LED_RGB, fingers);
		Serial.println("Starting Dom Glove!");
		glove.run();
	};
};