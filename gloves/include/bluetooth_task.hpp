// bluetooth task

# ifndef _BLUETOOTH_TASK_HPP
# define _BLUETOOTH_TASK_HPP

# include "input_object.hpp"
# include <free

class Bluetooth_task{
private:
    BLE_device BLE_device_phone;
    char[11] advertised_service = 'HonestHands';
    char[4] device_name = 'HAND';
    char* data;
    uint8_t data_length = 255;
    bool active_connection;
    InputObject mode_switch
public:
    Bluetooth_task( InputObject mode_switch):
        mode_switch(mode_switch)
    {}
    void main();
};

# endif