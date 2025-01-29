package com.robotutor.feedService.models

import com.robotutor.iot.service.IdSequenceType


enum class IdType(override val length: Int) : IdSequenceType {
    BOARD_ID(10),
    FEED_ID(10),
    WIDGET_ID(10),
}
