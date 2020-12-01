#include <../include/glove.hpp>

// ===================================== Constructors =============================

Glove::Glove(uint8_t& finger_pins, uint8_t battery_pin, uint8_t& bluetooth_led_pins, uint8_t& battery_led_pin):
    finger_pins(finger_pins),
    battery_pin(battery_pin),
    bluetooth_led_pins(bluetooth_led_pins),
    battery_led_pin(battery_led_pin)
{};

DomGlove::DomGlove(uint8_t& finger_pins, uint8_t battery_pin, uint8_t& bluetooth_led_pins, uint8_t& battery_led_pin):
    Glove(finger_pins, battery_pin, bluetooth_led_pins, battery_led_pin)
{};

SubGlove::SubGlove(uint8_t& finger_pins, uint8_t battery_pin, uint8_t& bluetooth_led_pins, uint8_t& battery_led_pin):
    Glove(finger_pins, battery_pin, bluetooth_led_pins, battery_led_pin)
{};

// ===================================== Functions =============================

void DomGlove::generateUUID(char& service_UUID){

};

void DomGlove::updateCharacteristics(){

};

void DomGlove::connectHandler(RGB_LED & glove_led, RGB_LED & phone_led){
    Serial.print("Connected event, central: ");
    Serial.println(central.address());
    if (central.deviceName() == "GLOVE"){
        glove_LED.setColor(RGB::GREEN);
    } else {
        phone_LED.setColor(RGB::GREEN)
    }
};

void DomGlove::disconnectHandler(RGB_LED & glove_led, RGB_LED & phone_led){
    Serial.print("Disconnected event, central: ");
    Serial.println(central.address());
    if (central.deviceName() == "GLOVE"){
        glove_led.setColor(RGB::RED);
    } else {
        phone_led.setColor(RGB::RED);
    }
};

void SubGlove::connectHandler(RGB_LED & glove_led){
    Serial.print("Connected event, central: ");
    Serial.println(central.address());
    if (central.deviceName() == "GLOVE"){
        glove_LED.setColor(RGB::GREEN);
    }
};

void SubGlove::disconnectHandler(RGB_LED & glove_led){
    Serial.print("Disconnected event, central: ");
    Serial.println(central.address());
    if (central.deviceName() == "GLOVE"){
        glove_led.setColor(RGB::RED);
    }
};

void SubGlove::updateCharacteristics(){

};

// ---------------------------------------RUN DOM----------------------------------

void DomGlove::run(){
    GLOVE::STATES::DOM state = GLOVE::STATES::DOM::SETUP;
    uint8_t fingers_pos[5];
    while(true){
        switch(state) {
            case GLOVE::STATES::DOM::SETUP:

                generateUUID(service_UUID);
                bluetooth_phone_LED.setColor(RGB::RED);
                bluetooth_glove_LED.setColor(RGB.OFF);
                battery_LED.setColor(RGB.OFF);
                state = GLOVE::STATES::DOM::INITIALIZE_BLUETOOTH;
                break;

            case GLOVE::STATES::DOM::INITIALIZE_BLUETOOTH:

                bluetooth.setConnectHandler( connectHandler() );
                bluetooth.setDisconnectHandler( disconnectHandler(bluetooth_glove_LED, bluetooth_phone_LED) );
                bluetooth.createService( service_UUID, fingers_UUID, "Glove" );
                if( bluetooth.advertise() == true){
                    state = GLOVE::STATES::DOM::READ_SENSORS;
                } else {
                    state = GLOVE::STATES::DOM::IINITIALIZE_BLUETOOTH;
                };
                break;

            case GLOVE::STATES::DOM::READ_SENSORS:

                fingers_pos = Glove::getFingersPositions();
                state = GLOVE::STATES::DOM::UPDATE_CHARACTERISTICS;
                break;

            case GLOVE::STATES::DOM::UPDATE_CHARACTERISTICS:

                bluetooth.updateCharacteristics(fingers_pos);
                if( bluetooth.advertise() == true){
                    state = GLOVE::STATES::DOM::READ_SENSORS;
                } else {
                    state = GLOVE::STATES::DOM::INITIALIZE_BLUETOOTH;
                };
                break;

            default:
                break;
        };
    };
};
// ---------------------------------------RUN SUB----------------------------------

void SubGlove::run(){
    GLOVE::STATES::SUB state = GLOVE::STATES::SUB::SETUP;
    uint8_t fingers_pos[5];
    while(true){
        switch(state){
        case GLOVE::STATES::SUB::SETUP:

            bluetooth_phone_LED.setColor(RGB.OFF);
            bluetooth_glove_LED.setColor(RGB.RED);
            bluetooth_battery_LED.setColor(RGB.RED);
            state = GLOVE::STATES::SUB::SCAN_AND_CONNECT_BLUETOOTH;
            break;

        case GLOVE::STATES::SUB::SCAN_AND_CONNECT_BLUETOOTH:
            bluetooth.connectToName("Glove");
            bluetooth.discoverFingers();
            if( bluetooth.connected() ){
                state = GLOVE::STATES::SUB::READ_SENSORS;
            }
            break;

        case GLOVE::STATES::SUB::READ_SENSORS:
            fingers_pos = getFingersPositions();
            state = GLOVE::STATES::SUB::UPDATE_CHARACTERISTICS;
            break;

        case GLOVE::STATES::SUB::UPDATE_CHARACTERISTICS:
            updateCharacteristics(fingers_pos);
            if( bluetooth.connected()){
                state = GLOVE::STATES::SUB::READ_SENSORS;
            } else {
                state = GLOVE::STATES::SUB::SCAN_AND_CONNECT_BLUETOOTH;
            }
            break;

        default:
            break;
        };

    };
};