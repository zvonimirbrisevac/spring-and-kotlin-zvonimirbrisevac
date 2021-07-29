package com.infinum.academyproject.services

import com.infinum.academyproject.dto.AddCarModelDTO
import com.infinum.academyproject.models.CarModel
import com.infinum.academyproject.models.CarModelsResponse
import com.infinum.academyproject.repositories.ModelRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Component
class HttpCarModelService (
    private val webClient: WebClient,
) {

    fun getCarModels() : List<AddCarModelDTO>? {
        return webClient.get()
            .uri("/api/v1/cars")
            .retrieve()
            .bodyToMono<CarModelsResponse>()
            .map { current -> current.models}
            .block()
            ?.distinct()
    }


}