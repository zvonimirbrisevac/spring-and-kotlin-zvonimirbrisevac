package com.infinum.academyproject.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import java.time.LocalDateTime
import javax.persistence.ManyToOne

data class CarCheckUpDTO(
    val id: Long,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    val timeAndDate: LocalDateTime,
    val workerName: String,
    val price: Double,
    val car: CarDTO
) {
    constructor(checkUp : CarCheckUp ) : this(checkUp.id, checkUp.timeAndDate, checkUp.workerName,
    checkUp.price, CarDTO(checkUp.car))

    fun toCarCheckUp() : CarCheckUp = CarCheckUp(0, timeAndDate, workerName, price, car.toCar())
}