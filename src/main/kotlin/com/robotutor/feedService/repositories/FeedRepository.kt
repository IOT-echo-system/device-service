package com.robotutor.feedService.repositories

import com.robotutor.feedService.models.Feed
import com.robotutor.feedService.models.FeedId
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface FeedRepository : ReactiveCrudRepository<Feed, FeedId> {
    fun findAllByPremisesId(premisesId: String): Flux<Feed>
}
