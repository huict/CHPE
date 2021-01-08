/**
 * @file glove.cpp
 * @author Maaike Hovenkamp, Duur Alblas
 * @brief 
 * @version 0.1
 * @date 2020-12-16
 * 
 * @copyright This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * 
 */

#include <../include/glove.hpp>

// The Following Global objects are needed here since the BLE Library does not play well within classes.
BLEService g_service("bd3d409d-f8a3-4c80-b8db-daea6ddabec3");
BLELocalDevice g_BLElocal;
BLEDevice g_connected_centrals[2];

bool g_glove_connected = false;
bool g_last_glove_status = g_glove_connected;
bool g_phone_connected = false;
bool g_last_phone_status = g_phone_connected;

// ===================================== Constructors =============================

Glove::Glove(const uint8_t battery_pin, const uint8_t* glove_led_pins, const uint8_t* phone_led_pins, const uint8_t* battery_led_pin, const uint8_t* finger_pins):
    battery(battery_pin),
    bluetooth_phone_LED(phone_led_pins),
    bluetooth_glove_LED(glove_led_pins),
    battery_LED(battery_led_pin)
{
    fingers[0] = new Finger(GLOVE::DIGITS::THUMB, finger_pins[0]);
    fingers[1] = new Finger(GLOVE::DIGITS::INDEX_FINGER, finger_pins[1]);
    fingers[2] = new Finger(GLOVE::DIGITS::MIDDLE_FINGER, finger_pins[2]);
    fingers[3] = new Finger(GLOVE::DIGITS::RING_FINGER, finger_pins[3]);
    fingers[4] = new Finger(GLOVE::DIGITS::PINKY, finger_pins[4]);
};

DomGlove::DomGlove( const uint8_t battery_pin, const uint8_t* glove_led_pins, const uint8_t* phone_led_pins, const uint8_t* battery_led_pin, const uint8_t* finger_pins):
    Glove(battery_pin, glove_led_pins, phone_led_pins, battery_led_pin, finger_pins)
{};

SubGlove::SubGlove(const uint8_t battery_pin, const uint8_t* glove_led_pins, const uint8_t* phone_led_pins, const uint8_t* battery_led_pin, const uint8_t* finger_pins):
    Glove(battery_pin, glove_led_pins, phone_led_pins, battery_led_pin, finger_pins)
{};

// ===================================== Functions =============================

void Glove::getFingerPositions( uint16_t * finger_pos){
    for(unsigned int i = 0; i < 5; i++){
        finger_pos[i] = fingers[i]->getInformation();
    };
};

void Glove::updateCharacteristics( BLECharacteristic * characteristics, uint16_t * finger_positions){
    characteristics[0].writeValue(finger_positions[0]);
    characteristics[1].writeValue(finger_positions[1]);
    characteristics[2].writeValue(finger_positions[2]);
    characteristics[3].writeValue(finger_positions[3]);
    characteristics[4].writeValue(finger_positions[4]);
};

void SubGlove::connectToDom(){
  	// Start scan for peripheral service
    if (g_BLElocal.scanForName(service_name)){
        Serial.println("Start Scanning");
        // check if a peripheral has been discovered
        g_glove_connected = false;
        while(!g_glove_connected){
            BLEDevice dom_glove_device = g_BLElocal.available();
            if (dom_glove_device){
                // Check for correct device discovered
                if (dom_glove_device.localName() == service_name) {
                    g_glove_connected = true;
                    Serial.println("Found Dominant Glove");
                    g_BLElocal.stopScan();
                    if (dom_glove_device.connect()) {
                        Serial.println("Connected");
                    };
                    if(dom_glove_device.discoverAttributes()){
                        characteristics[0] = dom_glove_device.characteristic(fingers_UUID[5]);
                        characteristics[1] = dom_glove_device.characteristic(fingers_UUID[6]);
                        characteristics[2] = dom_glove_device.characteristic(fingers_UUID[7]);
                        characteristics[3] = dom_glove_device.characteristic(fingers_UUID[8]);
                        characteristics[4] = dom_glove_device.characteristic(fingers_UUID[9]);
                        for(unsigned int i = 0; i < 5; i++){
                            if(characteristics[i] == false){
                                Serial.println("Finger "+String(i)+" NOT Accessable");
                                g_glove_connected = false;
                                continue;
                            };
                            if(characteristics[i].canSubscribe()){
                                characteristics[i].subscribe();
                            } else {
                                Serial.println("Finger "+String(i)+" NOT Subscribable");
                                g_glove_connected = false;
                                continue;
                            };
                        };
                    };
                };
            };
        };
    };
};

