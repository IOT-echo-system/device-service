package com.robotutor.deviceService.gateway

import com.robotutor.deviceService.config.PremisesConfig
import com.robotutor.deviceService.gateway.view.Premises
import com.robotutor.deviceService.models.PremisesId
import com.robotutor.iot.service.WebClientWrapper
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PremisesGateway(private val webClient: WebClientWrapper, private val premisesConfig: PremisesConfig) {
    fun getPremisesByOwner(premisesId: PremisesId): Mono<Premises> {
        return webClient.get(
            baseUrl = premisesConfig.baseUrl,
            path = premisesConfig.premisesByOwner,
            uriVariables = mapOf("premisesId" to premisesId),
            returnType = Premises::class.java
        )
    }
}
