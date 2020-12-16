/**
 * @file finger.hpp
 * @author Maaike Hovenkamp, Duur Alblas
 * @brief 
 * @version 0.1
 * @date 2020-12-16
 *  @copyright This program is free software: you can redistribute it and/or modify
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

# ifndef _FINGER_HPP
# define _FINGER_HPP

#include "flex_sensor.hpp"
#include "support.hpp"

/**
 * @class Finger
 * @brief Finger class, Class that controls a Finger object
 * 
 */
class Finger{
private:
    GLOVE::DIGITS finger_index;
    Flex_Sensor sensor;
public:
    /**
     * @brief Construct a new Finger object
     * 
     * @param finger_index GLOVE.DIGITS
     * @param flex_pin uint8_t
     */
    Finger(GLOVE::DIGITS finger_index, uint8_t flex_pin);

    /**
     * @brief Get information about Finger
     * 
     * @return int 
     */
    int getInformation();
};

# endif