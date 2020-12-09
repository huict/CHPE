#include <../include/glove.hpp>

// ===================================== Constructors =============================

Glove::Glove(uint8_t battery_pin, uint8_t* glove_led_pins, uint8_t* phone_led_pins, uint8_t* battery_led_pin, uint8_t* finger_pins):
    battery(battery_pin),
    bluetooth_phone_LED(phone_led_pins),
    bluetooth_glove_LED(glove_led_pins),
    battery_LED(battery_led_pin)
{
    fingers[0] = Finger(GLOVE::DIGITS::THUMB, finger_pins[0]);
    fingers[1] = Finger(GLOVE::DIGITS::INDEX_FINGER, finger_pins[1]);
    fingers[2] = Finger(GLOVE::DIGITS::MIDDLE_FINGER, finger_pins[2]);
    fingers[3] = Finger(GLOVE::DIGITS::RING_FINGER, finger_pins[3]);
    fingers[4] = Finger(GLOVE::DIGITS::PINKY, finger_pins[4]);
};

DomGlove::DomGlove(uint8_t battery_pin, uint8_t* glove_led_pins, uint8_t* phone_led_pins, uint8_t* battery_led_pin, uint8_t* finger_pins):
    Glove(battery_pin, glove_led_pins, phone_led_pins, battery_led_pin, finger_pins)
{};

SubGlove::SubGlove(uint8_t battery_pin, uint8_t* glove_led_pins, uint8_t* phone_led_pins, uint8_t* battery_led_pin, uint8_t* finger_pins):
    Glove(battery_pin, glove_led_pins, phone_led_pins, battery_led_pin, finger_pins)
{};

// ===================================== Functions =============================

uint8_t * Glove::getFingerPositions(){
    uint8_t temp_pos[5];
    for( unsigned int i = 0; i < 5; i++){
        temp_pos[i] = fingers[i].getInformation();
    };
    return temp_pos;
};

// void DomGlove::generateUUID(char& service_UUID){
// 	service_UUID = "bd3d409d-f8a3-4c80-b8db-daea6ddabec3"
// };


void DomGlove::connectHandler(BLEDevice & central){
    Serial.print("Connected event, central: ");
    Serial.println(central.address());
};

void SubGlove::connectToDom(){
	BLE.begin();
	Serial.println("Start scanning");
  	// Start scan for peripheral service
  	BLE.scanForName(service_name);

  	// check if a peripheral has been discovered
  	bool glove_connected = false;
  	while(!glove_connected){
	    BLEDevice dom_glove = BLE.available();
    	Serial.println("BLE Available");
    	if (dom_glove){
      		// Check for correct device discovered
      		if (dom_glove.localName() == service_name) {
        		glove_connected = true;
        		Serial.println("Found Glove");
        		BLE.stopScan();
        		if (dom_glove.connect()) {
          			Serial.println("Connected");
        		};
        		if(dom_glove.discoverAttributes()){
          			fingers[0] = dom_glove.characteristic(fingers_UUID[5]);
          			fingers[1] = dom_glove.characteristic(fingers_UUID[6]);
          			fingers[2] = dom_glove.characteristic(fingers_UUID[7]);
          			fingers[3] = dom_glove.characteristic(fingers_UUID[8]);
          			fingers[4] = dom_glove.characteristic(fingers_UUID[9]);
          			for(unsigned int i = 0; i < 5; i++){
            			Serial.println("Finger : "+String(i));
            			if(!fingers[i]){
              				Serial.println("NOT Accessable");
            			};
            			if(fingers[i].canSubscribe()){
              				fingers[i].subscribe();
            			} else {
              				Serial.println("NOT Subscribable");
            			};
          			};
        		};
      		};
    	};
  	};
};

void SubGlove::connectHandler(BLEDevice &central, RGB_LED & glove_led){
    Serial.print("Connected event, central: ");
    Serial.println(central.address());
    RGB rgb;
    if (central.deviceName() == "GLOVE"){
        glove_led.setColor(rgb.GREEN);
    };
};

void SubGlove::disconnectHandler(BLEDevice &central, RGB_LED & glove_led){
    Serial.print("Disconnected event, central: ");
    Serial.println(central.address());
    RGB rgb;
    if (central.deviceName() == "GLOVE"){
        glove_led.setColor(rgb.RED);
    };
};

void SubGlove::updateCharacteristics( BLEUnsignedCharCharacteristic* characteristics, uint8_t * finger_positions){
    characteristics[0].writeValue(finger_positions[0]);
    characteristics[1].writeValue(finger_positions[1]);
    characteristics[2].writeValue(finger_positions[2]);
    characteristics[3].writeValue(finger_positions[3]);
    characteristics[4].writeValue(finger_positions[4]);
};

