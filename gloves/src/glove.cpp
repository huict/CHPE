#include <../include/glove.hpp>

// ===================================== Constructors =============================

Glove::Glove(uint8_t* finger_pins, uint8_t battery_pin, uint8_t* glove_led_pins, uint8_t* phone_led_pins, uint8_t* battery_led_pin):
    finger_pins(finger_pins),
    battery_pin(battery_pin),
    glove_led_pins(glove_led_pins),
    phone_led_pins(phone_led_pins),
    battery_led_pin(battery_led_pin)
{};

DomGlove::DomGlove(uint8_t* finger_pins, uint8_t battery_pin, uint8_t& glove_led_pins, uint8_t* phone_led_pins, uint8_t* battery_led_pin):
    Glove(finger_pins, battery_pin, glove_led_pins, phone_led_pins, battery_led_pin)
{};

SubGlove::SubGlove(uint8_t* finger_pins, uint8_t battery_pin, uint8_t* glove_led_pins, uint8_t* phone_led_pins, uint8_t* battery_led_pin):
    Glove(finger_pins, battery_pin, glove_led_pins, phone_led_pins, battery_led_pin)
{};

// ===================================== Functions =============================

uint8_t * Glove::getFingerPositions(){
    uint8_t temp_pos[5];
    for( unsigned int i = 0; i < 5; i++){
        temp_pos[i] = fingers[i].getInformation();
    }
    return temp_pos;
};

// void DomGlove::generateUUID(char& service_UUID){
// 	service_UUID = "bd3d409d-f8a3-4c80-b8db-daea6ddabec3"
// };


void DomGlove::connectHandler(BLEDevice central){
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
        		}
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
            			}
            			if(fingers[i].canSubscribe()){
              				fingers[i].subscribe();
            			} else {
              				Serial.println("NOT Subscribable");
            			}
          			}
        		}
      		}
    	}
  	}
};

void SubGlove::connectHandler(BLEDevice &central, RGB_LED & glove_led){
    Serial.print("Connected event, central: ");
    Serial.println(central.address());
    RGB rgb;
    if (central.deviceName() == "GLOVE"){
        glove_led.setColor(rgb.GREEN);
    }
};

void SubGlove::disconnectHandler(BLEDevice &central, RGB_LED & glove_led){
    Serial.print("Disconnected event, central: ");
    Serial.println(central.address());
    RGB rgb;
    if (central.deviceName() == "GLOVE"){
        glove_led.setColor(rgb.RED);
    }
};

void SubGlove::updateCharacteristics(uint8_t * finger_positions){
    sub_finger_0.writeValue(finger_positions[0])
    sub_finger_1.writeValue(finger_positions[1])
    sub_finger_2.writeValue(finger_positions[2])
    sub_finger_3.writeValue(finger_positions[3])
    sub_finger_4.writeValue(finger_positions[4])
};

void DomGlove::updateCharacteristics(uint8_t * finger_positions){
    dom_finger_0.writeValue(finger_positions[0])
    dom_finger_1.writeValue(finger_positions[1])
    dom_finger_2.writeValue(finger_positions[2])
    dom_finger_3.writeValue(finger_positions[3])
    dom_finger_4.writeValue(finger_positions[4])
};

void DomGlove::createBLEService(){
    // begin initialization
    if (!BLE.begin()) {
        Serial.println("starting BLE failed!");
        while (1);
    }
    BLE.setEventHandler(BLEConnected, ConnectHandler);
    BLE.setEventHandler(BLEDisconnected, DisconnectHandler);
    // set advertised local name and service UUID:
    BLE.setLocalName(service_name);
    BLE.setAdvertisedService(service);

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
};

// ---------------------------------------RUN DOM----------------------------------

void DomGlove::run(){
    GLOVE::STATES::DOM state = GLOVE::STATES::DOM::SETUP;
    uint8_t fingers_pos[5];
    RGB rgb;
    while(true){
        switch(state) {
            case GLOVE::STATES::DOM::SETUP:

                // generateUUID(service_UUID);
                bluetooth_phone_LED.setColor(rgb.RED);
                bluetooth_glove_LED.setColor(rgb.OFF);
                battery_LED.setColor(rgb.OFF);

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
                updateCharacteristics(getFingerPositions());

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
    RGB rgb();
    while(true){
        switch(state){
        case GLOVE::STATES::SUB::SETUP:

            bluetooth_phone_LED.setColor(rgb.OFF);
            bluetooth_glove_LED.setColor(rgb.RED);
            battery_LED.setColor(rgb.RED);
            if (!BLE.begin()) {
                Serial.println("starting BLE failed!");
                while (1);
            }
            
        case GLOVE::STATES::SUB::SCAN_AND_CONNECT_BLUETOOTH:
            connectToDom();
            if( dom_glove.connected() ){
                state = GLOVE::STATES::SUB::READ_SENSORS;
            }
            break;

        case GLOVE::STATES::SUB::READ_SENSORS:
            fingers_pos = getFingerPositions();

        case GLOVE::STATES::SUB::UPDATE_CHARACTERISTICS:
            updateCharacteristics(fingers_pos);
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