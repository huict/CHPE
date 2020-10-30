#include <../include/input_object.hpp>

// Input_object class

Input_object::Input_object(
    uint8_t input_pin
):
    input_pin(input_pin)
{};

bool Input_object::readPin(){
    return input_pin.digitalRead();
};