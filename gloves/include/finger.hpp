# ifndef _FINGER_HPP
# define _FINGER_HPP

#include "flex_sensor.hpp"
#include "support.hpp"

// Finger class
class Finger{
private:
    Flex_Sensor sensor;
    DIGITS finger_index;
public:
    Finger(uint8_t flex_pin, DIGITS finger_index);
    uint8_t getInformation();
};

# endif