/**
 * @brief ConnectHandler used by the BLELocalDevice.
 * @details This Function cannot be part of a class because of the BLE Library
 * 
 * @param central 
 */
void domConnectHandler(BLEDevice central){
    Serial.println("---ConHand---");
    central.discoverAttributes();
    if(central.deviceName() == arduino::String("SubmissiveGlove") && !g_glove_connected){
        g_connected_centrals[0] = central;
        g_glove_connected = true;
        Serial.println("Glove connected event, central: ");
    } else if(central.deviceName() == arduino::String("SubmissiveGlove") && g_glove_connected){
        central.disconnect();
        Serial.println("Denied glove connection event, central: ");   
    } else if(g_phone_connected){
        central.disconnect();
        Serial.println("Denied connection event, central: ");
    } else {
        g_connected_centrals[1] = central;
        g_phone_connected = true;
        Serial.println("Connected event, central: ");
    };

    Serial.println(central.deviceName());
    Serial.println(central.address());
    Serial.println("-------------");
    g_BLElocal.advertise();
};

/**
 * @brief DisconnectHandler used by the BLELocalDevice
 * @details This Function cannot be part of a class because of the BLE Library.
 * 
 * @param central 
 */
void domDisconnectHandler(BLEDevice central){
    Serial.println("---DisHand---");
    if(central.address() == g_connected_centrals[0].address() && g_glove_connected){
        g_glove_connected = false;
        Serial.println("Glove disconnect event, central: ");
    } else if(central.address() == g_connected_centrals[1].address() && g_phone_connected) {
        g_phone_connected = false;
        Serial.println("Phone disconnect event, central: ");
    } else {
        Serial.println("Unkown disconnect: ");
    };
    Serial.println(central.address());
    Serial.println("-------------");
    g_BLElocal.advertise();
};

bool DomGlove::createBLEService(BLEUnsignedIntCharacteristic * dom_fingers, BLEUnsignedIntCharacteristic * sub_fingers){
    // begin initialization
    if (!g_BLElocal.begin()) {
        Serial.println("starting BLE failed!");
        while (1);
    };
    
    g_BLElocal.setDeviceName("DominantGlove");
    g_BLElocal.setEventHandler(BLEConnected, domConnectHandler);
    g_BLElocal.setEventHandler(BLEDisconnected, domDisconnectHandler);
    // set advertised local name and service UUID:
    g_BLElocal.setLocalName(service_name);
    g_BLElocal.setAdvertisedService(g_service);
    
    g_service.addCharacteristic(dom_fingers[0]);
    g_service.addCharacteristic(dom_fingers[1]);
    g_service.addCharacteristic(dom_fingers[2]);
    g_service.addCharacteristic(dom_fingers[3]);
    g_service.addCharacteristic(dom_fingers[4]);

    g_service.addCharacteristic(sub_fingers[0]);
    g_service.addCharacteristic(sub_fingers[1]);
    g_service.addCharacteristic(sub_fingers[2]);
    g_service.addCharacteristic(sub_fingers[3]);
    g_service.addCharacteristic(sub_fingers[4]);

    // add service
    g_BLElocal.addService(g_service);

    Serial.println("BLE Peripheral");
    Serial.println(g_service.uuid());
    return g_BLElocal.advertise();
};

// ---------------------------------------RUN DOM----------------------------------

