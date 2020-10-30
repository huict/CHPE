# ifndef _FLEX_SENSOR_HPP
# define _FLEX_SENSOR_HPP

# include "support.hpp"

// Flex_sensor class
class Flex_sensor{
private:
    uint8_t flex_pin;
public:
    Flex_sensor( uint8_t flex_pin );
    uint8_t getFlexBend();
};

# endif