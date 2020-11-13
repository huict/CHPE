#include <../include/flex_sensor.hpp>

// Flex_sensor class
Flex_sensor::Flex_sensor(
    uint8_t flex_pin
):
    flex_pin(flex_pin)
{}

uint8_t Flex_sensor::getFlexBend(){
    int ADCflex = analogRead(flexPin);
    float Vflex = ADCflex * VCC / 1023.0;
    float Rflex = R_DIV * (VCC / Vflex - 1.0);

    float angle = map(Rflex, flatResistance, bendResistance, 0, 90.0);
    return angle;z2
}