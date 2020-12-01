#ifndef _GLOVE_TASK_HPP
#define _GLOVE_TASK_HPP

#include <input_object.hpp>
#include <battery.hpp>
#include <finger.hpp>
#include <rgb_led.hpp>
#include <bluetooth.hpp>
#include <support.hpp>

// Glove class
class Glove{
protected:
	Finger fingers[5];
	// Bluetooth bluetooth; Moet later in de children
	Battery battery;
	RGB_LED bluetooth_phone_LED;
	RGB_LED bluetooth_glove_LED;
	RGB_LED battery_LED;
	// First 5 UUID's are for the DOM Glove, second 5 UUID's are for the SUB Glove
	char fingers_UUID[10][37] = {
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
	Glove(uint8_t& finger_pins, uint8_t battery_pin, uint8_t& bluetooth_led_pins, uint8_t& battery_led_pin);
};

class DomGlove : public Glove{
private:
	Bluetooth<10> bluetooth;
	BLEService service;
	char service_UUID[37];
	String service_name = "Glove";
	void generateUUID(char& service_UUID);
	void updateCharacteristics();
	void connectHandler(RGB_LED & glove_led, RGB_LED & phone_led);
	void disconnectHandler(RGB_LED & glove_led, RGB_LED & phone_led);
public:
	DomGlove(uint8_t& finger_pins, uint8_t battery_pin, uint8_t& bluetooth_led_pins, uint8_t& battery_led_pin);
	void run();
};

class SubGlove : public Glove{
private:
	Bluetooth<5> bluetooth;
	void updateCharacteristics();
	void connectHandler(RGB_LED & glove_led);
	void disconnectHandler(RGB_LED & glove_led);
public:
	SubGlove(uint8_t& finger_pins, uint8_t battery_pin, uint8_t& bluetooth_led_pins, uint8_t& battery_led_pin);
	void run();
};


#endif