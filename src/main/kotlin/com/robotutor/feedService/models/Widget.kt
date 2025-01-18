package com.robotutor.feedService.models

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
    val boardId: BoardId,
    val premisesId: PremisesId,
    val createdBy: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
typealias WidgetId = String
