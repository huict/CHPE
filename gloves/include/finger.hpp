// Finger class, DIGITS enum

# ifndef _FINGER_HPP
# define _FINGER_HPP

#include "support.hpp"
#include "flex_sensor.hpp"

enum DIGITS{
    THUMB = 0x00, 
    INDEX_FINGER, 
    MIDDLE_FINGER, 
    RINGER_FINGER, 
    PINKY
};

class Finger{
private:
    DIGITS finger_index;
    FLex_Sensor sensor;
public:
    Finger( DIGITS finger_index, uint8_t flex_pin );
    uint8_t getInformation();
};

# endif