/**
 * @file rgb_led.cpp
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

#include <../include/rgb_led.hpp>

// RGB_led class

RGB_LED::RGB_LED(
    uint8_t pin[3]
){
    rgb_pins[0] = pin[0];
    rgb_pins[1] = pin[1];
    rgb_pins[2] = pin[2];
    pinMode(rgb_pins[0], OUTPUT);
    pinMode(rgb_pins[1], OUTPUT);
    pinMode(rgb_pins[2], OUTPUT);
    uint8_t blue[3] = {0,0,255};
    setColor(blue);
};

void RGB_LED::setOutput(){
    digitalWrite(rgb_pins[0], current_color[0]);
    digitalWrite(rgb_pins[1], current_color[1]);
    digitalWrite(rgb_pins[2], current_color[2]);
}

void RGB_LED::setColor(uint8_t rgb_color[3]){
    current_color[0] = rgb_color[0]; // Red
    current_color[1] = rgb_color[1]; // Green
    current_color[2] = rgb_color[2]; // Blue
    setOutput();
};