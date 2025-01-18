package com.robotutor.feedService.services

import com.robotutor.feedService.controllers.view.FeedNameRequest
import com.robotutor.feedService.controllers.view.FeedRequest
import com.robotutor.feedService.models.Feed
import com.robotutor.feedService.models.IdType
import com.robotutor.feedService.repositories.FeedRepository
import com.robotutor.iot.auditOnError
import com.robotutor.iot.auditOnSuccess
import com.robotutor.iot.service.IdGeneratorService
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
class FeedService(
    private val feedRepository: FeedRepository,
    private val idGeneratorService: IdGeneratorService,
) {
    val logger = Logger(this::class.java)
    fun addFeed(feedRequest: FeedRequest, userData: UserData, premisesData: PremisesData): Mono<Feed> {
        val feedRequestMap = feedRequest.toMap().toMutableMap()
        return validatePremisesOwner(premisesData) { idGeneratorService.generateId(IdType.BOARD_ID) }
            .flatMap { feedId ->
                feedRequestMap["feedId"] = feedId
                val feed = Feed.from(feedId, feedRequest, userData, premisesData)
                feedRepository.save(feed)
            }
            .auditOnSuccess("FEED_CREATE", feedRequestMap)
            .auditOnError("FEED_CREATE", feedRequestMap)
            .logOnSuccess(logger, "Successfully created feed!", additionalDetails = feedRequestMap)
            .logOnError(logger, "", "Failed to create feed!", additionalDetails = feedRequestMap)
    }

    fun getFeeds(userData: UserData, premisesData: PremisesData): Flux<Feed> {
        return feedRepository.findAllByPremisesId(premisesData.premisesId)
    }

    fun updateName(feedId: String, feedNameRequest: FeedNameRequest, premisesData: PremisesData): Mono<Feed> {
        val feedRequestMap = feedNameRequest.toMap().toMutableMap()
        return validatePremisesOwner(premisesData) {
            feedRepository.findByFeedIdAndPremisesId(feedId, premisesData.premisesId)
        }
            .flatMap {
                feedRepository.save(it.updateName(feedNameRequest.name))
            }
            .auditOnSuccess("FEED_UPDATE", feedRequestMap)
            .auditOnError("FEED_UPDATE", feedRequestMap)
            .logOnSuccess(logger, "Successfully updated feed name", additionalDetails = feedRequestMap)
            .logOnError(logger, "", "Failed to update feed name", additionalDetails = feedRequestMap)
    }
}

