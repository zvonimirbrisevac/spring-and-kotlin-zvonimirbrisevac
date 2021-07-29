package com.infinum.academyproject.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.infinum.academyproject.models.CarModel

data class AddCarModelDTO(
    @JsonProperty("manufacturer") val manufacturer : String,
    @JsonProperty("model_name") val modelName : String,
    @JsonProperty("is_common") val isCommon : Int
) {
    fun toCarModel() = CarModel(0, manufacturer, modelName, isCommon != 0)
}
