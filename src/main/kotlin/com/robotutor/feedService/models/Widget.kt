package com.robotutor.feedService.models

import com.robotutor.feedService.controllers.view.WidgetRequest
import com.robotutor.iot.utils.models.PremisesData
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

const val WIDGET_COLLECTION = "widgets"

@TypeAlias("Widget")
@Document(WIDGET_COLLECTION)
data class Widget(
    @Id
    var id: ObjectId? = null,
    @Indexed(unique = true)
    val widgetId: WidgetId,
    var name: String,
    val premisesId: PremisesId,
    val feedId: FeedId,
    val zoneId: ZoneId,
    val type: WidgetType,
    val config: Map<String, Any>,
    val createdBy: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    fun updateName(name: String): Widget {
        this.name = name
        return this
    }

    companion object {
        fun from(widgetId: String, widgetRequest: WidgetRequest, premisesData: PremisesData): Widget {
            return Widget(
                widgetId = widgetId,
                name = widgetRequest.name,
                premisesId = premisesData.premisesId,
                feedId = widgetRequest.feedId,
                zoneId = widgetRequest.zoneId,
                type = widgetRequest.type,
                config = widgetRequest.config,
                createdBy = premisesData.user.userId,
            )
        }
    }
}
typealias WidgetId = String

enum class WidgetType {
    TOGGLE,
    MOMENTARY_SWITCH,
    SLIDER,
    GAUGE
}
