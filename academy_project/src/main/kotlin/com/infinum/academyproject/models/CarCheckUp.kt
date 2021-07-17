package com.infinum.academyproject.models

import java.time.LocalDateTime

data class CarCheckUp(
    val timeAndDate: LocalDateTime,
    val workerName: String,
    val price: Double,
    val carId: Long
)
