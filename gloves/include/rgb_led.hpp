/**
 * @file rgb_led.hpp
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

#ifndef _RGB_LED_HPP
#define _RGB_LED_HPP

#include <support.hpp>

/**
 * @class RGB_LED
 * @brief RGB_LED class, control an rgb LED
 * 
 */
class RGB_LED {
private:
    uint8_t rgb_pins[3];
    uint8_t current_color[3];
    void setOutput();
public:
    /**
     * @brief Construct a new RGB_LED object
     * 
     * @param pin uint8_t[3]
     */
    RGB_LED(uint8_t pin[3]);

    /**
     * @brief Set the color of an RGB_LED
     * 
     * @param color uint8_t[3]
     */
    void setColor(uint8_t* color);
};

#endif