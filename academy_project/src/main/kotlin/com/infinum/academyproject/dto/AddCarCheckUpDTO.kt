package com.infinum.academyproject.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.infinum.academyproject.models.CarCheckUp
import java.time.LocalDateTime

data class AddCarCheckUpDTO(
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    val timeAndDate: LocalDateTime,
    val workerName: String,
    val price: Double,
    val car: CarDTO
) {

    fun toCarCheckUp() : CarCheckUp = CarCheckUp(0, timeAndDate, workerName, price, car.toCar())
}