void DomGlove::run(){
    GLOVE::STATES::DOM state = GLOVE::STATES::DOM::SETUP;
    uint16_t fingers_pos[5];
    RGB rgb;

    if (!g_BLElocal.begin()) {
        Serial.println("starting BLE failed!");
        while (1);
    };

    BLEUnsignedIntCharacteristic dom_finger_0(fingers_UUID[0], BLERead | BLEWrite | BLENotify);
    BLEUnsignedIntCharacteristic dom_finger_1(fingers_UUID[1], BLERead | BLEWrite | BLENotify);
    BLEUnsignedIntCharacteristic dom_finger_2(fingers_UUID[2], BLERead | BLEWrite | BLENotify);
    BLEUnsignedIntCharacteristic dom_finger_3(fingers_UUID[3], BLERead | BLEWrite | BLENotify);
    BLEUnsignedIntCharacteristic dom_finger_4(fingers_UUID[4], BLERead | BLEWrite | BLENotify);
    
    BLEUnsignedIntCharacteristic sub_finger_0(fingers_UUID[5], BLERead | BLEWrite | BLENotify);
    BLEUnsignedIntCharacteristic sub_finger_1(fingers_UUID[6], BLERead | BLEWrite | BLENotify);
    BLEUnsignedIntCharacteristic sub_finger_2(fingers_UUID[7], BLERead | BLEWrite | BLENotify);
    BLEUnsignedIntCharacteristic sub_finger_3(fingers_UUID[8], BLERead | BLEWrite | BLENotify);
    BLEUnsignedIntCharacteristic sub_finger_4(fingers_UUID[9], BLERead | BLEWrite | BLENotify);

    BLEUnsignedIntCharacteristic characteristics_list[5]=
    {
        dom_finger_0,
        dom_finger_1,
        dom_finger_2,
        dom_finger_3,
        dom_finger_4
    };

    BLEUnsignedIntCharacteristic sub_characteristics_list[5]=
    {
        sub_finger_0,
        sub_finger_1,
        sub_finger_2,
        sub_finger_3,
        sub_finger_4
    };

    while(true){
        switch(state) {
            case GLOVE::STATES::DOM::SETUP:{
                bluetooth_phone_LED.setColor(rgb.RED);
                bluetooth_glove_LED.setColor(rgb.RED);
                state = GLOVE::STATES::DOM::INITIALIZE_BLUETOOTH;
                break;
            }

            case GLOVE::STATES::DOM::INITIALIZE_BLUETOOTH:{  
                if (createBLEService(characteristics_list, sub_characteristics_list)){
                    state = GLOVE::STATES::DOM::READ_SENSORS; 
                } else {
                    state = GLOVE::STATES::DOM::SETUP;
                }; 
                break;
            }

            case GLOVE::STATES::DOM::READ_SENSORS:{
                g_BLElocal.poll();
                getFingerPositions(fingers_pos);
                updateCharacteristics(characteristics_list, fingers_pos);
                state = GLOVE::STATES::DOM::READ_SENSORS;
            }

            default:{
                if(g_glove_connected != g_last_glove_status){
                    g_last_glove_status = g_glove_connected;    
                    if(g_glove_connected){
                        bluetooth_glove_LED.setColor(rgb.GREEN);
                    }else{
                        bluetooth_glove_LED.setColor(rgb.RED);
                    };
                };
                if(g_phone_connected != g_last_phone_status){  
                    g_last_phone_status = g_phone_connected;          
                    if (g_phone_connected){
                        bluetooth_phone_LED.setColor(rgb.GREEN);
                    }else{
                        bluetooth_phone_LED.setColor(rgb.RED);
                    };
                };
                break;
            };
        };
    };
};
// ---------------------------------------RUN SUB----------------------------------

void SubGlove::run(){
    GLOVE::STATES::SUB state = GLOVE::STATES::SUB::SETUP;
    uint16_t fingers_pos[5];
    RGB rgb;

    if (!g_BLElocal.begin()) {
        Serial.println("starting BLE failed!");
        while (1);
    };
    
    g_BLElocal.setDeviceName("SubmissiveGlove");

    while(true){
        switch(state){
            case GLOVE::STATES::SUB::SETUP:{
                bluetooth_glove_LED.setColor(rgb.RED);
                state = GLOVE::STATES::SUB::SCAN_AND_CONNECT_BLUETOOTH;
                break;
            }

            case GLOVE::STATES::SUB::SCAN_AND_CONNECT_BLUETOOTH:{
                connectToDom();
                if(g_glove_connected){
                    bluetooth_glove_LED.setColor(rgb.GREEN);
                };
                state = GLOVE::STATES::SUB::READ_SENSORS;
                break;
            }

            case GLOVE::STATES::SUB::READ_SENSORS:{
                getFingerPositions(fingers_pos);
                if(g_BLElocal.connected()){
                    state = GLOVE::STATES::SUB::UPDATE_CHARACTERISTICS;
                } else {
                    state = GLOVE::STATES::SUB::SCAN_AND_CONNECT_BLUETOOTH;
                    g_glove_connected = false;
                    bluetooth_glove_LED.setColor(rgb.RED);
                }
                break;
            }

            case GLOVE::STATES::SUB::UPDATE_CHARACTERISTICS: {
                updateCharacteristics(characteristics, fingers_pos);
                state = GLOVE::STATES::SUB::READ_SENSORS;
                break;
            }

            default: {
                break;
            }
        };
    };
};