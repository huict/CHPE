// Battery class
# ifndef _BATTERY_HPP
# define _BATTERY_HPP

#include "support.hpp"

class Battery{
private:
    uint8_t analog_read_pin;
public:
    Battery( uint8_t analog_read_pin);
    uint8_t getBatteryPercentage();
};

# endif