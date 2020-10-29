#ifndef _RGB_LED_HPP
#define _RGB_LED_HPP

#include <support.hpp>

// RGB_led class

class RGB_LED {
private:
    uint8_t rgb_pins[3];
    uint8_t current_color[3];
public:
    RGB_LED(uint8_t pin[3]);
    void setColor(RGB color);
    void setColor(uint8_t rgb_colors[3]);
};

#endif