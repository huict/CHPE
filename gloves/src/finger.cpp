/**
 * @file finger.cpp
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

#include <../include/finger.hpp>

// Finger class
Finger::Finger(
    GLOVE::DIGITS finger_index,
    const uint8_t flex_pin
):
    finger_index(finger_index),
    sensor(flex_pin)
{
    Serial.println("Contructed finger");
};

int Finger::getInformation(){
    // The amount of measures is a arbritary number used to filter noise by returning median, can be changed to increase accuracy. 
    // WARNING: Must be uneven!
    const unsigned int amount_of_measures = 11;
    std::array<int, amount_of_measures> measured_values;
    for( unsigned int i = 0; i < amount_of_measures; i++){
        measured_values[i] = sensor.getFlexRaw();
    }
    std::sort(measured_values.begin(), measured_values.end());
    return measured_values[amount_of_measures/2];
};