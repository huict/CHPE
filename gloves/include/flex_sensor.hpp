# ifndef _FLEX_SENSOR_HPP
# define _FLEX_SENSOR_HPP

# include "support.hpp"

// Flex_sensor class
class Flex_Sensor{
private:
    uint8_t flex_pin;
    uint8_t VCC = 3.3;
    uint16_t R_DIV = 47000;
    uint16_t flatResistance = 25000;
    uint32_t bendResistance = 100000;
public:
    Flex_Sensor( uint8_t flex_pin );
    uint8_t getFlexBend();
};

# endif