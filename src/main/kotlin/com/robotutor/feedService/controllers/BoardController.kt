package com.robotutor.feedService.controllers

import com.robotutor.feedService.controllers.view.BoardNameRequest
import com.robotutor.feedService.controllers.view.BoardRequest
import com.robotutor.feedService.controllers.view.BoardView
import com.robotutor.feedService.services.BoardService
import com.robotutor.iot.utils.filters.annotations.RequirePolicy
import com.robotutor.iot.utils.models.PremisesData
import com.robotutor.iot.utils.models.UserData
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/boards")
class BoardController(private val boardService: BoardService) {

    @RequirePolicy("BOARD:CREATE")
    @PostMapping
    fun createBoard(
        @RequestBody @Validated boardRequest: BoardRequest,
        userData: UserData,
        premisesData: PremisesData
    ): Mono<BoardView> {
        return boardService.addBoard(boardRequest, userData, premisesData).map { BoardView.from(it) }
    }

    @RequirePolicy("BOARD:READ")
    @GetMapping
    fun getBoards(premisesData: PremisesData): Flux<BoardView> {
        return boardService.getBoards(premisesData).map { BoardView.from(it) }
    }

    @RequirePolicy("BOARD:UPDATE")
    @PutMapping("/{boardId}/name")
    fun updateBoardName(
        @PathVariable boardId: String,
        @RequestBody @Validated boardNameRequest: BoardNameRequest,
        premisesData: PremisesData
    ): Mono<BoardView> {
        return boardService.updateBoardName(boardId, boardNameRequest, premisesData).map { BoardView.from(it) }
    }
}
