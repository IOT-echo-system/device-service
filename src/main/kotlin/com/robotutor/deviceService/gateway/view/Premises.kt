package com.robotutor.deviceService.gateway.view

import com.robotutor.deviceService.models.PremisesId
import java.time.LocalDateTime

data class Premises(
    val premisesId: PremisesId,
    val name: String,
    val address: Map<String, Any>,
    val createdAt: LocalDateTime
)
