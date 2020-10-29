// rtos namespace
// flag namespace
// bluetooth_led_flag namespace
// radio_led_flag namespace
// semaphore namespace
// queue namespace
// mutex namespace
// bluetooth_queue QueueHandle_t
// radio_queue QueueHandle_t
// bluetooth_mutex SemaphoreHandle_t
// Radio_mutex SemaphoreHandle_t

#ifndef _SUPPORT_HPP
#define _SUPPORT_HPP

#include "FreeRTOS.h"
#include "task.h"
#include "queue.h"
#include "semphr.h"
#include "event_groups.h"
#include <stdint-gcc.h>

// namespace RTOS{
// 	namespace FLAG{
// 		namespace BLUETOOTH_LED{

// 		};
// 		namespace RADIO_LED{

// 		};

// 	};
// 	namespace SEMAPHORE{
// 		namespace MUTEX{
// 			SemaphoreHandle_t bluetooth;
// 			SamaphoreHandle_t radio;
// 		};
// 	};
// 	namespace QUEUE{
// 		QueueHandle_t bluetooth;
// 		QueueHandle_t radio;
// 	};
// };

enum DIGITS{
	THUMB = 0x00,
	INDEX_FINGER,
	MIDDLE_FINGER,
	RING_FINGER,
	PINKY
};

namespace RGB{
	uint8_t red = [255,0,0];
	uint8_t green = [0,255,0];
	uint8_t blue = [0,0,255];
	uint8_t orange = [255,94,19];
}

#endif