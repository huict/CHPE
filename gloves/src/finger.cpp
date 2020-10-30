#include <../include/finger.hpp>

// Finger class
Finger::Finger(
    uint8_t flex_pin,
    DIGITS finger_index
):
    sensor(flex_pin),
    finger_index(finger_index)
{};

uint8_t Finger::getInformation(){
    return sensor.getBend();
};