#include <../include/glove.hpp>

BLEService service("bd3d409d-f8a3-4c80-b8db-daea6ddabec3");
BLELocalDevice BLElocal;
BLEDevice connected_centrals[2];
bool glove_connected = false;
bool phone_connected = false;

// ===================================== Constructors =============================

Glove::Glove(uint8_t battery_pin, uint8_t* glove_led_pins, uint8_t* phone_led_pins, uint8_t* battery_led_pin, uint8_t* finger_pins):
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

DomGlove::DomGlove( uint8_t battery_pin, uint8_t* glove_led_pins, uint8_t* phone_led_pins, uint8_t* battery_led_pin, uint8_t* finger_pins):
    Glove(battery_pin, glove_led_pins, phone_led_pins, battery_led_pin, finger_pins)
{};

SubGlove::SubGlove(uint8_t battery_pin, uint8_t* glove_led_pins, uint8_t* phone_led_pins, uint8_t* battery_led_pin, uint8_t* finger_pins):
    Glove(battery_pin, glove_led_pins, phone_led_pins, battery_led_pin, finger_pins)
{};

// ===================================== Functions =============================

void Glove::getFingerPositions( uint8_t * finger_pos){
    for(unsigned int i = 0; i < 5; i++){
        finger_pos[i] = fingers[i]->getInformation();
    };
};

void Glove::updateCharacteristics( BLECharacteristic * characteristics, uint8_t * finger_positions){
    characteristics[0].writeValue(finger_positions[0]);
    characteristics[1].writeValue(finger_positions[1]);
    characteristics[2].writeValue(finger_positions[2]);
    characteristics[3].writeValue(finger_positions[3]);
    characteristics[4].writeValue(finger_positions[4]);
};

void SubGlove::connectToDom(){
  	// Start scan for peripheral service
    if (BLElocal.scanForName(service_name)){
        Serial.println("Start scanning");
        // check if a peripheral has been discovered
        bool glove_connected = false;
        while(!glove_connected){
            BLEDevice dom_glove_device = BLElocal.available();
            if (dom_glove_device){
                // Check for correct device discovered
                if (dom_glove_device.localName() == service_name) {
                    glove_connected = true;
                    Serial.println("Found Glove");
                    BLElocal.stopScan();
                    if (dom_glove_device.connect()) {
                        Serial.println("Connected");
                    };
                    if(dom_glove_device.discoverAttributes()){
                        characteristcs[0] = dom_glove_device.characteristic(fingers_UUID[5]);
                        characteristcs[1] = dom_glove_device.characteristic(fingers_UUID[6]);
                        characteristcs[2] = dom_glove_device.characteristic(fingers_UUID[7]);
                        characteristcs[3] = dom_glove_device.characteristic(fingers_UUID[8]);
                        characteristcs[4] = dom_glove_device.characteristic(fingers_UUID[9]);
                        for(unsigned int i = 0; i < 5; i++){
                            Serial.println("Finger : "+String(i));
                            if(characteristcs[i] == false){
                                Serial.println("NOT Accessable");
                            };
                            if(characteristcs[i].canSubscribe()){
                                characteristcs[i].subscribe();
                            } else {
                                Serial.println("NOT Subscribable");
                            };
                        };
                    };
                };
            };
        };
    };
};

void SubGlove::connectHandler(BLEDevice central, RGB_LED & glove_led){
    Serial.print("Connected event, central: ");
    Serial.println(central.address());
    RGB rgb;
    if (central.deviceName() == "GLOVE"){
        glove_led.setColor(rgb.GREEN);
    };
    BLElocal.advertise();
};

void SubGlove::disconnectHandler(BLEDevice central, RGB_LED & glove_led){
    Serial.print("Disconnected event, central: ");
    Serial.println(central.address());
    RGB rgb;
    if (central.deviceName() == "GLOVE"){
        glove_led.setColor(rgb.RED);
    };
    BLElocal.advertise();
};

