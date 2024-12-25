package com.robotutor.deviceService.repositories

import com.robotutor.deviceService.models.Board
import com.robotutor.deviceService.models.BoardId
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BoardRepository : ReactiveCrudRepository<Board, BoardId> {
}
