#ifndef _RADIO_TASK_HPP
#define _RADIO_TASK_HPP

#include <input_object.hpp>
#include <support.hpp>

enum class RADIO_STATES{
	INITIALIZE,
	RECEIVING_SETUP,
	SENDING_SETUP,
	CONNECTED_RECEIVE,
	CONNECTED_SEND
};

// Radio_task class
class Radio_task {
private:
	uint8_t CSN_pin;
	uint8_t CE_pin;
	uint8_t SCK_pin;
	uint8_t MOSI_pin;
	uint8_t MISO_pin;
	char* data;
	uint8_t data_length = 255;
	bool sender;
	const char adress[7] = "abcdef";
	Input_object mode_switch;
public:
	Radio_task(
		int CSN_pin, 
		int CE_pin, 
		uint8_t SCK_pin, 
		uint8_t MOSI_pin, 
		uint8_t MISO_pin, 
		Input_object mode_switch
	):
		CSN_pin(CSN_pin),
		CE_pin(CE_pin),
		SCK_pin(SCK_pin),
		MOSI_pin(MOSI_pin),
		MISO_pin(MISO_pin),
		mode_switch(mode_switch)
	{
		sender = mode_switch.readPin();
	};

	static RADIO_STATES main_state = RADIO_STATES::INITIALIZE;

	// !! WARNING willen we de modeswitch vaker checken ? Dus kan een gebruiker de mode veranderen terwijl de glove aanstaat?
	void main(){
		while(true){
			switch (main_state) {
			// Check the position of the mode_switch pin to determine the mode of the glove.
			case RADIO_STATES::INITIALIZE{
				if (mode_switch.readPin()) {
					main_state = RADIO_STATES::SENDING_SETUP;
				}else{
					main_state = RADIO_STATES::RECEIVING_SETUP;
				}
				break;
			}
			case RADIO_STATES::SENDING_SETUP{

				break;
			}
			case RADIO_STATES::RECEIVING_SETUP{

				break;
			}
			case RADIO_STATES::CONNECTED_SENDING{

				break;
			}
			case RADIO_STATES::CONNECTED_RECEIVING{
				break;
			}
		};
	};
};

#endif