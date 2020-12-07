#ifndef _SUPPORT_HPP
#define _SUPPORT_HPP

#include <stdint-gcc.h>
#include <Arduino.h>
#include <ArduinoBLE.h>

struct RGB{
	uint8_t RED[3] = {255,0,0};
	uint8_t GREEN[3] = {0,255,0};
	uint8_t BLUE[3] = {0,0,255};
	uint8_t ORANGE[3] = {255,128,0};
	uint8_t OFF[3] = {0,0,0};
};

namespace GLOVE{
	namespace STATES{
		enum class DOM {
			SETUP = 0x00,
			INITIALIZE_BLUETOOTH,
			READ_SENSORS,
			UPDATE_CHARACTERISTICS
		};
		enum class SUB {
			SETUP = 0x00,
			READ_SENSORS,
			UPDATE_CHARACTERISTICS,
			SCAN_AND_CONNECT_BLUETOOTH
		};
		enum class CONNECTION{
			NONE = 0x00,
			RIGHT,
			BOTH
		};
	};
	enum class DIGITS{
		THUMB = 0x00,
		INDEX_FINGER,
		MIDDLE_FINGER,
		RING_FINGER,
		PINKY
	};
};

#endif