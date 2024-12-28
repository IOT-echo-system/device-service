package com.robotutor.deviceService.models

import com.robotutor.deviceService.controllers.view.BoardRequest
import com.robotutor.iot.utils.models.UserData
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

const val DEVICE_COLLECTION = "devices"

@TypeAlias("Device")
@Document(DEVICE_COLLECTION)
data class Device(
    @Id
    var id: ObjectId? = null,
    @Indexed(unique = true)
    val deviceId: DeviceId,
    val boardId: BoardId,
    val premisesId: PremisesId,
    val name: String,
    val type: DeviceType,
    val createdBy: String,
    val pins: List<Pin> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        fun from(boardId: BoardId, boardRequest: BoardRequest, userData: UserData): Board {
            return Board(
                boardId = boardId,
                premisesId = boardRequest.premisesId,
                name = boardRequest.name,
                type = boardRequest.type,
                createdBy = userData.userId
            )
        }
    }
}

enum class DeviceType {
    INPUT,
    OUTPUT,
}

data class Pin(val pinNo: String, val name: String)

typealias DeviceId = String
