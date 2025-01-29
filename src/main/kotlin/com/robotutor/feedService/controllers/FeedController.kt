package com.robotutor.feedService.controllers

import com.robotutor.feedService.controllers.view.FeedNameRequest
import com.robotutor.feedService.controllers.view.FeedRequest
import com.robotutor.feedService.controllers.view.FeedValueRequest
import com.robotutor.feedService.controllers.view.FeedView
import com.robotutor.feedService.services.FeedService
import com.robotutor.iot.utils.filters.annotations.RequirePolicy
import com.robotutor.iot.utils.models.PremisesData
import com.robotutor.iot.utils.models.UserData
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/feeds")
class FeedController(private val feedService: FeedService) {

    @RequirePolicy("FEED:CREATE")
    @PostMapping
    fun createFeed(
        @RequestBody @Validated feedRequest: FeedRequest,
        userData: UserData,
        premisesData: PremisesData
    ): Mono<FeedView> {
        return feedService.addFeed(feedRequest, userData, premisesData).map { FeedView.from(it) }
    }

    @RequirePolicy("FEED:READ")
    @GetMapping
    fun getFeeds(userData: UserData, premisesData: PremisesData): Flux<FeedView> {
        return feedService.getFeeds(userData, premisesData).map { FeedView.from(it) }
    }

    @RequirePolicy("FEED:UPDATE")
    @PutMapping("/{feedId}/name")
    fun updateName(
        @PathVariable feedId: String,
        @Validated @RequestBody feedNameRequest: FeedNameRequest,
        premisesData: PremisesData,
    ): Mono<FeedView> {
        return feedService.updateName(feedId, feedNameRequest, premisesData).map { FeedView.from(it) }
    }

    @RequirePolicy("FEED:UPDATE_VALUE")
    @PutMapping("/{feedId}/value")
    fun updateValue(
        @PathVariable feedId: String,
        @Validated @RequestBody feedValueRequest: FeedValueRequest,
        premisesData: PremisesData,
    ): Mono<FeedView> {
        return feedService.updateValue(feedId, feedValueRequest, premisesData).map { FeedView.from(it) }
    }
}
