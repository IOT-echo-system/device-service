package com.robotutor.deviceService.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.premises")
data class PremisesConfig(val baseUrl: String, val premisesByOwner: String) {

}
