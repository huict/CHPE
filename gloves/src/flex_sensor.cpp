/**
 * @file flex_sensor.cpp
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

#include <../include/flex_sensor.hpp>

// Flex_sensor class
Flex_Sensor::Flex_Sensor(
    const uint8_t flex_pin
):
    flex_pin(flex_pin)
{
    pinMode(flex_pin, INPUT);
    Serial.println("Constructed Flex sensor");
};

uint16_t Flex_Sensor::getFlexBend(){
    return analogRead(flex_pin);
}

// DEPRICATED No longer calculate angle just take raw value, NN will work better.
// int Flex_Sensor::getFlexBend(){
//     int ADCflex = analogRead(flex_pin);
//     float Vflex = ADCflex * VCC / Max_Voltage_Reading;
//     float Rflex = R_DIV * (VCC / Vflex - 1.0);

//     int angle = map(Rflex, flatResistance, bendResistance, 0, 180);
//     if( angle < 0){
//         angle = 0;
//     } else if (angle > 180){
//         angle = 180;
//     }
//     int angle = map(ADCflex, 750, 350, 0, 180);
//     return angle;
// }