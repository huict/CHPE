/**
 * @file battery.cpp
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

#ifndef BATTERY_HPP
#define BATTERY_HPP
#include "../include/battery.hpp"

// Battery class
Battery::Battery(
    uint8_t analog_read_pin
):
    analog_read_pin(analog_read_pin)
{
    pinMode(analog_read_pin, INPUT);
};

uint8_t Battery::getBatteryPercentage(){
    return digitalRead(analog_read_pin);
};

#endif