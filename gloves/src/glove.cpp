#include <../include/glove.hpp>

BLEService service("bd3d409d-f8a3-4c80-b8db-daea6ddabec3");

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

void Glove::updateCharacteristics( BLECharacteristic* characteristics, uint8_t * finger_positions){
    characteristics[0].writeValue(finger_positions[0]);
    characteristics[1].writeValue(finger_positions[1]);
    characteristics[2].writeValue(finger_positions[2]);
    characteristics[3].writeValue(finger_positions[3]);
    characteristics[4].writeValue(finger_positions[4]);
};

void SubGlove::connectToDom(){
	BLE.begin();
	Serial.println("Start scanning");
  	// Start scan for peripheral service
  	BLE.scanForName(service_name);

  	// check if a peripheral has been discovered
  	bool glove_connected = false;
  	while(!glove_connected){
	    BLEDevice dom_glove_device = BLE.available();
    	Serial.println("BLE Available");
    	if (dom_glove_device){
      		// Check for correct device discovered
      		if (dom_glove_device.localName() == service_name) {
        		glove_connected = true;
        		Serial.println("Found Glove");
        		BLE.stopScan();
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

bool DomGlove::createBLEService(){
    // begin initialization
    if (!BLE.begin()) {
        Serial.println("starting BLE failed!");
        while (1);
    };
    BLE.setEventHandler(BLEConnected, [](BLEDevice central){Serial.print("Connected event, central: ");Serial.println(central.address());});
    BLE.setEventHandler(BLEDisconnected, [](BLEDevice central){Serial.print("Disconnect event, central: ");Serial.println(central.address());});
    // set advertised local name and service UUID:
    BLE.setLocalName(service_name);
    BLE.setAdvertisedService(service);

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

    // add the characteristic to the service
    service.addCharacteristic(dom_finger_0);
    service.addCharacteristic(dom_finger_1);
    service.addCharacteristic(dom_finger_2);
    service.addCharacteristic(dom_finger_3);
    service.addCharacteristic(dom_finger_4);
    
    service.addCharacteristic(sub_finger_0);
    service.addCharacteristic(sub_finger_1);
    service.addCharacteristic(sub_finger_2);
    service.addCharacteristic(sub_finger_3);
    service.addCharacteristic(sub_finger_4);

    // add service
    BLE.addService(service);

    Serial.println("BLE Peripheral");
    Serial.println(service.uuid());
    if (BLE.advertise()){
        return true;
    } else {
        return false;
    }
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
        BLE.poll();
        switch(state) {
            case GLOVE::STATES::DOM::SETUP:
            {
                Serial.println("STATE : SETUP");
                // generateUUID(service_UUID);
                bluetooth_phone_LED.setColor(rgb.RED);
                bluetooth_glove_LED.setColor(rgb.OFF);
                battery_LED.setColor(rgb.OFF);
                state = GLOVE::STATES::DOM::INITIALIZE_BLUETOOTH;
                break;
            }
            case GLOVE::STATES::DOM::INITIALIZE_BLUETOOTH:
            {  
                Serial.println("STATE : INIT");
                if (createBLEService()){
                    state = GLOVE::STATES::DOM::READ_SENSORS; 
                } else {
                    state = GLOVE::STATES::DOM::SETUP;
                }    
                bluetooth_glove_LED.setColor(rgb.RED);
                bluetooth_phone_LED.setColor(rgb.RED);


                break;
            }
            case GLOVE::STATES::DOM::READ_SENSORS:
            {
                Serial.println("STATE : READ");
                BLEUnsignedCharCharacteristic characteristics_list[5]=
                {
                    dom_finger_0,
                    dom_finger_1,
                    dom_finger_2,
                    dom_finger_3,
                    dom_finger_4
                };
                getFingerPositions(fingers_pos);
                updateCharacteristics(characteristics_list, fingers_pos);
                state = GLOVE::STATES::DOM::READ_SENSORS;
                break;
            }
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
    unsigned char test_val = 1;
    BLECharacteristic sub_finger_0;
	BLECharacteristic sub_finger_1;
	BLECharacteristic sub_finger_2;
	BLECharacteristic sub_finger_3;
	BLECharacteristic sub_finger_4;

    while(true){
        switch(state){
        case GLOVE::STATES::SUB::SETUP:
        {
            Serial.println("setup");
            bluetooth_phone_LED.setColor(rgb.OFF);
            bluetooth_glove_LED.setColor(rgb.RED);
            battery_LED.setColor(rgb.RED);
            if (!BLE.begin()) {
                Serial.println("starting BLE failed!");
                while (1);
            };
            state = GLOVE::STATES::SUB::SCAN_AND_CONNECT_BLUETOOTH;
            break;
        }
        case GLOVE::STATES::SUB::SCAN_AND_CONNECT_BLUETOOTH:
        {
            Serial.println("connectToDom");
            connectToDom();
            // if( dom_glove.connected() ){
            //     state = GLOVE::STATES::SUB::READ_SENSORS;
            // }
            state = GLOVE::STATES::SUB::READ_SENSORS;
            break;
        }
        case GLOVE::STATES::SUB::READ_SENSORS:
        {
            Serial.println("read sensors");
            getFingerPositions(fingers_pos);
            state = GLOVE::STATES::SUB::UPDATE_CHARACTERISTICS;
            break;
        }
        case GLOVE::STATES::SUB::UPDATE_CHARACTERISTICS:
        {
            Serial.println("update characteristics");
            BLECharacteristic characteristics_list[5] = {
                sub_finger_0,
                sub_finger_1,
                sub_finger_2,
                sub_finger_3,
                sub_finger_4
            };
            // updateCharacteristics(characteristics_list, fingers_pos);
            
            characteristics_list[0].writeValue(int32_t(test_val+0));
            characteristics_list[1].writeValue(int32_t(test_val+1));
            characteristics_list[2].writeValue(int32_t(test_val+2));
            characteristics_list[3].writeValue(int32_t(test_val+3));
            characteristics_list[4].writeValue(int32_t(test_val+4));
            
            // if( dom_glove.connected()){
            state = GLOVE::STATES::SUB::READ_SENSORS;
            // } else {
            //     state = GLOVE::STATES::SUB::SCAN_AND_CONNECT_BLUETOOTH;
            // };
            test_val += 10;
            break;
        }
        default:
        {
            break;
        }
        };
    };
};