void domConnectHandler(BLEDevice central){
    // if(central.address() == arduino::String("dd:f4:a3:9e:86:7c")){
    //     connected_centrals[0] = central;
    //     glove_connected = true;
    //     Serial.print("Glove connected event, central: ");
    // }else{
    //     if(connected_centrals[1].connected()){
    //         central.disconnect();
    //         Serial.print("Denied connection event, central: ");
    //     } else{
    //         connected_centrals[1] = central;
    //         phone_connected = true;
    //         Serial.print("Connected event, central: ");
    //     }
    // };
    Serial.print("Connect event, central: ");
    Serial.println(central.address());
    
    BLElocal.advertise();
};

void domDisconnectHandler(BLEDevice central){
    // if(central.address() == arduino::String("dd:f4:a3:9e:86:7c")){
    //     glove_connected = false;
    //     Serial.print("Glove disconnect event, central: ");
    // }else{
    //     phone_connected = false;
    //     Serial.print("Phone disconnect event, central: ");
    // };
    Serial.print("Disconnect event, central: ");
    Serial.println(central.address());
    BLElocal.advertise();
};

bool DomGlove::createBLEService(BLEUnsignedCharCharacteristic * dom_fingers, BLEUnsignedCharCharacteristic * sub_fingers){
    // begin initialization
    if (!BLElocal.begin()) {
        Serial.println("starting BLE failed!");
        while (1);
    };
    
    BLElocal.setEventHandler(BLEConnected, domConnectHandler);
    BLElocal.setEventHandler(BLEDisconnected, domDisconnectHandler);
    // set advertised local name and service UUID:
    BLElocal.setLocalName(service_name);
    BLElocal.setAdvertisedService(service);
    
    service.addCharacteristic(dom_fingers[0]);
    service.addCharacteristic(dom_fingers[1]);
    service.addCharacteristic(dom_fingers[2]);
    service.addCharacteristic(dom_fingers[3]);
    service.addCharacteristic(dom_fingers[4]);

    service.addCharacteristic(sub_fingers[0]);
    service.addCharacteristic(sub_fingers[1]);
    service.addCharacteristic(sub_fingers[2]);
    service.addCharacteristic(sub_fingers[3]);
    service.addCharacteristic(sub_fingers[4]);

    // add service
    BLElocal.addService(service);

    Serial.println("BLE Peripheral");
    Serial.println(service.uuid());
    if (BLElocal.advertise()){
        return true;
    } else {
        return false;
    };
};

// ---------------------------------------RUN DOM----------------------------------

void DomGlove::run(){
    GLOVE::STATES::DOM state = GLOVE::STATES::DOM::SETUP;
    uint8_t fingers_pos[5];
    RGB rgb;
    // while(!BLE.begin());

    BLEUnsignedCharCharacteristic dom_finger_0(fingers_UUID[0], BLERead | BLEWrite | BLENotify);
    BLEUnsignedCharCharacteristic dom_finger_1(fingers_UUID[1], BLERead | BLEWrite | BLENotify);
    BLEUnsignedCharCharacteristic dom_finger_2(fingers_UUID[2], BLERead | BLEWrite | BLENotify);
    BLEUnsignedCharCharacteristic dom_finger_3(fingers_UUID[3], BLERead | BLEWrite | BLENotify);
    BLEUnsignedCharCharacteristic dom_finger_4(fingers_UUID[4], BLERead | BLEWrite | BLENotify);
    
    BLEUnsignedCharCharacteristic sub_finger_0(fingers_UUID[5], BLERead | BLEWrite | BLENotify);
    BLEUnsignedCharCharacteristic sub_finger_1(fingers_UUID[6], BLERead | BLEWrite | BLENotify);
    BLEUnsignedCharCharacteristic sub_finger_2(fingers_UUID[7], BLERead | BLEWrite | BLENotify);
    BLEUnsignedCharCharacteristic sub_finger_3(fingers_UUID[8], BLERead | BLEWrite | BLENotify);
    BLEUnsignedCharCharacteristic sub_finger_4(fingers_UUID[9], BLERead | BLEWrite | BLENotify);


    BLEUnsignedCharCharacteristic characteristics_list[5]=
    {
        dom_finger_0,
        dom_finger_1,
        dom_finger_2,
        dom_finger_3,
        dom_finger_4
    };

    BLEUnsignedCharCharacteristic sub_characteristics_list[5]=
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
                // Serial.println("STATE : SETUP");
                // generateUUID(service_UUID);
                bluetooth_phone_LED.setColor(rgb.RED);
                bluetooth_glove_LED.setColor(rgb.RED);
                battery_LED.setColor(rgb.RED);
                state = GLOVE::STATES::DOM::INITIALIZE_BLUETOOTH;
                break;
            }

            case GLOVE::STATES::DOM::INITIALIZE_BLUETOOTH:{  
                // Serial.println("STATE : INIT");
                if (createBLEService(characteristics_list, sub_characteristics_list)){
                    state = GLOVE::STATES::DOM::READ_SENSORS; 
                } else {
                    state = GLOVE::STATES::DOM::SETUP;
                };
                    
                break;
            }

            case GLOVE::STATES::DOM::READ_SENSORS:{
                BLElocal.poll();
                // Serial.println("STATE : READ");
                getFingerPositions(fingers_pos);
                updateCharacteristics(characteristics_list, fingers_pos);
                state = GLOVE::STATES::DOM::READ_SENSORS;
            }

            default:{
                if(glove_connected){
                    bluetooth_glove_LED.setColor(rgb.GREEN);
                }else{
                    bluetooth_glove_LED.setColor(rgb.RED);
                }

                if (phone_connected){
                    bluetooth_phone_LED.setColor(rgb.GREEN);
                }else{
                    bluetooth_phone_LED.setColor(rgb.RED);
                }
                break;
            };
        };
    };
};
// ---------------------------------------RUN SUB----------------------------------

