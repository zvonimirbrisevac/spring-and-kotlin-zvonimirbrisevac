package com.infinum.academyproject.dto

import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarModel
import java.time.LocalDate

data class CarDTO (
    val id: Long,
    val ownerId: Long,
    val addedDate: LocalDate = LocalDate.now(),
    val manufacturer: String,
    val model: String,
    val productionYear: Int,
    val serialNumber: String
) {
    constructor(car : Car) : this(car.id, car.ownerId,
        car.addedDate, car.model.manufacturer, car.model.modelName, car.productionYear, car.serialNumber)

    fun toCar(modelFetcher: (String, String) -> CarModel) : Car =
        Car(id, ownerId, addedDate, modelFetcher.invoke(manufacturer, model), productionYear, serialNumber)
}