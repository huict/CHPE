/**
 * @file battery.hpp
 * @author Maaike Hovenkamp, Duur Alblas
 * @brief battery file containing future battery class
 * @version 0.1
 * @date 2020-12-16
 * 
 *  * @copyright This program is free software: you can redistribute it and/or modify
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

# ifndef _BATTERY_HPP
# define _BATTERY_HPP

#include "support.hpp"

/**
 * @class Battery
 * @brief Battery Class, Class used to read the battery percentage of the battery that's connected to the arduino
 * @details TODO: Implement class since it is not ready yet and hasn't been tested yet.
 */
class Battery{
private:
    uint8_t analog_read_pin;
public:
    /**
     * @brief Construct a new Battery object
     * 
     * @param analog_read_pin uint8_t
     */
    Battery(const uint8_t analog_read_pin);

    /**
     * @brief Get the Battery Percentage object
     * 
     * @return uint8_t 
     */
    uint8_t getBatteryPercentage();
};

# endif