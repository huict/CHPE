/**
 * @file glove.hpp
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

#ifndef _GLOVE_HPP
#define _GLOVE_HPP

#include <input_object.hpp>
#include <battery.hpp>
#include <finger.hpp>
#include <rgb_led.hpp>
#include <support.hpp>

/**
 * @class Glove class
 * @brief Glove class to control a Glove module
 * @details The Glove module can consist of 2 parts: The Dominant Glove and a Submissive Glove. 
 * The SubGlove connects to the DomGlove using bluetooth. A phone can connect to the Dominant Glove. 
 * The Dominant Glove is also known as a peripheral and the SubGlove is also known as a central.
 * 
 */
class Glove{
protected:
	Battery battery;
	RGB_LED bluetooth_phone_LED;
	RGB_LED bluetooth_glove_LED;
	RGB_LED battery_LED;
	
	// WARNING Keep in mind how many fingers you can initialize with
	Finger * fingers[5];
	const char service_name[6] = "GLOVE";
	// First 5 UUID's are for the DomGlove, second 5 UUID's are for the SubGlove
	const char fingers_UUID[10][37] = {
		"34452906-33d2-11eb-adc1-0242ac120002",
		"3b08185c-33d2-11eb-adc1-0242ac120002",
		"3e1443e0-33d2-11eb-adc1-0242ac120002",
		"40f35268-33d2-11eb-adc1-0242ac120002",
		"453d9d1a-33d2-11eb-adc1-0242ac120002",
		"486172dc-33d2-11eb-adc1-0242ac120002",
		"50a55ac6-33d2-11eb-adc1-0242ac120002",
		"5c36354a-33d2-11eb-adc1-0242ac120002",
		"67c8b8ba-33d2-11eb-adc1-0242ac120002",
		"6b51a79e-33d2-11eb-adc1-0242ac120002"
	};

	/**
	 * @brief Get the Finger Positions of all the Finger objects connected to a Glove
	 * 
	 * @param finger_pos uint16_t[5]
	 */
	void getFingerPositions( uint16_t * finger_pos);

	/**
	 * @brief Update the characteristcs of the Glove
	 * 
	 * @param characteristics BLECharacterisic[]
	 * @param finger_positions uint16_t[]
	 */
	void updateCharacteristics(BLECharacteristic* characteristics, uint16_t * finger_positions);

public:
	/**
	 * @brief Construct a new Glove object
	 * 
	 * @param battery_pin uint_t
	 * @param glove_led_pins uint8_t[3]
	 * @param phone_led_pins uint8_t[3]
	 * @param battery_led_pin uint8_t[3]
	 * @param finger_pins uint8_t[5]
	 */
	Glove(const uint8_t battery_pin, const uint8_t* glove_led_pins, const uint8_t* phone_led_pins, const uint8_t* battery_led_pin, const uint8_t* finger_pins);
	
	/**
	 * @brief run function, virtual void
	 * 
	 */
	virtual void run() = 0;
};

/**
 * @class DomGlove
 * @brief Dominant Glove class, inherits from Glove
 * 
 */
class DomGlove : public Glove{
	/**
	 * @brief Create a BLE service. It does so with a BLEService object which is declared globally in the glove.cpp because of restrictions within the library.
	 * 
	 * @param dom_fingers BLEIntCharacteristic[5]
	 * @param sub_fingers BLEIntCharacteristic[5]
	 * @return bool
	 */
	bool createBLEService(BLEUnsignedIntCharacteristic * dom_fingers, BLEUnsignedIntCharacteristic * sub_fingers);
public:

	/**
	 * @brief Construct a new DomGlove object
	 * 
	 * @param battery_pin uint8_t
	 * @param glove_led_pins uint8_t[3]
	 * @param phone_led_pins uint8_t[3]
	 * @param battery_led_pin uint8_t[3]
	 * @param finger_pins uint8_t[5]
	 */
	DomGlove(const uint8_t battery_pin, const uint8_t* glove_led_pins, const uint8_t* phone_led_pins, const uint8_t* battery_led_pin, const uint8_t* finger_pins);
	
	/**
	 * @brief Main run function of the DomGlove.
	 * @details WARNING This function is a endless loop!
	 * 
	 */
	void run();
};

/**
 * @class SubGlove 
 * @brief Submissive Glove class, inherits from Glove
 * 
 */
class SubGlove : public Glove{
private:
	BLECharacteristic characteristics[5];
	
	/**
	 * @brief Connect to a DomGlove.
	 * 
	 */
	void connectToDom();
public:
	/**
	 * @brief Construct a new SubGlove object
	 * 
	 * @param battery_pin uint8_t
	 * @param glove_led_pins uint8_t[3]
	 * @param phone_led_pins uint8_t[3]
	 * @param battery_led_pin uint8_t[3]
	 * @param finger_pins uint8_t[5]
	 */
	SubGlove(const uint8_t battery_pin, const uint8_t* glove_led_pins, const uint8_t* phone_led_pins, const uint8_t* battery_led_pin, const uint8_t* finger_pins);
	
	/**
	 * @brief Main run function of the SubGlove
	 * @details WARNING This function is a endless loop!
	 * 
	 */
	void run();
};

#endif