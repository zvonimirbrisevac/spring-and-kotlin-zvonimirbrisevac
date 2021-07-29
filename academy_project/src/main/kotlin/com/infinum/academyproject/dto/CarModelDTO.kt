package com.infinum.academyproject.dto

import com.infinum.academyproject.models.CarModel
import javax.print.attribute.standard.PrinterMoreInfoManufacturer

data class CarModelDTO(
    val id: Long,
    val manufacturer: String,
    val modelName: String,
    val isCommon: Boolean
) {
    constructor(model: CarModel) : this(model.id, model.manufacturer, model.modelName, model.isCommon)

    fun toCarModel() : CarModel = CarModel(id, manufacturer, modelName, isCommon)


}