void DomGlove::updateCharacteristics(BLEUnsignedCharCharacteristic* characteristics, uint8_t * finger_positions){
    characteristics[0].writeValue(finger_positions[0]);
    characteristics[1].writeValue(finger_positions[1]);
    characteristics[2].writeValue(finger_positions[2]);
    characteristics[3].writeValue(finger_positions[3]);
    characteristics[4].writeValue(finger_positions[4]);
};

void DomGlove::createBLEService(BLEUnsignedCharCharacteristic* characteristics){
    // begin initialization
    if (!BLE.begin()) {
        Serial.println("starting BLE failed!");
        while (1);
    };
    BLE.setEventHandler(BLEConnected, ConnectHandler);
    BLE.setEventHandler(BLEDisconnected, DisconnectHandler);
    // set advertised local name and service UUID:
    BLE.setLocalName(service_name);
    BLE.setAdvertisedService(service);

    // add the characteristic to the service
    characteristics[0].addCharacteristic(dom_finger_0);
    characteristics[1].addCharacteristic(dom_finger_1);
    characteristics[2].addCharacteristic(dom_finger_2);
    characteristics[3].addCharacteristic(dom_finger_3);
    characteristics[4].addCharacteristic(dom_finger_4);
    
    characteristics[5].addCharacteristic(sub_finger_0);
    characteristics[6].addCharacteristic(sub_finger_1);
    characteristics[7].addCharacteristic(sub_finger_2);
    characteristics[8].addCharacteristic(sub_finger_3);
    characteristics[9].addCharacteristic(sub_finger_4);

    // add service
    BLE.addService(service);

    Serial.println("BLE Peripheral");
    Serial.println(service.uuid());
};

// ---------------------------------------RUN DOM----------------------------------

void DomGlove::run(){
    GLOVE::STATES::DOM state = GLOVE::STATES::DOM::SETUP;
    uint8_t fingers_pos[5];
    RGB rgb;

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

    while(true){
        switch(state) {
            case GLOVE::STATES::DOM::SETUP:

                // generateUUID(service_UUID);
                bluetooth_phone_LED.setColor(rgb.RED);
                bluetooth_glove_LED.setColor(rgb.OFF);
                battery_LED.setColor(rgb.OFF);
                state = GLOVE::STATES::DOM::INITIALIZE_BLUETOOTH;
                break;
            case GLOVE::STATES::DOM::INITIALIZE_BLUETOOTH:
                createBLEService();
                
                bluetooth_glove_LED.setColor(rgb.RED);
                bluetooth_phone_LED.setColor(rgb.RED);

                if( BLE.advertise()){
                    state = GLOVE::STATES::DOM::READ_SENSORS;
                } else {
                    state = GLOVE::STATES::DOM::INITIALIZE_BLUETOOTH;
                };

                break;

            case GLOVE::STATES::DOM::READ_SENSORS:
                BLEUnsignedCharCharacteristic characteristics_list[5]=
                {
                    dom_finger_0,
                    dom_finger_1,
                    dom_finger_2,
                    dom_finger_3,
                    dom_finger_4
                };
                updateCharacteristics(characteristics_list, getFingerPositions());

                if(BLE.advertise()){
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
    RGB rgb;

    BLEUnsignedCharCharacteristic sub_finger_0(fingers_UUID[5]);
	BLEUnsignedCharCharacteristic sub_finger_1(fingers_UUID[6]);
	BLEUnsignedCharCharacteristic sub_finger_2(fingers_UUID[7]);
	BLEUnsignedCharCharacteristic sub_finger_3(fingers_UUID[8]);
	BLEUnsignedCharCharacteristic sub_finger_4(fingers_UUID[9]);

    while(true){
        switch(state){
        case GLOVE::STATES::SUB::SETUP:

            bluetooth_phone_LED.setColor(rgb.OFF);
            bluetooth_glove_LED.setColor(rgb.RED);
            battery_LED.setColor(rgb.RED);
            if (!BLE.begin()) {
                Serial.println("starting BLE failed!");
                while (1);
            };
            state = GLOVE::STATES::SUB::SCAN_AND_CONNECT_BLUETOOTH;
            break;
        case GLOVE::STATES::SUB::SCAN_AND_CONNECT_BLUETOOTH:
            connectToDom();
            if( dom_glove.connected() ){
                state = GLOVE::STATES::SUB::READ_SENSORS;
            }
            state = GLOVE::STATES::SUB::READ_SENSORS;
            break;
        case GLOVE::STATES::SUB::READ_SENSORS:
            fingers_pos = getFingerPositions();
            state = GLOVE::STATES::SUB::UPDATE_CHARACTERISTICS;
            break;
        case GLOVE::STATES::SUB::UPDATE_CHARACTERISTICS:
            BLEUnsignedCharCharacteristic characteristics_list[5]=
            {
                sub_finger_0,
                sub_finger_1,
                sub_finger_2,
                sub_finger_3,
                sub_finger_4
            };
            updateCharacteristics(characteristics_list, fingers_pos);
            if( dom_glove.connected()){
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