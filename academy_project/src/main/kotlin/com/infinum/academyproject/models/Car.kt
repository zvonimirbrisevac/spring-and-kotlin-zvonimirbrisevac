package com.infinum.academyproject.models

import java.time.LocalDate

data class Car(
    val ownerId: Long,
    val addedDate: LocalDate = LocalDate.now(),
    val manufacturer: String,
    val productionYear: Int,
    val serialNumber: String,
    var checkUps: List<CarCheckUp> = listOf<CarCheckUp>()
)
