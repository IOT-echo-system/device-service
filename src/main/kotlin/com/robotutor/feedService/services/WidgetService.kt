package com.robotutor.feedService.services

import com.robotutor.feedService.controllers.view.WidgetNameRequest
import com.robotutor.feedService.controllers.view.WidgetRequest
import com.robotutor.feedService.models.IdType
import com.robotutor.feedService.models.Widget
import com.robotutor.feedService.models.WidgetId
import com.robotutor.feedService.models.kafka.AddWidgetMessage
import com.robotutor.feedService.repositories.WidgetRepository
import com.robotutor.iot.auditOnError
import com.robotutor.iot.auditOnSuccess
import com.robotutor.iot.models.KafkaTopicName
import com.robotutor.iot.service.IdGeneratorService
import com.robotutor.iot.services.KafkaPublisher
import com.robotutor.iot.utils.filters.validatePremisesOwner
import com.robotutor.iot.utils.models.PremisesData
import com.robotutor.iot.utils.utils.toMap
import com.robotutor.loggingstarter.Logger
import com.robotutor.loggingstarter.logOnError
import com.robotutor.loggingstarter.logOnSuccess
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class WidgetService(
    private val widgetRepository: WidgetRepository,
    private val idGeneratorService: IdGeneratorService,
    private val kafkaPublisher: KafkaPublisher
) {
    val logger = Logger(this::class.java)
    fun addWidget(widgetRequest: WidgetRequest, premisesData: PremisesData): Mono<Widget> {
        val widgetRequestMap = widgetRequest.toMap().toMutableMap()
        return validatePremisesOwner(premisesData) { idGeneratorService.generateId(IdType.WIDGET_ID) }
            .flatMap { widgetId ->
                widgetRequestMap["widgetId"] = widgetId
                val widget = Widget.from(widgetId, widgetRequest, premisesData)
                widgetRepository.save(widget)
            }
            .flatMap { widget ->
                kafkaPublisher.publish(KafkaTopicName.ADD_WIDGET, widget.widgetId, AddWidgetMessage.from(widget))
                    .map { widget }
            }
            .auditOnSuccess("WIDGET_CREATE", widgetRequestMap)
            .auditOnError("WIDGET_CREATE", widgetRequestMap)
            .logOnSuccess(logger, "Successfully created widget!", additionalDetails = widgetRequestMap)
            .logOnError(logger, "", "Failed to create widget!", additionalDetails = widgetRequestMap)
    }

    fun getWidgets(premisesData: PremisesData): Flux<Widget> {
        return widgetRepository.findAllByPremisesId(premisesData.premisesId)
    }

    fun updateName(widgetId: WidgetId, widgetNameRequest: WidgetNameRequest, premisesData: PremisesData): Mono<Widget> {
        val widgetRequestMap = widgetNameRequest.toMap()
        return validatePremisesOwner(premisesData) { widgetRepository.findByWidgetId(widgetId) }
            .flatMap { widget ->
                widgetRepository.save(widget.updateName(widgetNameRequest.name))
            }
            .auditOnSuccess("WIDGET_UPDATE", widgetRequestMap)
            .auditOnError("WIDGET_UPDATE", widgetRequestMap)
            .logOnSuccess(logger, "Successfully updated widget name!", additionalDetails = widgetRequestMap)
            .logOnError(logger, "", "Failed to update widget name!", additionalDetails = widgetRequestMap)
    }
}

