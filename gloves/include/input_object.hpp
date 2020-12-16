/**
 * @file input_object.hpp
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

#ifndef _INPUT_OBJECT_HPP
#define _INPUT_OBJECT_HPP

#include <support.hpp>

/**
 * @class Input_object
 * @brief Input_Object class, controls any input object.
 * 
 */
class Input_object{
private:
    uint8_t input_pin;
public:
    /**
     * @brief Construct a new Input_object object
     * 
     * @param input_pin uint8_t
     */
    Input_object(uint8_t input_pin);
    
    /**
     * @brief Read the pin status of the input object
     * @details Reads the pin using digitalRead() from the Arduino Library.
     * 
     * @return bool 
     */
    bool readPin();
};

#endif