package com.robotutor.feedService.services

import com.robotutor.feedService.controllers.view.BoardNameRequest
import com.robotutor.feedService.controllers.view.BoardRequest
import com.robotutor.feedService.models.Board
import com.robotutor.feedService.models.IdType
import com.robotutor.feedService.repositories.BoardRepository
import com.robotutor.iot.auditOnError
import com.robotutor.iot.auditOnSuccess
import com.robotutor.iot.exceptions.UnAuthorizedException
import com.robotutor.iot.service.IdGeneratorService
import com.robotutor.iot.utils.createMono
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

        return createMono(premisesData.user.role == PremisesRole.OWNER)
            .flatMap {
                if (it) idGeneratorService.generateId(IdType.BOARD_ID)
                else createMonoError(UnAuthorizedException(IOTError.IOT0104))
            }
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

    fun updateBoardName(boardId: String, boardNameRequest: BoardNameRequest, premisesData: PremisesData): Mono<Board> {
        val boardRequestMap = boardNameRequest.toMap().toMutableMap()
        boardRequestMap["boardId"] = boardId
        boardRequestMap["premisesId"] = premisesData.premisesId

        return createMono(premisesData.user.role == PremisesRole.OWNER)
            .flatMap {
                if (it) boardRepository.findByPremisesIdAndBoardId(premisesData.premisesId, boardId)
                else createMonoError(UnAuthorizedException(IOTError.IOT0104))
            }
            .flatMap {
                boardRepository.save(it.updateName(boardNameRequest.name))
            }
            .auditOnSuccess("DEVICE_UPDATE", boardRequestMap)
            .auditOnError("DEVICE_UPDATE", boardRequestMap)
            .logOnSuccess("Successfully updated board name", additionalDetails = boardRequestMap)
            .logOnError("", "Failed to update board name", additionalDetails = boardRequestMap)
    }
}

