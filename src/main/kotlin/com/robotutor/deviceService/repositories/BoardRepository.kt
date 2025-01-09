package com.robotutor.deviceService.repositories

import com.robotutor.deviceService.models.Board
import com.robotutor.deviceService.models.BoardId
import com.robotutor.deviceService.models.PremisesId
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface BoardRepository : ReactiveCrudRepository<Board, BoardId> {
    fun findAllByPremisesId(premisesId: PremisesId): Flux<Board>
    fun findByPremisesIdAndBoardId(premisesId: PremisesId, boardId: BoardId): Mono<Board>
}
