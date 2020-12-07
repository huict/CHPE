// bluetooth task

# ifndef _BLUETOOTH_TASK_HPP
# define _BLUETOOTH_TASK_HPP

# include "support.hpp"
# include "input_object.hpp"
# include <ArduinoBLE.h>

template<int I>
class Bluetooth{
private:
	__uint128_t finger_UUID[I];
public:
	Bluetooth Bluetooth():{};

    BLEService createService(uint128_t service_UUID, uint128_t[I] fingers_UUID, String service_name );

	template<typename T>
	void setConnectHandler(T functie);

	template<typename T>
    void setDisconnectHandler( T functie );

    bool connectToName( String name );

	BLEDevice.characteristics[I] discoverFingers();

	String getCentralDeviceName( BLEDevice &central){
		return central.deviceName
	}

	bool advertise(){
		return BLE.advertise()
	};

	uint8_t[I] getCharacteristics();
};
# endif