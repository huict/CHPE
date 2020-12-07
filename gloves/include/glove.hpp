#ifndef _GLOVE_HPP
#define _GLOVE_HPP

#include <input_object.hpp>
#include <battery.hpp>
#include <finger.hpp>
#include <rgb_led.hpp>
// #include <bluetooth.hpp>
#include <support.hpp>


/*
central | device = subgloveTASK_
TASK_
peripheral | service = domglove

Gloves : SUB & DOM
DOM : heeft "bulletin bord" waar alle characteristics in staan
DOM : Bezit het BLEService object wat een "Glove" service gaat aanbieden
DOM : Door "BLE.begin()" aan te roepen kunnen we beginnen met handlers, namen etc te setten. bijvoorbeeld : BLE.setLocalName("Glove")
DOM : Door iedere loop BLE.poll() te callen worden events afgehandeld

SUB : verbind met de DOM en past characteristics aan
SUB : Bezit een BLEDevice object bijvoorbeeld genaamd "Peripheral" en die word gevuld met een BLE.available waarop we ".localName()" kunnen aanroepen om te kijken
	of het wel "Glove" is.
SUB : Als de "Peripheral" de "Glove" is dan kunnen we characteristics gaan discoveren met behulp van UUID's
SUB : Zolang "Peripheral.connected() == true" is de SUB met de DOM verbonden

Phone: Android
Android : verbind met de DOM en leest characteristics uit

	BLEService testService
	BLE.setAdvertisedService(testService) -> verderwerken met testService
	BLE.addService(testService);
	BLE.advertise(); -> alle commando's aanroepen op BLE
*/

// Glove class
class Glove{
protected:
	Finger fingers[5];
	Battery battery;
	RGB_LED bluetooth_phone_LED;
	RGB_LED bluetooth_glove_LED;
	RGB_LED battery_LED;
	String service_name = "GLOVE";
	// First 5 UUID's are for the DOM Glove, second 5 UUID's are for the SUB Glove
	String fingers_UUID[10] = {
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

	uint8_t* getFingerPositions();
	virtual void updateCharacteristics() = 0;

public:
	Glove(uint8_t* finger_pins, uint8_t battery_pin, uint8_t* glove_led_pins, uint8_t* phone_led_pins, uint8_t* battery_led_pin);
	virtual void run() = 0;
};

// Domglove ==  peripheral == prikbord
class DomGlove : public Glove{
	const char service_UUID[37] = "bd3d409d-f8a3-4c80-b8db-daea6ddabec3";
	BLEService service(service_UUID);
	BLEDevice new_central, phone, glove;

	// BLEUnsignedCharCharacteristic dom_finger_0(fingers_UUID[0], BLERead | BLEWrite | BLENotify);
    // BLEUnsignedCharCharacteristic dom_finger_1(fingers_UUID[1], BLERead | BLEWrite | BLENotify);
    // BLEUnsignedCharCharacteristic dom_finger_2(fingers_UUID[2], BLERead | BLEWrite | BLENotify);
    // BLEUnsignedCharCharacteristic dom_finger_3(fingers_UUID[3], BLERead | BLEWrite | BLENotify);
    // BLEUnsignedCharCharacteristic dom_finger_4(fingers_UUID[4], BLERead | BLEWrite | BLENotify);
    
    // BLEUnsignedCharCharacteristic sub_finger_0(fingers_UUID[5], BLERead | BLEWrite | BLENotify);
    // BLEUnsignedCharCharacteristic sub_finger_1(fingers_UUID[6], BLERead | BLEWrite | BLENotify);
    // BLEUnsignedCharCharacteristic sub_finger_2(fingers_UUID[7], BLERead | BLEWrite | BLENotify);
    // BLEUnsignedCharCharacteristic sub_finger_3(fingers_UUID[8], BLERead | BLEWrite | BLENotify);
    // BLEUnsignedCharCharacteristic sub_finger_4(fingers_UUID[9], BLERead | BLEWrite | BLENotify);

	// void generateUUID(char* service_UUID);
	void updateCharacteristics(uint8_t * finger_positions);
	void connectHandler(BLEDevice & central);
	void disconnectHandler(BLEDevice & central);
	void createBLEService();
public:
	DomGlove(uint8_t* finger_pins, uint8_t battery_pin, uint8_t* glove_led_pins, uint8_t* phone_led_pins, uint8_t* battery_led_pin);
	void run();
};

class SubGlove : public Glove{
private:
	BLEDevice dom_glove;

	BLEUnsignedCharCharacteristic sub_finger_0(fingers[5]);
	BLEUnsignedCharCharacteristic sub_finger_1(fingers[6]);
	BLEUnsignedCharCharacteristic sub_finger_2(fingers[7]);
	BLEUnsignedCharCharacteristic sub_finger_3(fingers[8]);
	BLEUnsignedCharCharacteristic sub_finger_4(fingers[9]);

	void connectToDom();
	void updateCharacteristics(uint8_t * finger_positions);
	void connectHandler(BLEDevice &central, RGB_LED & glove_led);
	void disconnectHandler(BLEDevice &central, RGB_LED & glove_led);
public:
	SubGlove(uint8_t* finger_pins, uint8_t battery_pin, uint8_t* glove_led_pins, uint8_t* phone_led_pins, uint8_t* battery_led_pin);
	void run();
};

#endif