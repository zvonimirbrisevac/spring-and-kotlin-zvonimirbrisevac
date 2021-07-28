package com.infinum.academyproject.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import java.time.LocalDateTime

data class CarCheckUpNoCarDTO(
    val id : Long,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    val timeAndDate: LocalDateTime,
    val workerName: String,
    val price: Double,
) {
    constructor(checkUp : CarCheckUp) : this(
        checkUp.id, checkUp.timeAndDate, checkUp.workerName, checkUp.price
    )
}