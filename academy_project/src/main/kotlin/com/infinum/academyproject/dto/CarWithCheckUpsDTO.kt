package com.infinum.academyproject.dto

import com.infinum.academyproject.models.Car
import com.infinum.academyproject.models.CarCheckUp
import java.time.LocalDate

data class CarWithCheckUpsDTO(
    val id: Long,
    val ownerId: Long,
    val addedDate: LocalDate,
    val manufacturer: String,
    val model: String,
    val productionYear: Int,
    val serialNumber: String,
    val checkUpsDTO: List<CarCheckUpNoCarDTO>
) {
    constructor(car : Car, checkUps: List<CarCheckUp>) : this(car.id, car.ownerId, car.addedDate,
        car.model.manufacturer, car.model.modelName, car.productionYear, car.serialNumber,
        checkUps.map {CarCheckUpNoCarDTO(it)}
    )
}
