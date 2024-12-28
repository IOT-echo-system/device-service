package com.robotutor.deviceService.services

import com.robotutor.deviceService.repositories.DeviceRepository
import com.robotutor.iot.service.IdGeneratorService
import org.springframework.stereotype.Service

@Service
class DeviceService(
    private val boardService: BoardService,
    private val deviceRepository: DeviceRepository,
    private val idGeneratorService: IdGeneratorService,
) {
}

