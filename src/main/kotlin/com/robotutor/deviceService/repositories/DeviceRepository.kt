package com.robotutor.deviceService.repositories

import com.robotutor.deviceService.models.Device
import com.robotutor.deviceService.models.DeviceId
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DeviceRepository : ReactiveCrudRepository<Device, DeviceId> {
}
