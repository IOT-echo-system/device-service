package com.robotutor.feedService.models.kafka

import com.robotutor.feedService.models.PremisesId
import com.robotutor.feedService.models.Widget
import com.robotutor.feedService.models.WidgetId
import com.robotutor.iot.models.Message
import java.time.ZoneId

data class AddWidgetMessage(
    val widgetId: WidgetId,
    val premisesId: PremisesId,
    val zoneId: ZoneId,
    val name: String,
) : Message() {
    companion object {
        fun from(widget: Widget): AddWidgetMessage {
            return AddWidgetMessage(widgetId = widget.widgetId, premisesId = widget.premisesId, name = widget.name, zoneId = widget.zoneId)
        }
    }
}
