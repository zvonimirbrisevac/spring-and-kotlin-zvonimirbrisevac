package com.infinum.academyproject.dto

import com.infinum.academyproject.models.Car
import java.time.LocalDate

data class CarDTO (
    val id: Long,
    val ownerId: Long,
    val addedDate: LocalDate = LocalDate.now(),
    val manufacturer: String,
    val productionYear: Int,
    val serialNumber: String
) {
    constructor(car : Car) : this (car.id, car.ownerId, car.addedDate, car.manufacturer, car.productionYear, car.serialNumber)

    fun toCar() : Car = Car(0, ownerId, addedDate, manufacturer, productionYear, serialNumber)
}