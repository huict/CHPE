#ifndef _RGB_LED_HPP
#define _RGB_LED_HPP

#include <support.hpp>

// RGB_led class

class RGB_LED {
private:
    uint8_t rgb_pins[3];
    uint8_t current_color[3];
    void setOutput();
public:
    RGB_LED(uint8_t pin[3]);
    void setColor(uint8_t* color);
};

#endif