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