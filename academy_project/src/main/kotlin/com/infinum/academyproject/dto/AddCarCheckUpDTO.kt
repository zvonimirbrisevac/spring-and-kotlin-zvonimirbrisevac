package com.infinum.academyproject.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import java.time.LocalDateTime

data class AddCarCheckUpDTO(
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    val timeAndDate: LocalDateTime,
    val workerName: String,
    val price: Double,
    val carId: Long
) {

    constructor(checkUp : CarCheckUp) : this(
        checkUp.timeAndDate, checkUp.workerName, checkUp.price, checkUp.car.id)

    fun toCarCheckUp(fetcher: (Long) -> Car) : CarCheckUp = CarCheckUp(
        0, timeAndDate, workerName, price, fetcher.invoke(carId))
}