package com.robotutor.feedService.repositories

import com.robotutor.feedService.models.PremisesId
import com.robotutor.feedService.models.Widget
import com.robotutor.feedService.models.WidgetId
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface WidgetRepository : ReactiveCrudRepository<Widget, WidgetId> {
    fun findAllByPremisesId(premisesId: PremisesId): Flux<Widget>
    fun findByWidgetId(widgetId: WidgetId): Mono<Widget>
}
