// bluetooth task

# ifndef _BLUETOOTH_TASK_HPP
# define _BLUETOOTH_TASK_HPP

# include "support.hpp"
# include "input_object.hpp"
#include <ArduinoBLE.h>

enum class BLUETOOTH_STATES{
    INITIALIZE,
    WAIT_FOR_CONNECTION,
    UPDATE_CHARACTERISTIC,
    CONNECTED,
    PREPARE_DATA
};

class Bluetooth_task{
private:
    BLE_service glove_peripheral("180F");
    BLE_device BLE_device_phone;
    const char* advertised_service = "HonestHands";
    char[4] device_name = 'HAND';
    uint8_t data_length = 255;
    bool active_connection;
    InputObject mode_switch
public:
    Bluetooth_task( InputObject mode_switch):
        mode_switch(mode_switch)
    {}
    void main();
    // source = https://ladvien.com/arduino-nano-33-bluetooth-low-energy-setup/
};
# endif