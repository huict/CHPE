/**
 * @file flex_sensor.hpp
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
# ifndef _FLEX_SENSOR_HPP
# define _FLEX_SENSOR_HPP

# include "support.hpp"

/**
 * @class Flex_sensor
 * @brief Flex Sensor used to read the bend from a flex sensor
 * 
 */
class Flex_Sensor{
private:
    const uint8_t flex_pin;
    const float VCC = 3.3;
    const uint16_t R_DIV = 47000;
    const double Max_Voltage_Reading = 1023.0;
    const uint16_t flatResistance = 25000;
    const uint32_t bendResistance = 100000;
public:
    /**
     * @brief Construct a new Flex_Sensor object
     * 
     * @param flex_pin uint8_t
     */
    Flex_Sensor(const uint8_t flex_pin );

    /**
     * @brief Get the bend of the flex sensor
     * @details Currently the minimum angle returned is 0 degrees and the maximum angle is 180 degrees.
     * @return int 
     */
    int getFlexBend();
    
    /**
     * @brief Get the Raw data from the flex sensor pin.
     * @return uint16_t
     */
    uint16_t getFlexRaw();
};

# endif