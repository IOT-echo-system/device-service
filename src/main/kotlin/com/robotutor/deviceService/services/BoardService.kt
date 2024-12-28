package com.robotutor.deviceService.services

import com.robotutor.deviceService.controllers.view.BoardRequest
import com.robotutor.deviceService.gateway.PremisesGateway
import com.robotutor.deviceService.models.Board
import com.robotutor.deviceService.models.IdType
import com.robotutor.deviceService.repositories.BoardRepository
import com.robotutor.iot.auditOnError
import com.robotutor.iot.auditOnSuccess
import com.robotutor.iot.service.IdGeneratorService
import com.robotutor.iot.utils.models.UserData
import com.robotutor.iot.utils.utils.toMap
import com.robotutor.loggingstarter.logOnError
import com.robotutor.loggingstarter.logOnSuccess
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class BoardService(
    private val boardRepository: BoardRepository,
    private val idGeneratorService: IdGeneratorService,
    private val premisesGateway: PremisesGateway
) {
    fun addBoard(boardRequest: BoardRequest, userData: UserData): Mono<Board> {
        val boardRequestMap = boardRequest.toMap().toMutableMap()
        return premisesGateway.getPremisesByOwner(boardRequest.premisesId)
            .flatMap { idGeneratorService.generateId(IdType.BOARD_ID) }
            .flatMap { boardId ->
                boardRequestMap["boardId"] = boardId
                val board = Board.from(boardId, boardRequest, userData)
                boardRepository.save(board)
                    .auditOnSuccess("BOARD_CREATE", boardRequestMap)
            }
            .auditOnError("BOARD_CREATE", boardRequestMap)
            .logOnSuccess("Successfully created board!", additionalDetails = boardRequestMap)
            .logOnError("", "Failed to create board!", additionalDetails = boardRequestMap)

    }

}