void SubGlove::run(){
    GLOVE::STATES::SUB state = GLOVE::STATES::SUB::SETUP;
    uint8_t fingers_pos[5];
    RGB rgb;
    unsigned char test_val = 1;

    BLECharacteristic sub_finger_0;
	BLECharacteristic sub_finger_1;
	BLECharacteristic sub_finger_2;
	BLECharacteristic sub_finger_3;
	BLECharacteristic sub_finger_4;

    BLECharacteristic characteristics_list[5] = {
        sub_finger_0,
        sub_finger_1,
        sub_finger_2,
        sub_finger_3,
        sub_finger_4
    };

    if (!BLElocal.begin()) {
        Serial.println("starting BLE failed!");
        while (1);
    };

    while(true){
        switch(state){
            case GLOVE::STATES::SUB::SETUP:{
                // Serial.println("SETUP");
                bluetooth_phone_LED.setColor(rgb.GREEN);
                bluetooth_glove_LED.setColor(rgb.RED);
                battery_LED.setColor(rgb.BLUE);
                state = GLOVE::STATES::SUB::SCAN_AND_CONNECT_BLUETOOTH;
                break;
            }

            case GLOVE::STATES::SUB::SCAN_AND_CONNECT_BLUETOOTH:{
                // Serial.println("SCAN_CONNECT");
                connectToDom();
                state = GLOVE::STATES::SUB::READ_SENSORS;
                break;
            }

            case GLOVE::STATES::SUB::READ_SENSORS:{
                Serial.println("READ");
                getFingerPositions(fingers_pos);
                if(BLElocal.connected()){
                    state = GLOVE::STATES::SUB::UPDATE_CHARACTERISTICS;
                } else {
                    state = GLOVE::STATES::SUB::SCAN_AND_CONNECT_BLUETOOTH;
                }
                break;
            }

            case GLOVE::STATES::SUB::UPDATE_CHARACTERISTICS: {
                Serial.println("UPDATE");
                // updateCharacteristics(characteristics_list, fingers_pos);
                characteristics_list[0].writeValue(int32_t(test_val+0));
                characteristics_list[1].writeValue(int32_t(test_val+1));
                characteristics_list[2].writeValue(int32_t(test_val+2));
                characteristics_list[3].writeValue(int32_t(test_val+3));
                characteristics_list[4].writeValue(int32_t(test_val+4));
                // if( dom_glove.connected()){
                if(BLElocal.connected()){
                    state = GLOVE::STATES::SUB::READ_SENSORS;
                } else {
                    state = GLOVE::STATES::SUB::SCAN_AND_CONNECT_BLUETOOTH;
                }
                // } else {
                //     state = GLOVE::STATES::SUB::SCAN_AND_CONNECT_BLUETOOTH;
                // };
                test_val += 10;
                break;
            }

            default: {
                break;
            }
        };
    };
};