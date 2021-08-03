package com.infinum.academyproject.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.infinum.academyproject.dto.AddCarModelDTO

data class CarModelsResponse (
    @JsonProperty("data") val models : List<AddCarModelDTO>
        )