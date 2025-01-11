package com.robotutor.feedService.repositories

import com.robotutor.feedService.models.Board
import com.robotutor.feedService.models.BoardId
import com.robotutor.feedService.models.PremisesId
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface BoardRepository : ReactiveCrudRepository<Board, BoardId> {
    fun findAllByPremisesId(premisesId: PremisesId): Flux<Board>
    fun findByPremisesIdAndBoardId(premisesId: PremisesId, boardId: BoardId): Mono<Board>
}
