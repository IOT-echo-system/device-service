package com.robotutor.deviceService.exceptions

import com.robotutor.iot.exceptions.ServiceError


enum class IOTError(override val errorCode: String, override val message: String) : ServiceError {

}
