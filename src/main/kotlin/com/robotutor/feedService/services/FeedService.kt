package com.robotutor.feedService.services

import com.robotutor.feedService.controllers.view.FeedRequest
import com.robotutor.feedService.models.Feed
import com.robotutor.feedService.models.IdType
import com.robotutor.feedService.repositories.FeedRepository
import com.robotutor.iot.auditOnError
import com.robotutor.iot.auditOnSuccess
import com.robotutor.iot.exceptions.UnAuthorizedException
import com.robotutor.iot.service.IdGeneratorService
import com.robotutor.iot.utils.createMono
import com.robotutor.iot.utils.createMonoError
import com.robotutor.iot.utils.exceptions.IOTError
import com.robotutor.iot.utils.gateway.views.PremisesRole
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
        return createMono(premisesData.user.role == PremisesRole.OWNER)
            .flatMap {
                if (it) idGeneratorService.generateId(IdType.BOARD_ID)
                else createMonoError(UnAuthorizedException(IOTError.IOT0104))
            }
            .flatMap { feedId ->
                feedRequestMap["feedId"] = feedId
                val feed = Feed.from(feedId, feedRequest, userData, premisesData)
                feedRepository.save(feed)
                    .auditOnSuccess("FEED_CREATE", feedRequestMap)
            }
            .auditOnError("FEED_CREATE", feedRequestMap)
            .logOnSuccess(logger, "Successfully created feed!", additionalDetails = feedRequestMap)
            .logOnError(logger, "", "Failed to create feed!", additionalDetails = feedRequestMap)
    }

    fun getFeeds(userData: UserData, premisesData: PremisesData): Flux<Feed> {
        return feedRepository.findAllByPremisesId(premisesData.premisesId)
    }
}

