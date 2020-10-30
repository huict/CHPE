#include <../include/rgb_led.hpp>

// RGB_led class

RGB_LED::RGB_LED(
    uint8_t pin[3]
):
    rgb_pins[0] = pin[0],
    rgb_pins[1] = pin[1],
    rgb_pins[2] = pin[2]
{ 
   setColor(RGB::blue);
};

void RGB_LED::setColor(RGB color){
    current_color = color;
};

void RGB_LED::setColor(uint8_t rgb_color[3]){
    current_color[0] = rgb_color[0] // Red
    current_color[1] = rgb_color[1] // Green
    current_color[2] = rgb_color[2] // Blue
};