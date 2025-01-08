package com.robotutor.deviceService.services

import com.robotutor.deviceService.controllers.view.BoardRequest
import com.robotutor.deviceService.models.Board
import com.robotutor.deviceService.models.IdType
import com.robotutor.deviceService.repositories.BoardRepository
import com.robotutor.iot.auditOnError
import com.robotutor.iot.auditOnSuccess
import com.robotutor.iot.exceptions.UnAuthorizedException
import com.robotutor.iot.service.IdGeneratorService
import com.robotutor.iot.utils.createMonoError
import com.robotutor.iot.utils.exceptions.IOTError
import com.robotutor.iot.utils.gateway.views.PremisesRole
import com.robotutor.iot.utils.models.PremisesData
import com.robotutor.iot.utils.models.UserData
import com.robotutor.iot.utils.utils.toMap
import com.robotutor.loggingstarter.logOnError
import com.robotutor.loggingstarter.logOnSuccess
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class BoardService(
    private val boardRepository: BoardRepository,
    private val idGeneratorService: IdGeneratorService,
) {
    fun addBoard(boardRequest: BoardRequest, userData: UserData, premisesData: PremisesData): Mono<Board> {
        val boardRequestMap = boardRequest.toMap().toMutableMap()
        if (premisesData.user.role != PremisesRole.OWNER) {
            return createMonoError<Board>(UnAuthorizedException(IOTError.IOT0104))
                .auditOnError("DEVICE_CREATE", boardRequestMap)
                .logOnError("", "Failed to create board!", additionalDetails = boardRequestMap)
        }

        return idGeneratorService.generateId(IdType.BOARD_ID)
            .flatMap { boardId ->
                boardRequestMap["boardId"] = boardId
                val board = Board.from(boardId, premisesData.premisesId, boardRequest, userData)
                boardRepository.save(board)
                    .auditOnSuccess("DEVICE_CREATE", boardRequestMap)
            }
            .auditOnError("DEVICE_CREATE", boardRequestMap)
            .logOnSuccess("Successfully created board!", additionalDetails = boardRequestMap)
            .logOnError("", "Failed to create board!", additionalDetails = boardRequestMap)

    }

    fun getBoards(premisesId: PremisesData): Flux<Board> {
        return boardRepository.findAllByPremisesId(premisesId.premisesId)
    }
}

