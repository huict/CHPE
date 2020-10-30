// bluetooth task

# include "../../gloves/include/bluetooth_task.hpp"

void bluetooth_task::main(){
    BLUETOOTH_STATES state = BLUETOOTH_STATES::INITIALIZE
    switch(state){
        case(BLUETOOTH_STATES::INITIALIZE):{
            BLEUnsignedCharCharacteristic glove_peripheral_char_1("2A19", BLERead | BLENotify);  // standard 16-bit characteristic UUID
            BLEUnsignedCharCharacteristic glove_peripheral_char_2("2A19", BLERead | BLENotify);  // standard 16-bit characteristic UUID
            BLEUnsignedCharCharacteristic glove_peripheral_char_3("2A19", BLERead | BLENotify);  // standard 16-bit characteristic UUID
            BLEUnsignedCharCharacteristic glove_peripheral_char_4("2A19", BLERead | BLENotify);  // standard 16-bit characteristic UUID
            BLEUnsignedCharCharacteristic glove_peripheral_char_5("2A19", BLERead | BLENotify);  // standard 16-bit characteristic UUID
            BLEUnsignedCharCharacteristic glove_peripheral_char_6("2A19", BLERead | BLENotify);  // standard 16-bit characteristic UUID
            BLEUnsignedCharCharacteristic glove_peripheral_char_7("2A19", BLERead | BLENotify);  // standard 16-bit characteristic UUID
            BLEUnsignedCharCharacteristic glove_peripheral_char_8("2A19", BLERead | BLENotify);  // standard 16-bit characteristic UUID
            BLEUnsignedCharCharacteristic glove_peripheral_char_9("2A19", BLERead | BLENotify);  // standard 16-bit characteristic UUID
            BLEUnsignedCharCharacteristic glove_peripheral_char_10("2A19", BLERead | BLENotify);  // standard 16-bit characteristic UUID
            
            if (!BLE.begin()) {
                Serial.println("starting BLE failed!");
                while (1);
            }

            BLE.setLocalName(advertised_service);
            BLE.setAdvertisedService(glove_peripheral);
            glove_peripheral.addCharacteristic(glove_peripheral_char_1);
            glove_peripheral.addCharacteristic(glove_peripheral_char_2);
            glove_peripheral.addCharacteristic(glove_peripheral_char_3);
            glove_peripheral.addCharacteristic(glove_peripheral_char_4);
            glove_peripheral.addCharacteristic(glove_peripheral_char_5);
            glove_peripheral.addCharacteristic(glove_peripheral_char_6);
            glove_peripheral.addCharacteristic(glove_peripheral_char_7);
            glove_peripheral.addCharacteristic(glove_peripheral_char_8);
            glove_peripheral.addCharacteristic(glove_peripheral_char_9);
            glove_peripheral.addCharacteristic(glove_peripheral_char_10);
            BLE.addService(glove_peripheral);


            BLE.advertise();
            state = BLUETOOTH_STATES::WAIT_FOR_CONNECTION;
            break;
        }
        case(BLUETOOTH_STATES::WAIT_FOR_CONNECTION){
            BLE_device_phone = BLE.central();
            if(BLE_device_phone){
                state = BLUETOOTH_STATES::CONNECTED;
                break;
            }
        }
        case(BLUETOOTH_STATES::CONNECTED){
            while(BLE_device_phone.connected()){
                // check if there is new valid data
                uint8_t[10] old_data;
                uint8_t[10] data;
                // data = read from bluetooth_queue
                if( data != old_data ){
                    // put individual numbers into richt characteristic
                }
            }
            state = BLUETOOTH_STATES::WAIT_FOR_CONNECTION;
            break;
        }

    }



}