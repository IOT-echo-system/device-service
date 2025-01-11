package com.robotutor.feedService.models

import com.robotutor.iot.service.IdSequenceType


enum class IdType(override val length: Int) : IdSequenceType {
    BOARD_ID(10),
}
