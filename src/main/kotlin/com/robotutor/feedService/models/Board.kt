package com.robotutor.feedService.models

import com.robotutor.feedService.controllers.view.BoardRequest
import com.robotutor.iot.utils.models.UserData
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

const val BOARD_COLLECTION = "boards"

@TypeAlias("Board")
@Document(BOARD_COLLECTION)
data class Board(
    @Id
    var id: ObjectId? = null,
    @Indexed(unique = true)
    val boardId: BoardId,
    val premisesId: PremisesId,
    var name: String,
    val type: String,
    val createdBy: String,
    val state: BoardState = BoardState.OFFLINE,
    val firmwareVersion: String = "NA",
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    fun updateName(name: String): Board {
        this.name = name
        return this
    }

    companion object {
        fun from(boardId: BoardId, premisesId: PremisesId, boardRequest: BoardRequest, userData: UserData): Board {
            return Board(
                boardId = boardId,
                premisesId = premisesId,
                name = boardRequest.name,
                type = boardRequest.type,
                createdBy = userData.userId
            )
        }
    }
}

enum class BoardState {
    ONLINE,
    OFFLINE,
}

typealias BoardId = String
typealias PremisesId = String
typealias ZoneId = String
