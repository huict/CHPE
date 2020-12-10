#include <../include/finger.hpp>

// Finger class
Finger::Finger(
    GLOVE::DIGITS finger_index,
    uint8_t flex_pin
):
    finger_index(finger_index),
    sensor(flex_pin)
{
    Serial.println("Contructed finger");
};

uint8_t Finger::getInformation(){
    return sensor.getFlexBend();
};