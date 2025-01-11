package com.robotutor.deviceService.repositories

import com.robotutor.deviceService.models.Feed
import com.robotutor.deviceService.models.FeedId
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface FeedRepository : ReactiveCrudRepository<Feed, FeedId> {
    fun findAllByPremisesId(premisesId: String): Flux<Feed>
}
