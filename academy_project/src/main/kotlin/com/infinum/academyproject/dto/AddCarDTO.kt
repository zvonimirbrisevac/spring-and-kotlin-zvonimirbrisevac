package com.infinum.academyproject.dto

import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarModel
import java.time.LocalDate

data class AddCarDTO(
    val ownerId: Long,
    val addedDate: LocalDate = LocalDate.now(),
    val manufacturer: String,
    val model: String,
    val productionYear: Int,
    val serialNumber: String
) {
    constructor(car : Car) : this(car.ownerId, car.addedDate, car.model.manufacturer,
        car.model.modelName, car.productionYear, car.serialNumber)

    fun toCar(modelFetcher: (String, String) -> CarModel) : Car =
        Car(0, ownerId, addedDate, modelFetcher.invoke(manufacturer, model), productionYear, serialNumber)

}