#include <../include/input_object.hpp>

// Input_object class

Input_object::Input_object(
    uint8_t input_pin
):
    input_pin(input_pin)
{
    pinMode(input_pin, INPUT);
};

bool Input_object::readPin(){
    return digitalRead(input_pin);
};