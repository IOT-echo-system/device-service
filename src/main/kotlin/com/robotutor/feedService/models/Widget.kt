package com.robotutor.feedService.models

import com.robotutor.feedService.controllers.view.FeedRequest
import com.robotutor.iot.utils.models.PremisesData
import com.robotutor.iot.utils.models.UserData
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
) {
    companion object {
        fun from(feedId: FeedId, feedRequest: FeedRequest, userData: UserData, premisesData: PremisesData): Feed {
            return Feed(
                feedId = feedId,
                boardId = feedRequest.boardId,
                premisesId = premisesData.premisesId,
                name = feedRequest.name,
                type = feedRequest.type,
                value = 0,
                createdBy = userData.userId
            )
        }
    }
}

typealias WidgetId = String
