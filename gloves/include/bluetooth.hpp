/**
 * @file bluetooth.hpp
 * @author Maaike Hovenkamp, Duur Alblas
 * @brief 
 * @version 0.1
 * @date 2020-12-16
 * 
 *  @copyright This program is free software: you can redistribute it and/or modify
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

# ifndef _BLUETOOTH_TASK_HPP
# define _BLUETOOTH_TASK_HPP

# include "support.hpp"
# include "input_object.hpp"
# include <ArduinoBLE.h>

/**
 * @class Bluetooth
 * @brief Templated Bluetooth class
 * @details WARNING This Class is obsolete !!
 * @tparam I int
 */
template<int I>
class Bluetooth{
private:
	__uint128_t finger_UUID[I];
public:
    /**
     * @brief Construct a new empty bluetooth object.
     * 
     */
	Bluetooth Bluetooth():{};

	/**
	 * @brief Create a Service object
	 * 
	 * @param service_UUID uint8_t
	 * @param fingers_UUID uint128_t
	 * @param service_name String
	 * @return BLEService 
	 */
    BLEService createService(uint128_t service_UUID, uint128_t[I] fingers_UUID, String service_name );

	/**
	 * @brief Set the Connect Handler
	 * 
	 * @tparam T 
	 * @param function T
	 */
	template<typename T>
	void setConnectHandler(T function);

	/**
	 * @brief Set the Disconnect Handler
	 * 
	 * @tparam T 
	 * @param function T
	 */
	template<typename T>
    void setDisconnectHandler( T function );

	/**
	 * @brief Connect to peripheral with specific name
	 * 
	 * @param name String
	 * @return bool
	 */
    bool connectToName( String name );

	/**
	 * @brief discover atributes from a connected peripheral
	 * 
	 */
	BLEDevice.characteristics[I] discoverFingers();

	/**
	 * @brief Get the Central Device Name object
	 * 
	 * @param central BleDevide &
	 * @return String 
	 */
	String getCentralDeviceName( BLEDevice &central){
		return central.deviceName
	}

	/**
	 * @brief advertise bluetooth object
	 * 
	 * @return bool
	 */
	bool advertise(){
		return BLE.advertise()
	};

	/**
	 * @brief Get the Characteristics object
	 * 
	 * @return uint8_t[I] 
	 */
	uint8_t[I] getCharacteristics();
};
# endif