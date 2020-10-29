#ifndef _GLOVE_TASK_HPP
#define _GLOVE_TASK_HPP

#include <input_object.hpp>
#include <battery.hpp>
#include <finger.hpp>
#include <rgb_led.hpp>
#include <support.hpp>

// Glove_task class
class Glove_task{
private:
	uint8_t finger_position[5];
	char compiled_data[255];
	Finger fingers[5];
	Timer finger_time;
	Timer battery_time;
	Battery battery;
	RGB_LED bluetooth_led;
	RGB_LED radio_led;
	RGB_LED battery_led;
	Input_object mode_switch;

	void updateBatteryLEDColor();
	void prepareData();
	void compileData(char data[127], char data2[127]);
	void compileData(char data[255]);

public:
	Glove_task(
		uint8_t finger_pins[5], 
		uint8_t battery_pin,
		uint8_t switch_pin,
		uint8_t bluetooth_led_pin[3],
		uint8_t radio_led_pin[3],
		uint8_t battery_led_pin[3]
	):
		battery(battery_pin),
		mode_switch(switch_pin),
		bluetooth_led(bluetooth_led_pin),
		radio_led(radio_led_pin),
		battery_led(battery_led_pin)
	{
		for (uint8_t i = 0; i < 5; i++) {
			fingers[i] = Finger(finger_pins[i]);
		};
	};

	void main(){

	};


};

#endif