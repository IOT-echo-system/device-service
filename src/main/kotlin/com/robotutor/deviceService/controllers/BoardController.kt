package com.robotutor.deviceService.controllers

import com.robotutor.deviceService.controllers.view.BoardRequest
import com.robotutor.deviceService.controllers.view.BoardView
import com.robotutor.deviceService.services.BoardService
import com.robotutor.iot.utils.filters.annotations.RequirePolicy
import com.robotutor.iot.utils.models.UserData
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/boards")
class BoardController(private val boardService: BoardService) {

    @RequirePolicy("DEVICE:CREATE")
    @PostMapping
    fun createBoard(@RequestBody @Validated boardRequest: BoardRequest, userData: UserData): Mono<BoardView> {
        return boardService.addBoard(boardRequest, userData).map { BoardView.from(it) }
    }

}
