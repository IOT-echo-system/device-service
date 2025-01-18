package com.robotutor.feedService.services

import com.robotutor.feedService.controllers.view.BoardNameRequest
import com.robotutor.feedService.controllers.view.BoardRequest
import com.robotutor.feedService.models.Board
import com.robotutor.feedService.models.IdType
import com.robotutor.feedService.models.kafka.AddBoardMessage
import com.robotutor.feedService.repositories.BoardRepository
import com.robotutor.iot.auditOnError
import com.robotutor.iot.auditOnSuccess
import com.robotutor.iot.models.KafkaTopicName
import com.robotutor.iot.service.IdGeneratorService
import com.robotutor.iot.services.KafkaPublisher
import com.robotutor.iot.utils.filters.validatePremisesOwner
import com.robotutor.iot.utils.models.PremisesData
import com.robotutor.iot.utils.models.UserData
import com.robotutor.iot.utils.utils.toMap
import com.robotutor.loggingstarter.Logger
import com.robotutor.loggingstarter.logOnError
import com.robotutor.loggingstarter.logOnSuccess
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class BoardService(
    private val boardRepository: BoardRepository,
    private val idGeneratorService: IdGeneratorService,
    private val kafkaPublisher: KafkaPublisher
) {
    val logger = Logger(this::class.java)
    fun addBoard(boardRequest: BoardRequest, userData: UserData, premisesData: PremisesData): Mono<Board> {
        val boardRequestMap = boardRequest.toMap().toMutableMap()

        return validatePremisesOwner(premisesData) { idGeneratorService.generateId(IdType.BOARD_ID) }
            .flatMap { boardId ->
                boardRequestMap["boardId"] = boardId
                val board = Board.from(boardId, premisesData.premisesId, boardRequest, userData)
                boardRepository.save(board)
            }
            .flatMap { board ->
                kafkaPublisher.publish(KafkaTopicName.ADD_BOARD, board.boardId, AddBoardMessage.from(board))
                    .map { board }
            }
            .auditOnSuccess("BOARD_CREATE", boardRequestMap)
            .auditOnError("BOARD_CREATE", boardRequestMap)
            .logOnSuccess(logger, "Successfully created board!", additionalDetails = boardRequestMap)
            .logOnError(logger, "", "Failed to create board!", additionalDetails = boardRequestMap)
    }

    fun getBoards(premisesId: PremisesData): Flux<Board> {
        return boardRepository.findAllByPremisesId(premisesId.premisesId)
    }

    fun updateBoardName(boardId: String, boardNameRequest: BoardNameRequest, premisesData: PremisesData): Mono<Board> {
        val boardRequestMap = boardNameRequest.toMap().toMutableMap()
        boardRequestMap["boardId"] = boardId
        boardRequestMap["premisesId"] = premisesData.premisesId

        return validatePremisesOwner(premisesData) {
            boardRepository.findByPremisesIdAndBoardId(premisesData.premisesId, boardId)
        }
            .flatMap {
                boardRepository.save(it.updateName(boardNameRequest.name))
            }
            .auditOnSuccess("DEVICE_UPDATE", boardRequestMap)
            .auditOnError("DEVICE_UPDATE", boardRequestMap)
            .logOnSuccess(logger, "Successfully updated board name", additionalDetails = boardRequestMap)
            .logOnError(logger, "", "Failed to update board name", additionalDetails = boardRequestMap)
    }
}

