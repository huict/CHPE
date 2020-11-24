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

// #include "Arduino_FreeRTOS.h"
// #include "task.h"
// #include "queue.h"
// #include "semphr.h"
// #include "event_groups.h"
#include <stdint-gcc.h>
#include <Arduino.h>

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

#endif