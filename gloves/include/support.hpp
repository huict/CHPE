/**
 * @file support.hpp
 * @author Maaike Hovenkamp, Duur Alblas
 * @brief 
 * @version 0.1
 * @date 2020-12-16
 * 
 * @copyright 
 * 	This program is free software: you can redistribute it and/or modify
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
#ifndef _SUPPORT_HPP
#define _SUPPORT_HPP

#include <stdint-gcc.h>
#include <Arduino.h>
#include <ArduinoBLE.h>

/**
 * @brief RGB struct, preset colors
 * @details It is possible to easily add more colors in this struct using RGB values.
 * 
 */
struct RGB{
	const uint8_t RED[3] = {255,0,0};
	const uint8_t GREEN[3] = {0,255,0};
	const uint8_t BLUE[3] = {0,0,255};
	const uint8_t OFF[3] = {0,0,0};
};

/**
 * @brief Glove namespace containing STATES namepspace which contains the enum classes : DOM, SUB & CONNECTION. Also contains the enum class DIGITS
 * 
 */
namespace GLOVE{
	namespace STATES{
		enum class DOM {
			SETUP,
			INITIALIZE_BLUETOOTH,
			READ_SENSORS,
		};
		enum class SUB {
			SETUP,
			READ_SENSORS,
			UPDATE_CHARACTERISTICS,
			SCAN_AND_CONNECT_BLUETOOTH
		};
		enum class CONNECTION{
			NONE,
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