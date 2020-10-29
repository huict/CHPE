#ifndef _INPUT_OBJECT_HPP
#define _INPUT_OBJECT_HPP

// Input_object class
class Input_object{
private:
    uint8_t input_pin;
public:
    Input_object(uint8_t input_pin);
    bool readPin();
};

#endif