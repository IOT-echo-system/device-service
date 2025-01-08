package com.robotutor.deviceService.controllers

import com.robotutor.deviceService.controllers.view.BoardRequest
import com.robotutor.deviceService.controllers.view.BoardView
import com.robotutor.deviceService.models.PremisesId
import com.robotutor.deviceService.services.BoardService
import com.robotutor.iot.utils.filters.annotations.RequirePolicy
import com.robotutor.iot.utils.models.UserData
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/premises/{premisesId}/boards")
class BoardController(private val boardService: BoardService) {

    @RequirePolicy("DEVICE:CREATE")
    @PostMapping
    fun createBoard(
        @PathVariable premisesId: PremisesId,
        @RequestBody @Validated boardRequest: BoardRequest,
        userData: UserData,
    ): Mono<BoardView> {
        return boardService.addBoard(premisesId, boardRequest, userData).map { BoardView.from(it) }
    }

    @RequirePolicy("DEVICE:READ")
    @GetMapping
    fun getBoards(@PathVariable premisesId: PremisesId, userData: UserData): Flux<BoardView> {
        return boardService.getBoards(premisesId, userData).map { BoardView.from(it) }
    }
}
