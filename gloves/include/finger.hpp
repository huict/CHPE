# ifndef _FINGER_HPP
# define _FINGER_HPP

#include "flex_sensor.hpp"
#include "support.hpp"

// Finger class
class Finger{
private:
    GLOVE::DIGITS finger_index;
    Flex_Sensor sensor;
public:
    Finger(GLOVE::DIGITS finger_index, uint8_t flex_pin);
    uint8_t getInformation();
};

# endif