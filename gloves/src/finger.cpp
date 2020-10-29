// Finger class, DIGITS enum

#include "../../gloves/include/finger.hpp"

uint8_t Finger::getInformation(){
    return sensor.getFlexBent();
}