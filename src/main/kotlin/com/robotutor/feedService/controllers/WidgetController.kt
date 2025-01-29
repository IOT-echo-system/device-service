package com.robotutor.feedService.controllers

import com.robotutor.feedService.controllers.view.WidgetNameRequest
import com.robotutor.feedService.controllers.view.WidgetRequest
import com.robotutor.feedService.controllers.view.WidgetView
import com.robotutor.feedService.models.WidgetId
import com.robotutor.feedService.services.WidgetService
import com.robotutor.iot.utils.filters.annotations.RequirePolicy
import com.robotutor.iot.utils.models.PremisesData
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/widgets")
class WidgetController(private val widgetService: WidgetService) {

    @RequirePolicy("WIDGET:CREATE")
    @PostMapping
    fun createWidget(
        @RequestBody @Validated widgetRequest: WidgetRequest,
        premisesData: PremisesData
    ): Mono<WidgetView> {
        return widgetService.addWidget(widgetRequest, premisesData).map { WidgetView.from(it) }
    }

    @RequirePolicy("WIDGET:READ")
    @GetMapping
    fun getFeeds(premisesData: PremisesData): Flux<WidgetView> {
        return widgetService.getWidgets(premisesData).map { WidgetView.from(it) }
    }

    @RequirePolicy("WIDGET:UPDATE")
    @PutMapping("/{widgetId}/name")
    fun updateName(
        @PathVariable widgetId: WidgetId,
        @Validated @RequestBody widgetNameRequest: WidgetNameRequest,
        premisesData: PremisesData,
    ): Mono<WidgetView> {
        return widgetService.updateName(widgetId, widgetNameRequest, premisesData).map { WidgetView.from(it) }
    }
}
