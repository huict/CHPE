#include <../include/flex_sensor.hpp>

// Flex_sensor class
Flex_sensor::Flex_sensor(
    uint8_t flex_pin
):
    flex_pin(flex_pin)
{}

uint8_t Flex_sensor::getFlexBend(){
    uint8_t bend = 0; 
    // To-Do calculate bend / angle
    return bend
}