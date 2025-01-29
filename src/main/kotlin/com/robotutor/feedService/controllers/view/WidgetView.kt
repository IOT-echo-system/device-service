package com.robotutor.feedService.controllers.view

import com.robotutor.feedService.models.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class WidgetRequest(
    @field:NotNull(message = "Type is required")
    val type: WidgetType,

    @field:NotBlank(message = "BoardId is required")
    val boardId: BoardId,

    @field:NotBlank(message = "FeedId is required")
    val feedId: FeedId,

    @field:NotBlank(message = "ZoneId is required")
    val zoneId: ZoneId,

    @field:NotBlank(message = "Name is required")
    @field:Size(min = 4, max = 20, message = "Name should not be less than 4 char or more than 20 char")
    val name: String,

    @field:NotNull(message = "Config is required")
    val config: Map<String, Any>
)

data class WidgetNameRequest(
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 4, max = 20, message = "Name should not be less than 4 char or more than 20 char")
    val name: String,
)

data class WidgetView(
    val widgetId: WidgetId,
    val name: String,
    val boardId: BoardId,
    val premisesId: PremisesId,
    val feedId: FeedId,
    val zoneId: ZoneId,
    val type: WidgetType,
    val config: Map<String, Any>,
) {
    companion object {
        fun from(widget: Widget): WidgetView {
            return WidgetView(
                widgetId = widget.widgetId,
                name = widget.name,
                boardId = widget.boardId,
                premisesId = widget.premisesId,
                feedId = widget.feedId,
                zoneId = widget.zoneId,
                type = widget.type,
                config = widget.config
            )
        }
    }
}
