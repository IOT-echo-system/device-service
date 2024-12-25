package com.robotutor.deviceService.controllers.view

import com.robotutor.deviceService.models.Board
import com.robotutor.deviceService.models.BoardId
import com.robotutor.deviceService.models.PremisesId
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class BoardRequest(
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 4, max = 20, message = "Name should not be less than 4 char or more than 20 char")
    val name: String,
    @field:NotBlank(message = "Premises is required")
    val premisesId: PremisesId,
    @field:NotBlank(message = "type is required")
    val type: String
)

data class BoardView(
    val boardId: BoardId,
    val premisesId: PremisesId,
    val name: String,
    val type: String,
    val createdBy: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(board: Board): BoardView {
            return BoardView(
                boardId = board.boardId,
                premisesId = board.premisesId,
                name = board.name,
                type = board.type,
                createdBy = board.createdBy,
                createdAt = board.createdAt
            )
        }
    }
}
