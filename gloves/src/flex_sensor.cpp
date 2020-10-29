// Flex_sensor class

#include "/../../gloves/include/flex_sensor.hpp"

uint8_t Flex_sensor::getFlexBend(){
    value = analogRead(flexPin);                    
    value = map(value, 700, 900, 0, 255);
    return value;
};