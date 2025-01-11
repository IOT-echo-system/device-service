package com.robotutor.deviceService.controllers

import com.robotutor.deviceService.controllers.view.FeedRequest
import com.robotutor.deviceService.controllers.view.FeedView
import com.robotutor.deviceService.services.FeedService
import com.robotutor.iot.utils.filters.annotations.RequirePolicy
import com.robotutor.iot.utils.models.PremisesData
import com.robotutor.iot.utils.models.UserData
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/premises/*/feeds")
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
    fun getFeeds(
        userData: UserData,
        premisesData: PremisesData
    ): Flux<FeedView> {
        return feedService.getFeeds(userData, premisesData).map { FeedView.from(it) }
    }
}
