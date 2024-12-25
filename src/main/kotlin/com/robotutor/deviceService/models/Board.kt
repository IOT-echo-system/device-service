package com.robotutor.deviceService.models

import com.robotutor.deviceService.controllers.view.BoardRequest
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
    val name: String,
    val type: String,
    val createdBy: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        fun from(boardId: BoardId, boardRequest: BoardRequest, userData: UserData): Board {
            return Board(
                boardId = boardId,
                premisesId = boardRequest.premisesId,
                name = boardRequest.name,
                type = boardRequest.type,
                createdBy = userData.userId
            )
        }
    }
}

typealias BoardId = String
typealias PremisesId = String
