package com.infinum.academyproject.models

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import kotlin.time.measureTimedValue

data class CarCheckUp(
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm") val timeAndDate: LocalDateTime,
    val workerName: String,
    val price: Double,
    val carId: Long
)
