package com.robotutor.feedService.models.kafka

import com.robotutor.feedService.models.Board
import com.robotutor.feedService.models.BoardId
import com.robotutor.feedService.models.PremisesId
import com.robotutor.iot.models.Message

data class AddBoardMessage(
    val boardId: BoardId,
    val premisesId: PremisesId,
    val name: String,
) : Message() {
    companion object {
        fun from(board: Board): AddBoardMessage {
            return AddBoardMessage(boardId = board.boardId, premisesId = board.premisesId, name = board.name)
        }
    }
}
