package com.infinum.academyproject.models

import java.time.LocalDate

data class Car(
    val ownerId: Long,
    val addedDate: LocalDate,
    val manufacturer: String,
    val productionYear: Int,
    val serialNumber: String,
    var checkUps: MutableList<CarCheckUp> = mutableListOf<CarCheckUp>()
)